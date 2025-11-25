import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function FunctionButtons() {
  const navigate = useNavigate();
  const [userRole, setUserRole] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const role = localStorage.getItem("userRole") || sessionStorage.getItem("userRole");
    setUserRole(role || "");
    setLoading(false);
  }, []);

  if (loading) {
    return (
      <div className="mx-32 mt-10">
        <p>Carregando...</p>
      </div>
    );
  }

  if (userRole === "ROLE_PROFESSOR") {
     return (
    <div className="mx-32 mt-10">
      <button 
        className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2"
        onClick={() => navigate('/listaAlunos')}
      >
        Premiar um aluno
      </button>
      <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Cadastrar atividade
      </button>
      <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Ver meus alunos
      </button>
      <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Relatórios
      </button>
    </div>
  );
  }
  else if(userRole === "ROLE_ALUNO"){
    return (
    <div className="mx-32 mt-10">
      <button 
        className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2"
        onClick={() => navigate('/listaVantagensAluno')}
      >
        Resgatar vantagens
      </button>

      <button 
        className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2"
        onClick={() => navigate('/vantagensResgatadas')}
      >
        Vantagens já resgatadas
      </button>
      {
        /*
        <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Cadastrar atividade
      </button>
      <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Ver meus alunos
      </button>
      <button className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">
        Relatórios
      </button>
        */
      }
    </div>
  );
  }  
  else {
    return(
    <div className="mx-32 mt-10">
      <button 
        className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2"
        onClick={() => navigate('/listaVantagensEmpresas')}
      >
        Vantagens cadastradas
      </button>
    </div>
  );
  }
}