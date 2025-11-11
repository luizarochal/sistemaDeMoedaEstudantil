import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");
  const [query, setQuery] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchAlunos() {
      try {
        const response = await fetch('/api/alunos');
        if (!response.ok) {
          throw new Error('Não foi possível carregar os alunos.');
        }
        const data = await response.json();
        const formattedUsers = data.map(aluno => ({ id: aluno.id, name: aluno.nome, course: aluno.curso, details: `CPF: ${aluno.cpf}` }));
        setUsers(formattedUsers);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    fetchAlunos();
  }, []);

  function openModal(user) {
    setSelectedUser(user);
    setAmount("");
    setMessage("");
    setShowModal(true);
  }

  function closeModal() {
    setShowModal(false);
    setSelectedUser(null);
  }

  async function confirmSend() {
    const value = Number(amount);
    if (!value || value <= 0) {
      alert("Informe uma quantia válida de moedas (maior que 0).");
      return;
    }

    try {
      const response = await fetch('/api/transacoes-prof', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Se você usa autenticação (ex: JWT), adicione o header aqui:
          // 'Authorization': `Bearer ${seu_token_jwt}`
        },
        body: JSON.stringify({
          alunoId: selectedUser.id,
          quantidadeMoedas: value,
          mensagem: message
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Falha ao enviar moedas.');
      }

      alert(`Enviadas ${value} moedas para ${selectedUser.name} com sucesso!`);
      closeModal();
    } catch (error) {
      alert(error.message || "Ocorreu um erro ao processar a transação.");
    }
  }

  return (

    <div className="w-full bg-white h-screen">
      <button type="button" className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:border-black" onClick={() => navigate('/homePage')} ><i className="fa-solid fa-angle-left"></i> Voltar</button>
      <div className="text-left p-10 w-full max-w-7xl mx-auto">
        <div className="flex flex-row mt-10">
          <input
            type="text"
            placeholder="Buscar por nome, matrícula ou curso..."
            className="border border-gray-300 rounded-l-lg py-2 px-4 w-full"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            className="bg-gray-200 rounded-r-lg rounded-tl-none rounded-bl-none py-2 px-4 hover:border-b-gray-400"
            type="button"
            onClick={() => { }}
          ><i className="fa-solid fa-magnifying-glass"></i></button>
        </div>


        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mt-6">
          {loading && <div className="col-span-full text-center py-6">Carregando alunos...</div>}
          {error && (
            <div className="col-span-full text-center text-red-500 py-6">
              Erro ao carregar alunos.
            </div>
          )}
          {(() => {
            const q = query.trim().toLowerCase();
            const filtered = q
              ? users.filter((u) => {
                return (
                  u.name.toLowerCase().includes(q) ||
                  u.course.toLowerCase().includes(q) ||
                  u.details.toLowerCase().includes(q)
                );
              })
              : users;
            if (filtered.length === 0) {
              return (
                <div className="col-span-full text-center text-gray-500 py-6">Nenhum usuário encontrado para "{query}".</div>
              );
            }
            return filtered.map((u) => (
              <div key={u.id} className="flex shadow-md items-center justify-between p-4 bg-gray-50 rounded-lg shadow-sm">
                <div className="flex items-center space-x-4">
                  <div className="w-12 h-12 rounded-full bg-purple-600 text-white flex items-center justify-center text-lg">
                    <i className="fa-solid fa-user"></i>
                  </div>
                  <div>
                    <div className="font-semibold text-black">{u.name}</div>
                    <div className="text-sm text-gray-500">{u.course} </div>
                    <div className="text-sm text-gray-500"> {u.details}</div>
                  </div>
                </div>

                <div>
                  <button type="button" onClick={() => openModal(u)} className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-orange-400"><i className="fa-solid fa-money-bill-transfer"></i></button>
                </div>
              </div>
            ));
          })()}
        </div>
        {showModal && selectedUser && (
          <div className="fixed inset-0 z-50 flex items-center justify-center">
            <div className="absolute inset-0 bg-black opacity-40" onClick={closeModal} />
            <div className="relative bg-white rounded-lg shadow-lg w-11/12 max-w-md p-6 z-10">
              <h2 className="text-lg font-semibold mb-2">Enviar moedas para {selectedUser.name}</h2>
              <label className="block text-sm text-gray-600">Quantia</label>
              <input
                type="number"
                min="1"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                className="w-full border rounded px-3 py-2 mb-3"
                placeholder="Quantidade de moedas"
              />

              <label className="block text-sm text-gray-600">Mensagem (opcional)</label>
              <textarea
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                className="w-full border rounded px-3 py-2 mb-4"
                placeholder="Escreva uma mensagem..."
              />

              <div className="flex justify-center space-x-2">
                <button type="button" onClick={closeModal} className="px-4 py-2 rounded border hover:border-gray-300">Cancelar</button>
                <button type="button" onClick={confirmSend} className="px-4 py-2 rounded bg-purple-600 text-white hover:border-b-purple-900">Confirmar</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
