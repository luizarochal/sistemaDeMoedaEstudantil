import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
	const [transactions, setTransactions] = useState([]);
	const [resgateTransactions, setResgateTransactions] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [userInfo, setUserInfo] = useState({
		nome: "",
		role: "",
		quantidadeMoedas: 0,
		userId: null
	});
	const navigate = useNavigate();

	const fetchExtrato = useCallback(async (userId, userRole, token) => {
		try {
			let apiUrl = "";

			if (userRole === "ROLE_PROFESSOR") {
				apiUrl = `http://localhost:8081/api/transacoes-prof/professor/${userId}`;
			} else if (userRole === "ROLE_ALUNO") {
				apiUrl = `http://localhost:8081/api/transacoes-prof/aluno/${userId}`;
			} else {
				setTransactions([]);
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
					return;
				}
				throw new Error(`Erro ao carregar extrato: ${response.status}`);
			}

			const data = await response.json();
			console.log("Transações recebidas:", data);
			setTransactions(data);

		} catch (err) {
			console.error("Erro ao buscar extrato:", err);
			// Não setar erro aqui para não bloquear o carregamento das transações de resgate
		}
	}, []);

	const fetchResgateTransactions = useCallback(async (userId, userRole, token) => {
		try {
			// Buscar transações de resgate (apenas para alunos)
			if (userRole === "ROLE_ALUNO") {
				const response = await fetch(`http://localhost:8081/api/transacoes-resgate/aluno/${userId}`, {
					method: "GET",
					headers: {
						"Authorization": `Bearer ${token}`,
						"Content-Type": "application/json"
					}
				});

				if (response.ok) {
					const data = await response.json();
					console.log("Transações de resgate recebidas:", data);
					setResgateTransactions(data);
				} else if (response.status !== 404) {
					console.error("Erro ao buscar transações de resgate:", response.status);
				}
			}
		} catch (err) {
			console.error("Erro ao buscar transações de resgate:", err);
		} finally {
			setLoading(false);
		}
	}, []);

	const loadUserInfo = useCallback(async () => {
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

			// Buscar extratos após obter o ID do usuário
			if (userData.id) {
				await Promise.all([
					fetchExtrato(userData.id, userData.role, token),
					fetchResgateTransactions(userData.id, userData.role, token)
				]);
			} else {
				throw new Error("ID do usuário não encontrado");
			}

		} catch (err) {
			console.error("Erro ao carregar informações do usuário:", err);
			setError(err.message);
			setLoading(false);
		}
	}, [fetchExtrato, fetchResgateTransactions]);

	useEffect(() => {
		loadUserInfo();
	}, [loadUserInfo]);

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
				type: "TRANSFERENCIA",
				icon: "fa-arrow-up",
				color: "red"
			};
		} else if (userRole === "ROLE_ALUNO") {
			return {
				description: `Recebido de ${transaction.professorNome || "Professor"}`,
				amount: transaction.quantidadeMoedas,
				type: "RECOMPENSA",
				icon: "fa-arrow-down",
				color: "green"
			};
		}
		
		return {
			description: transaction.mensagem || "Transação",
			amount: transaction.quantidadeMoedas,
			type: "TRANSACAO",
			icon: "fa-exchange",
			color: "blue"
		};
	}

	function getResgateTransactionDisplayInfo(transaction) {
		return {
			description: `Resgate: ${transaction.vantagemNome || "Vantagem"}`,
			amount: -transaction.custoMoedas,
			type: "RESGATE",
			icon: "fa-ticket",
			color: "purple",
			cupom: transaction.codigoCupom
		};
	}

	// Combinar e ordenar todas as transações por data
	const allTransactions = [
		...transactions.map(tx => ({
			...tx,
			isResgate: false,
			displayInfo: getTransactionDisplayInfo(tx, userInfo.role),
			date: tx.dataTransacao
		})),
		...resgateTransactions.map(tx => ({
			...tx,
			isResgate: true,
			displayInfo: getResgateTransactionDisplayInfo(tx),
			date: tx.dataResgate
		}))
	].sort((a, b) => new Date(b.date) - new Date(a.date));

	function refreshExtrato() {
		setLoading(true);
		setError(null);
		setTransactions([]);
		setResgateTransactions([]);
		loadUserInfo();
	}

	return (
		<div className="w-full min-h-screen bg-white">
			{/* Header com informações do usuário */}
			<div className="bg-gray-100 text-white p-4">
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
						<h1 className="text-2xl text-black font-bold">Extrato</h1>
						<p className="text-gray-600">{userInfo.nome}</p>
						<p className="text-gray-600 text-sm">
							{userInfo.role === "ROLE_PROFESSOR" ? "Professor" : 
							 userInfo.role === "ROLE_ALUNO" ? "Aluno" : "Usuário"}
						</p>
					</div>
					
					<div className="w-20"></div>
				</div>
			</div>

			<div className="p-6 max-w-4xl mx-auto">
				{/* Saldo atual */}
				<div className="bg-purple-500 text-white p-6 rounded-lg shadow-lg mb-6">
					<h2 className="text-lg text-purple-100 font-semibold mb-2">Saldo Atual</h2>
					<div className="flex items-center text-yellow-400">
					<i className="fa-solid text-2xl fa-coins mr-1"></i>
					<p className="text-3xl text-yellow-400 font-bold">{userInfo.quantidadeMoedas} moedas</p>
					</div>
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

				{!loading && !error && allTransactions.length === 0 && (
					<div className="text-center text-gray-500 py-8 bg-gray-50 rounded-lg">
						<i className="fa-solid fa-receipt text-4xl mb-3 text-gray-400"></i>
						<div className="font-semibold">Nenhuma transação encontrada</div>
						<p className="text-sm mt-1">Suas transações aparecerão aqui</p>
					</div>
				)}

				{!loading && !error && allTransactions.length > 0 && (
					<div className="space-y-3">
						{allTransactions.map((tx, index) => {
							const displayInfo = tx.displayInfo;
							
							return (
								<div 
									key={tx.id || `resgate-${index}`} 
									className="flex justify-between items-center p-4 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition"
								>
									<div className="flex items-center">
										<div className={`w-10 h-10 rounded-full flex items-center justify-center mr-4 ${
											displayInfo.color === 'green' ? 'bg-green-100 text-green-600' :
											displayInfo.color === 'red' ? 'bg-red-100 text-red-600' :
											displayInfo.color === 'purple' ? 'bg-purple-100 text-purple-600' :
											'bg-blue-100 text-blue-600'
										}`}>
											<i className={`fa-solid ${displayInfo.icon}`}></i>
										</div>
										<div>
											<div className="font-semibold text-gray-800">
												{displayInfo.description}
											</div>
											<div className="text-sm text-gray-500">
												{tx.mensagem || (tx.isResgate ? `Cupom: ${displayInfo.cupom}` : "Transação de moedas")}
											</div>
											<div className="text-xs text-gray-400">
												{formatDate(tx.date)}
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