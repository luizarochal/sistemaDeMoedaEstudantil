import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
	const [transactions, setTransactions] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const navigate = useNavigate();

	useEffect(() => {
		async function fetchExtrato() {
			try {
				const response = await fetch('/api/transacoes-prof');
				if (!response.ok) {
					throw new Error('Não foi possível carregar o extrato.');
				}
				const data = await response.json();
				setTransactions(data);
			} catch (err) {
				setError(err.message);
			} finally {
				setLoading(false);
			}
		}

		fetchExtrato();
	}, []);


	function formatDate(iso) {
		try {
			const d = new Date(iso);
			return d.toLocaleDateString(undefined, { day: "2-digit", month: "short", year: "numeric" });
		} catch {
			return iso;
		}
	}

	function formatAmount(value) {
		const abs = Math.abs(value);
		return (value < 0 ? "-" : "+") + abs;
	}

	return (
		<div className="w-full min-h-screen bg-white">
			<button type="button" className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:border-black" onClick={() => navigate('/homePage')} ><i className="fa-solid fa-angle-left"></i> Voltar</button>
			<div className="p-10 max-w-4xl mx-auto">
				<h1 className="text-2xl font-bold text-purple-700 mb-4">Extrato</h1>
				<p className="text-gray-600 mb-6">Aqui estão suas transações recentes:</p>

				{loading && <div className="text-center text-gray-500 py-6">Carregando extrato...</div>}

				{error && <div className="text-center text-red-500 py-6">Erro ao carregar o extrato. Por favor, tente novamente mais tarde.</div>}

				{!loading && !error && transactions.length === 0 && (
					<div className="text-center text-gray-500">Nenhuma transação encontrada.</div>
				)}

				{!loading && !error && transactions.length > 0 && (
					<ul className="space-y-4">
						{transactions.map((tx) => (
							<li key={tx.id} className="flex justify-between p-4 bg-gray-50 rounded-lg shadow-sm">
								<div>
									{/* No backend, 'type' pode ser 'Recompensa' ou 'Resgate' */}
									<div className="font-semibold text-black">{tx.type} - {tx.description}</div>
									<div className="text-sm text-gray-500">{formatDate(tx.date)}</div>
								</div>
								<div className={`${tx.amount < 0 ? "text-red-600" : "text-green-600"} font-bold`}>
									{/* 'amount' deve vir positivo para recompensas e negativo para resgates */}
									{formatAmount(tx.amount)}
								</div>
							</li>
						))}
					</ul>
				)}
			</div>
		</div>
	);
}
