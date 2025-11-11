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
  const [professorInfo, setProfessorInfo] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
        
        if (!token) {
          throw new Error("Usuário não autenticado");
        }

        // Buscar informações do professor logado
        const userEmail = localStorage.getItem("userEmail") || sessionStorage.getItem("userEmail");
        const professoresResponse = await fetch('http://localhost:8081/api/professores', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (professoresResponse.ok) {
          const professores = await professoresResponse.json();
          const professor = professores.find(p => p.email === userEmail);
          if (professor) {
            setProfessorInfo(professor);
          }
        }

        // Buscar lista de alunos
        const alunosResponse = await fetch('http://localhost:8081/api/alunos', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!alunosResponse.ok) {
          throw new Error(`Erro ao carregar alunos: ${alunosResponse.status}`);
        }

        const data = await alunosResponse.json();
        const formattedUsers = data.map(aluno => ({ 
          id: aluno.id, 
          name: aluno.nome, 
          course: aluno.curso, 
          details: `CPF: ${aluno.cpf}`,
          email: aluno.email,
          quantidadeMoedas: aluno.quantidadeMoedas || 0
        }));
        setUsers(formattedUsers);
      } catch (err) {
        console.error("Erro ao buscar dados:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
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

    // Verificar se temos as informações do professor
    if (!professorInfo) {
      alert("Erro: Informações do professor não encontradas.");
      return;
    }

    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      
      if (!token) {
        throw new Error("Usuário não autenticado");
      }

      // Verificar se o usuário é professor
      const userRole = localStorage.getItem("userRole") || sessionStorage.getItem("userRole");
      if (userRole !== "ROLE_PROFESSOR") {
        throw new Error("Apenas professores podem enviar moedas");
      }

      // Verificar saldo do professor
      if (professorInfo.quantidadeMoedas < value) {
        throw new Error(`Saldo insuficiente. Você possui ${professorInfo.quantidadeMoedas} moedas, mas tentou enviar ${value}`);
      }

      const response = await fetch('http://localhost:8081/api/transacoes-prof', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          professorId: professorInfo.id,
          alunoId: selectedUser.id,
          quantidadeMoedas: value,
          mensagem: message || `Recompensa do professor ${professorInfo.nome}`
        }),
      });

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error("Token expirado ou inválido");
        }
        if (response.status === 402) {
          throw new Error("Saldo insuficiente de moedas");
        }
        const errorText = await response.text();
        throw new Error(errorText || 'Falha ao enviar moedas.');
      }

      alert(`Enviadas ${value} moedas para ${selectedUser.name} com sucesso!`);
      
      // Atualizar a lista de alunos para refletir o novo saldo
      setUsers(prevUsers => 
        prevUsers.map(user => 
          user.id === selectedUser.id 
            ? { ...user, quantidadeMoedas: (user.quantidadeMoedas || 0) + value }
            : user
        )
      );

      // Atualizar saldo do professor
      setProfessorInfo(prev => ({
        ...prev,
        quantidadeMoedas: prev.quantidadeMoedas - value
      }));
      
      closeModal();
    } catch (error) {
      console.error("Erro ao enviar moedas:", error);
      alert(error.message || "Ocorreu um erro ao processar a transação.");
    }
  }

  const filteredUsers = query.trim() 
    ? users.filter(user => 
        user.name.toLowerCase().includes(query.toLowerCase()) ||
        user.course.toLowerCase().includes(query.toLowerCase()) ||
        user.details.toLowerCase().includes(query.toLowerCase()) ||
        user.email.toLowerCase().includes(query.toLowerCase())
      )
    : users;

  return (
    <div className="w-full bg-white min-h-screen">
      <button 
        type="button" 
        className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition"
        onClick={() => navigate('/homePage')}
      >
        <i className="fa-solid fa-angle-left mr-2"></i> Voltar
      </button>
      
      <div className="text-left p-10 w-full max-w-7xl mx-auto pt-20">
        <div className="mb-6">
          <h1 className="text-3xl font-bold text-purple-700 mb-2">Lista de Alunos</h1>
          <p className="text-gray-600">Selecione um aluno para enviar moedas</p>
          
          {/* Informações do professor */}
          {professorInfo && (
            <div className="bg-purple-50 border border-purple-200 rounded-lg p-4 mt-4">
              <div className="flex justify-between items-center">
                <div>
                  <span className="font-semibold text-purple-700">Professor: </span>
                  <span>{professorInfo.nome}</span>
                </div>
                <div>
                  <span className="font-semibold text-purple-700">Saldo disponível: </span>
                  <span className="text-amber-600 font-bold">
                    <i className="fa-solid fa-coins mr-1"></i>
                    {professorInfo.quantidadeMoedas} moedas
                  </span>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Barra de busca */}
        <div className="flex flex-row mb-6">
          <input
            type="text"
            placeholder="Buscar por nome, curso, CPF ou email..."
            className="border border-gray-300 rounded-l-lg py-3 px-4 w-full focus:outline-none focus:ring-2 focus:ring-purple-500"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            className="bg-purple-600 text-white rounded-r-lg py-3 px-6 hover:bg-purple-700 transition"
            type="button"
          >
            <i className="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>

        {/* Lista de alunos */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {loading && (
            <div className="col-span-full text-center py-8">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mx-auto mb-2"></div>
              Carregando alunos...
            </div>
          )}
          
          {error && (
            <div className="col-span-full text-center text-red-500 py-6 bg-red-50 rounded-lg">
              <i className="fa-solid fa-exclamation-triangle text-xl mb-2"></i>
              <div className="font-semibold">Erro ao carregar alunos</div>
              <div className="text-sm mt-1">{error}</div>
            </div>
          )}
          
          {!loading && !error && filteredUsers.length === 0 && (
            <div className="col-span-full text-center text-gray-500 py-8 bg-gray-50 rounded-lg">
              <i className="fa-solid fa-users text-4xl mb-3 text-gray-400"></i>
              <div className="font-semibold">
                {query ? `Nenhum aluno encontrado para "${query}"` : "Nenhum aluno cadastrado"}
              </div>
            </div>
          )}

          {!loading && !error && filteredUsers.map((user) => (
            <div key={user.id} className="flex flex-col bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition p-4">
              <div className="flex items-center space-x-4 mb-3">
                <div className="w-12 h-12 rounded-full bg-gradient-to-r from-purple-500 to-purple-600 text-white flex items-center justify-center text-lg">
                  <i className="fa-solid fa-user-graduate"></i>
                </div>
                <div className="flex-1">
                  <div className="font-semibold text-gray-800 text-lg">{user.name}</div>
                  <div className="text-sm text-gray-600">{user.course}</div>
                  <div className="text-xs text-gray-500">{user.details}</div>
                </div>
              </div>

              <div className="flex justify-between items-center mt-auto">
                <div className="text-sm text-gray-600">
                  <i className="fa-solid fa-coins text-amber-500 mr-1"></i>
                  {user.quantidadeMoedas || 0} moedas
                </div>
                <button 
                  type="button" 
                  onClick={() => openModal(user)} 
                  className="bg-amber-500 hover:bg-amber-600 text-white px-4 py-2 rounded-lg transition flex items-center"
                  disabled={!professorInfo || professorInfo.quantidadeMoedas <= 0}
                >
                  <i className="fa-solid fa-money-bill-transfer mr-2"></i>
                  Enviar
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Modal de envio de moedas */}
        {showModal && selectedUser && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
            <div className="absolute inset-0 bg-black opacity-40" onClick={closeModal} />
            <div className="relative bg-white rounded-lg shadow-xl w-full max-w-md p-6 z-10">
              <div className="flex items-center mb-4">
                <div className="w-10 h-10 rounded-full bg-purple-100 text-purple-600 flex items-center justify-center mr-3">
                  <i className="fa-solid fa-user-graduate"></i>
                </div>
                <div>
                  <h2 className="text-lg font-semibold">Enviar moedas</h2>
                  <p className="text-sm text-gray-600">Para: {selectedUser.name}</p>
                </div>
              </div>

              {professorInfo && (
                <div className="bg-amber-50 border border-amber-200 rounded-lg p-3 mb-4">
                  <div className="flex justify-between text-sm">
                    <span className="text-amber-700">Seu saldo:</span>
                    <span className="font-semibold text-amber-700">
                      <i className="fa-solid fa-coins mr-1"></i>
                      {professorInfo.quantidadeMoedas} moedas
                    </span>
                  </div>
                </div>
              )}

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Quantia de moedas
                  </label>
                  <input
                    type="number"
                    min="1"
                    max={professorInfo ? professorInfo.quantidadeMoedas : 0}
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-500"
                    placeholder="Digite a quantidade"
                  />
                  {professorInfo && (
                    <p className="text-xs text-gray-500 mt-1">
                      Máximo: {professorInfo.quantidadeMoedas} moedas disponíveis
                    </p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Mensagem (opcional)
                  </label>
                  <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-purple-500"
                    placeholder="Motivo da recompensa..."
                    rows="3"
                  />
                </div>
              </div>

              <div className="flex justify-end space-x-3 mt-6">
                <button 
                  type="button" 
                  onClick={closeModal} 
                  className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition"
                >
                  Cancelar
                </button>
                <button 
                  type="button" 
                  onClick={confirmSend} 
                  className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition flex items-center"
                  disabled={!amount || Number(amount) <= 0}
                >
                  <i className="fa-solid fa-paper-plane mr-2"></i>
                  Enviar Moedas
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}