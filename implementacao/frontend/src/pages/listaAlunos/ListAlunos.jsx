import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../welcomePage/coponentes/header.jsx";
import PageInfo from "./componentes/pageInfo.jsx";

export default function ListAlunos() {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [userRole, setUserRole] = useState("");

  useEffect(() => {
    // Verificar se o usuário está autenticado e é professor
    const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
    const role = localStorage.getItem("userRole") || sessionStorage.getItem("userRole");
    
    if (!token) {
      navigate("/cadastro");
      return;
    }

    if (role !== "ROLE_PROFESSOR") {
      alert("Acesso permitido apenas para professores");
      navigate("/homePage");
      return;
    }

    setIsAuthenticated(true);
    setUserRole(role);
    setLoading(false);
  }, [navigate]);

  if (loading) {
    return (
      <div className="bg-purple-400 min-h-screen flex items-center justify-center">
        <div className="text-white text-xl">Carregando...</div>
      </div>
    );
  }

  if (!isAuthenticated || userRole !== "ROLE_PROFESSOR") {
    return null;
  }

  return (
    <div className="bg-purple-400 min-h-screen">
      <Header />
      <PageInfo />
    </div>
  );
}