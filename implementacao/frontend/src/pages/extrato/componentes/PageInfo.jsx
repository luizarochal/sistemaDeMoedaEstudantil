import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
	const [transactions, setTransactions] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [userInfo, setUserInfo] = useState({
		nome: "",
		role: "",
		quantidadeMoedas: 0,
		userId: null
	});
	const navigate = useNavigate();

	useEffect(() => {
		loadUserInfo();
	}, []);

	async function loadUserInfo() {
		try {
			const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
			const email = localStorage.getItem("userEmail") || sessionStorage.getItem("userEmail");
			
			if (!token || !email) {
				throw new Error("Usuário não autenticado");
			}

			// Buscar informações completas do usuário
			const userResponse = await fetch(`http://localhost:8081/api/usuarios/email/${email}`, {
				method: "GET",
				headers: {
					"Authorization": `Bearer ${token}`,
					"Content-Type": "application/json"
				}
			});

			if (!userResponse.ok) {
				throw new Error("Erro ao buscar informações do usuário");
			}

			const userData = await userResponse.json();
			
			setUserInfo({
				nome: userData.nome || "Usuário",
				role: userData.role || "",
				quantidadeMoedas: userData.quantidadeMoedas || 0,
				userId: userData.id
			});

			// Buscar extrato após obter o ID do usuário
			if (userData.id) {
				await fetchExtrato(userData.id, userData.role, token);
			} else {
				throw new Error("ID do usuário não encontrado");
			}

		} catch (err) {
			console.error("Erro ao carregar informações do usuário:", err);
			setError(err.message);
			setLoading(false);
		}
	}

	async function fetchExtrato(userId, userRole, token) {
		try {
			let apiUrl = "";

			if (userRole === "ROLE_PROFESSOR") {
				apiUrl = `http://localhost:8081/api/transacoes-prof/professor/${userId}`;
			} else if (userRole === "ROLE_ALUNO") {
				apiUrl = `http://localhost:8081/api/transacoes-prof/aluno/${userId}`;
			} else {
				setError("Extrato não disponível para este tipo de usuário");
				setLoading(false);
				return;
			}

			console.log("Buscando extrato na URL:", apiUrl);

			const response = await fetch(apiUrl, {
				method: "GET",
				headers: {
					"Authorization": `Bearer ${token}`,
					"Content-Type": "application/json"
				}
			});

			console.log("Status da resposta:", response.status);

			if (!response.ok) {
				if (response.status === 403) {
					throw new Error("Acesso negado. Verifique suas permissões.");
				} else if (response.status === 401) {
					throw new Error("Token expirado ou inválido");
				} else if (response.status === 404) {
					// Nenhuma transação encontrada - não é um erro
					setTransactions([]);
					setLoading(false);
					return;
				}
				throw new Error(`Erro ao carregar extrato: ${response.status}`);
			}

			const data = await response.json();
			console.log("Transações recebidas:", data);
			setTransactions(data);

		} catch (err) {
			console.error("Erro ao buscar extrato:", err);
			setError(err.message);
		} finally {
			setLoading(false);
		}
	}

	function formatDate(dateString) {
		try {
			if (!dateString) return "Data não disponível";
			
			const date = new Date(dateString);
			if (isNaN(date.getTime())) return dateString;
			
			return date.toLocaleDateString("pt-BR", { 
				day: "2-digit", 
				month: "2-digit", 
				year: "numeric",
				hour: "2-digit",
				minute: "2-digit"
			});
		} catch {
			return dateString || "Data não disponível";
		}
	}

	function getTransactionDisplayInfo(transaction, userRole) {
		if (userRole === "ROLE_PROFESSOR") {
			return {
				description: `Transferência para ${transaction.alunoNome || "Aluno"}`,
				amount: -transaction.quantidadeMoedas,
				type: "TRANSFERENCIA"
			};
		} else if (userRole === "ROLE_ALUNO") {
			return {
				description: `Recebido de ${transaction.professorNome || "Professor"}`,
				amount: transaction.quantidadeMoedas,
				type: "RECOMPENSA"
			};
		}
		
		return {
			description: transaction.mensagem || "Transação",
			amount: transaction.quantidadeMoedas,
			type: "TRANSACAO"
		};
	}

	function refreshExtrato() {
		setLoading(true);
		setError(null);
		loadUserInfo();
	}

	return (
		<div className="w-full min-h-screen bg-white">
			{/* Header com informações do usuário */}
			<div className="bg-purple-600 text-white p-4">
				<div className="max-w-4xl mx-auto flex justify-between items-center">
					<button 
						type="button" 
						className="flex items-center bg-purple-700 hover:bg-purple-800 text-white px-4 py-2 rounded-lg transition"
						onClick={() => navigate('/homePage')}
					>
						<i className="fa-solid fa-angle-left mr-2"></i> 
						Voltar
					</button>
					
					<div className="text-center">
						<h1 className="text-2xl font-bold">Extrato</h1>
						<p className="text-purple-200">{userInfo.nome}</p>
						<p className="text-purple-200 text-sm">
							{userInfo.role === "ROLE_PROFESSOR" ? "Professor" : 
							 userInfo.role === "ROLE_ALUNO" ? "Aluno" : "Usuário"}
						</p>
					</div>
					
					<div className="w-20"></div>
				</div>
			</div>

			<div className="p-6 max-w-4xl mx-auto">
				{/* Saldo atual */}
				<div className="bg-gradient-to-r from-purple-500 to-purple-600 text-white p-6 rounded-lg shadow-lg mb-6">
					<h2 className="text-lg font-semibold mb-2">Saldo Atual</h2>
					<p className="text-3xl font-bold">{userInfo.quantidadeMoedas} moedas</p>
				</div>

				<h2 className="text-xl font-bold text-purple-700 mb-4">Transações Recentes</h2>

				{loading && (
					<div className="text-center text-gray-500 py-8">
						<div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mx-auto mb-2"></div>
						Carregando extrato...
					</div>
				)}

				{error && (
					<div className="text-center text-red-500 py-6 bg-red-50 rounded-lg">
						<i className="fa-solid fa-exclamation-triangle text-xl mb-2"></i>
						<div className="font-semibold">Erro ao carregar extrato</div>
						<div className="text-sm mt-1">{error}</div>
						<button 
							onClick={refreshExtrato}
							className="mt-3 bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg transition"
						>
							Tentar Novamente
						</button>
					</div>
				)}

				{!loading && !error && transactions.length === 0 && (
					<div className="text-center text-gray-500 py-8 bg-gray-50 rounded-lg">
						<i className="fa-solid fa-receipt text-4xl mb-3 text-gray-400"></i>
						<div className="font-semibold">Nenhuma transação encontrada</div>
						<p className="text-sm mt-1">Suas transações aparecerão aqui</p>
					</div>
				)}

				{!loading && !error && transactions.length > 0 && (
					<div className="space-y-3">
						{transactions.map((tx, index) => {
							const displayInfo = getTransactionDisplayInfo(tx, userInfo.role);
							
							return (
								<div 
									key={tx.id || index} 
									className="flex justify-between items-center p-4 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition"
								>
									<div className="flex items-center">
										<div className={`w-10 h-10 rounded-full flex items-center justify-center mr-4 ${
											displayInfo.amount > 0 ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600'
										}`}>
											<i className={`fa-solid ${
												displayInfo.amount > 0 ? 'fa-arrow-down' : 'fa-arrow-up'
											}`}></i>
										</div>
										<div>
											<div className="font-semibold text-gray-800">
												{displayInfo.description}
											</div>
											<div className="text-sm text-gray-500">
												{tx.mensagem || "Transação de moedas"}
											</div>
											<div className="text-xs text-gray-400">
												{formatDate(tx.dataTransacao)}
											</div>
										</div>
									</div>
									<div className={`font-bold text-lg ${
										displayInfo.amount > 0 ? 'text-green-600' : 'text-red-600'
									}`}>
										{displayInfo.amount > 0 ? '+' : ''}{displayInfo.amount} moedas
									</div>
								</div>
							);
						})}
					</div>
				)}

				{/* Botão para recarregar */}
				{!loading && (
					<div className="text-center mt-6">
						<button 
							onClick={refreshExtrato}
							className="bg-purple-600 hover:bg-purple-700 text-white px-6 py-2 rounded-lg transition flex items-center mx-auto"
						>
							<i className="fa-solid fa-rotate mr-2"></i>
							Atualizar Extrato
						</button>
					</div>
				)}
			</div>
		</div>
	);
}