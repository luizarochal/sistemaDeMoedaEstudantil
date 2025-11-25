import Header from "../welcomePage/coponentes/Header.jsx";
import PageInfo from "./componentes/pageInfo.jsx";
import { useState, useEffect } from "react";

export default function ListAlunos() {
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchVantagens();
  }, []);

  const fetchVantagens = async () => {
    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      if (!token) {
        setError("Usuário não autenticado");
        setLoading(false);
        return;
      }

      const response = await fetch("http://localhost:8081/api/vantagens/sem-resgates", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`Erro ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setVantagens(data);
      setLoading(false);
    } catch (err) {
      console.error("Erro ao buscar vantagens:", err);
      setError("Erro ao carregar vantagens");
      setLoading(false);
    }
  };

  const refreshVantagens = () => {
    setLoading(true);
    setError(null);
    fetchVantagens();
  };

  return (
    <div className="bg-purple-400 min-h-screen">
      <Header />
      <PageInfo 
        vantagens={vantagens} 
        loading={loading} 
        error={error} 
        onRefresh={refreshVantagens} 
      />
    </div>
  );
}