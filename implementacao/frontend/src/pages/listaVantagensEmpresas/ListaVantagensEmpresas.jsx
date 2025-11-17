import Header from "../welcomePage/coponentes/Header.jsx";
import PageInfo from "./componentes/pageInfo.jsx";
import { useState, useEffect } from "react";

export default function ListaVantagemEmpresa() {
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [empresaInfo, setEmpresaInfo] = useState({
    cnpj: "",
    nome: ""
  });

  useEffect(() => {
    loadEmpresaInfo();
  }, []);

  const loadEmpresaInfo = async () => {
    try {
      const token = localStorage.getItem("authToken");
      const userEmail = localStorage.getItem("userEmail");
      
      if (!token || !userEmail) {
        setError("Usuário não autenticado");
        setLoading(false);
        return;
      }

      // Buscar informações do usuário pelo email
      const userResponse = await fetch(`http://localhost:8081/api/usuarios/email/${userEmail}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!userResponse.ok) {
        throw new Error("Erro ao buscar informações da empresa");
      }

      const userData = await userResponse.json();
      
      // Agora precisamos buscar os dados específicos da empresa pelo ID
      const empresaResponse = await fetch(`http://localhost:8081/api/empresas/${userData.cnpj}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!empresaResponse.ok) {
        throw new Error("Empresa não encontrada");
      }

      const empresaData = await empresaResponse.json();
      
      setEmpresaInfo({
        cnpj: empresaData.cnpj,
        nome: empresaData.nome
      });

      // Buscar vantagens da empresa
      await fetchVantagens(empresaData.cnpj, token);

    } catch (err) {
      console.error("Erro ao carregar informações da empresa:", err);
      setError(err.message);
      setLoading(false);
    }
  };

  const fetchVantagens = async (cnpj, token) => {
    try {
      const response = await fetch(`http://localhost:8081/api/vantagens/empresa/${cnpj}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
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

  const refreshVantagens = async () => {
    setLoading(true);
    setError(null);
    await loadEmpresaInfo();
  };

  return (
    <div className="bg-purple-400 min-h-screen">
      <Header />
      <PageInfo 
        vantagens={vantagens} 
        loading={loading} 
        error={error} 
        onRefresh={refreshVantagens}
        empresaInfo={empresaInfo}
      />
    </div>
  );
}