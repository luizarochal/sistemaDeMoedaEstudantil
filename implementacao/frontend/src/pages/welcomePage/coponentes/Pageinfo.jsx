import React from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
  const navigate = useNavigate();
  return (
    <div className="flex justify-center items-center mt-40">
      <div className="bg-white rounded-3xl shadow-xl text-center p-10 max-w-md animate-bounce-slow">
        <h1 className="text-2xl font-bold text-purple-700 mb-4">
          🏫 Sistema de Moeda Estudantil
        </h1>
        <p className="text-gray-600 mb-6">
          Ganhe moedas estudando, colaborando e participando de atividades!  
          Transforme o aprendizado em diversão.
        </p>
        <button
          className="bg-amber-400 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition"
          onClick={() => navigate('/cadastro')}
        >
          Entrar
        </button>
      </div>
    </div>
  );
}
