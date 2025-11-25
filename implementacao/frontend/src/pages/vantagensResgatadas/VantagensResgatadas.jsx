import Header from "../welcomePage/coponentes/Header.jsx";
import PageInfoResgatadas from "./componentes/pageInfoResgatadas.jsx";
import { useState, useEffect, useCallback } from "react";

export default function VantagensResgatadas() {
  const [resgates, setResgates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [alunoInfo, setAlunoInfo] = useState({
    id: null,
    nome: "",
    quantidadeMoedas: 0
  });

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
      
      setAlunoInfo({
        id: userData.id,
        nome: userData.nome || "Aluno",
        quantidadeMoedas: userData.quantidadeMoedas || 0
      });

      // Buscar resgates após obter o ID do aluno
      if (userData.id) {
        await fetchResgates(userData.id, token);
      } else {
        throw new Error("ID do aluno não encontrado");
      }

    } catch (err) {
      console.error("Erro ao carregar informações do usuário:", err);
      setError(err.message);
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadUserInfo();
  }, [loadUserInfo]);

  async function fetchResgates(alunoId, token) {
    try {
      const response = await fetch(`http://localhost:8081/api/resgates/aluno/${alunoId}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        if (response.status === 404) {
          // Nenhum resgate encontrado - não é um erro
          setResgates([]);
          setLoading(false);
          return;
        }
        throw new Error(`Erro ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setResgates(data);
      setLoading(false);
    } catch (err) {
      console.error("Erro ao buscar resgates:", err);
      setError("Erro ao carregar vantagens resgatadas");
      setLoading(false);
    }
  }

  const refreshResgates = () => {
    setLoading(true);
    setError(null);
    loadUserInfo();
  };

  return (
    <div className="bg-purple-400 min-h-screen">
      <Header />
      <PageInfoResgatadas 
        resgates={resgates} 
        loading={loading} 
        error={error} 
        onRefresh={refreshResgates}
        alunoInfo={alunoInfo}
      />
    </div>
  );
}