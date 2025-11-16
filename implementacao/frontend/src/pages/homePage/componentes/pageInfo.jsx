import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState({
    nome: "",
    email: "",
    role: "",
    quantidadeMoedas: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Buscar informações do usuário do localStorage e da API
    const loadUserInfo = async () => {
      try {
        // Informações básicas do localStorage
        const userName = localStorage.getItem("userName") || sessionStorage.getItem("userName");
        const userEmail = localStorage.getItem("userEmail") || sessionStorage.getItem("userEmail");
        const userRole = localStorage.getItem("userRole") || sessionStorage.getItem("userRole");
        const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");

        setUserInfo(prev => ({
          ...prev,
          nome: userName || "Usuário",
          email: userEmail || "",
          role: userRole || ""
        }));

        // Buscar informações específicas da API baseado no role
        if (token && userEmail) {
          let userData;
          
          if (userRole === "ROLE_PROFESSOR") {
            // Buscar dados do professor
            const response = await fetch("http://localhost:8081/api/professores", {
              headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
              }
            });
            
            if (response.ok) {
              const professores = await response.json();
              const professor = professores.find(p => p.email === userEmail);
              if (professor) {
                userData = professor;
              }
            }
          } else if (userRole === "ROLE_ALUNO") {
            // Buscar dados do aluno
            const response = await fetch("http://localhost:8081/api/alunos", {
              headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
              }
            });
            
            if (response.ok) {
              const alunos = await response.json();
              const aluno = alunos.find(a => a.email === userEmail);
              if (aluno) {
                userData = aluno;
              }
            }
          } else if (userRole === "ROLE_EMPRESA") {
            // Buscar dados da empresa
            const response = await fetch("http://localhost:8081/api/empresas", {
              headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
              }
            });
            
            if (response.ok) {
              const empresas = await response.json();
              const empresa = empresas.find(e => e.email === userEmail);
              if (empresa) {
                userData = empresa;
              }
            }
          }

          if (userData) {
            setUserInfo(prev => ({
              ...prev,
              quantidadeMoedas: userData.quantidadeMoedas || 0,
              // Adicionar outras informações específicas se necessário
              instituicao: userData.instituicao,
              curso: userData.curso,
              cnpj: userData.cnpj
            }));
          }
        }
      } catch (error) {
        console.error("Erro ao carregar informações do usuário:", error);
      } finally {
        setLoading(false);
      }
    };

    loadUserInfo();
  }, []);

  const getAdditionalInfo = () => {
    switch (userInfo.role) {
      case "ROLE_ALUNO":
        return [
          userInfo.instituicao ? `Instituição: ${userInfo.instituicao}` : "Instituição não informada",
          userInfo.curso ? `Curso: ${userInfo.curso}` : "Curso não informado"
        ];
      case "ROLE_PROFESSOR":
        return [
          "Professor",
          userInfo.instituicao ? `Instituição: ${userInfo.instituicao}` : "Instituição não informada"
        ];
      case "ROLE_EMPRESA":
        return [
          "Empresa Parceira",
          userInfo.cnpj ? `CNPJ: ${userInfo.cnpj}` : "CNPJ não informado"
        ];
      default:
        return ["Informações adicionais do usuário"];
    }
  };

  if (loading) {
    return (
      <div className="w-full bg-white">
        <div className="text-left p-10 w-full max-w-7xl mx-auto">
          <div className="flex justify-between items-center w-full">
            <div className="flex items-center">
              <div className="w-20 h-20 bg-gray-300 rounded-full flex items-center justify-center">
                <i className="fa-solid fa-user text-4xl" aria-hidden="true"></i>
              </div>
              <div className="flex flex-col ml-4">
                <h1 className="text-3xl font-bold text-purple-700 mb-2">Carregando...</h1>
                <p className="text-gray-600">Buscando informações</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  const additionalInfo = getAdditionalInfo();

  return (
    <div className="w-full bg-white">
      <div className="text-left p-10 w-full max-w-7xl mx-auto">
        <div className="flex justify-between items-center w-full">
          <div className="flex items-center">
            <div className="w-20 h-20 bg-gray-300 rounded-full flex items-center justify-center">
              <i className="fa-solid fa-user text-4xl" aria-hidden="true"></i>
            </div>
            
            <div className="flex flex-col ml-4">
              <h1 className="flex text-3xl font-bold text-purple-700 mb-2">
                {userInfo.nome}
              </h1>
              {additionalInfo.map((info, index) => (
                <p key={index} className="text-gray-600">{info}</p>
              ))}
            </div>
          </div>

          {(userInfo.role === "ROLE_PROFESSOR" || userInfo.role === "ROLE_ALUNO") && (
            <div className="flex flex-col ml-auto items-start">
              <h2 className="text-2xl font-bold text-purple-700">Saldo disponível</h2>
              <p className="text-4xl font-semibold text-green-600">
                {userInfo.quantidadeMoedas} Moedas
              </p>
              <p 
                className="text-gray-600 bg-gray-200 px-2 rounded-lg cursor-pointer hover:bg-gray-300" 
                onClick={() => navigate('/extrato')}
              >
                Ver extrato <i className="fa-solid fa-angle-right"></i>
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}