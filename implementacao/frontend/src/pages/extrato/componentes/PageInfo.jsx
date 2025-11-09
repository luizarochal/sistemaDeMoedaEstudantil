import React from "react";
import { useNavigate } from "react-router-dom";

const transactions = [
	{
		id: 1,
		type: "Compra",
		description: "Livraria",
		date: "2025-11-12",
		amount: -50,
	},
	{
		id: 2,
		type: "Recompensa",
		description: "Atividade",
		date: "2025-11-10",
		amount: 200,
	},
];

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

export default function PageInfo() {
	const navigate = useNavigate();

	return (
		<div className="w-full min-h-screen bg-white">
			<button type="button" className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:border-black" onClick={() => navigate('/homePage')} ><i className="fa-solid fa-angle-left"></i> Voltar</button>
			<div className="p-10 max-w-4xl mx-auto">
				<h1 className="text-2xl font-bold text-purple-700 mb-4">Extrato</h1>
				<p className="text-gray-600 mb-6">Aqui estão suas transações recentes:</p>

				{transactions.length === 0 ? (
					<div className="text-center text-gray-500">Nenhuma transação encontrada.</div>
				) : (
					<ul className="space-y-4">
						{transactions.map((tx) => (
							<li key={tx.id} className="flex justify-between p-4 bg-gray-50 rounded-lg shadow-sm">
								<div>
									<div className="font-semibold">{tx.type} - {tx.description}</div>
									<div className="text-sm text-gray-500">{formatDate(tx.date)}</div>
								</div>
								<div className={`${tx.amount < 0 ? "text-red-600" : "text-green-600"} font-bold`}>
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

