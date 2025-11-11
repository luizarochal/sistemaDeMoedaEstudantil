import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../welcomePage/coponentes/header.jsx";
import HomePageInfo from "./componentes/pageInfo.jsx";
import FunctionButtons from "./componentes/functionButtons.jsx";

export default function HomePage() {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar se o usuário está autenticado
    const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
    
    if (!token) {
      // Redirecionar para login se não estiver autenticado
      navigate("/cadastro");
      return;
    }
    
    setIsAuthenticated(true);
    setLoading(false);
  }, [navigate]);

  if (loading) {
    return (
      <div className="bg-purple-400 min-h-screen flex items-center justify-center">
        <div className="text-white text-xl">Carregando...</div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return null;
  }

  return (
    <div className="bg-purple-400 min-h-screen">
      <Header />
      <HomePageInfo/>
      <FunctionButtons/>
    </div>
  );
}