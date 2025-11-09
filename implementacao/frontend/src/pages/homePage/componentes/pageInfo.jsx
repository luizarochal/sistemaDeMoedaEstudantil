import React from "react";
import { useNavigate } from "react-router-dom";

export default function PageInfo() {
  const navigate = useNavigate();

  return (
    <div className="w-full bg-white">
      <div className="text-left p-10 w-full max-w-7xl mx-auto">
        <div className="flex justify-between items-center w-full">
        <div className="flex items-center">
          <div className="w-20 h-20 bg-gray-300 rounded-full flex items-center justify-center">
            <i className="fa-solid fa-user text-4xl" aria-hidden="true"></i>
          </div>
            
            <div className="flex flex-col ml-4">
            <h1 className="flex text-3xl font-bold text-purple-700 mb-2 ">
              Nome do usuário
            </h1>
            <p className="text-gray-600">Informações adicionais do usuário</p>
            <p className="text-gray-600">Informações adicionais do usuário</p>
            </div>
        </div>

        <div className="flex flex-col ml-auto items-start ">
            <h2 className="text-2xl font-bold text-purple-700">Saldo disponível</h2>
            <p className="text-4xl font-semibold text-green-600">1500 Moedas</p>
            <p className="text-gray-600 bg-gray-200 px-2 rounded-lg cursor-pointer hover:bg-gray-300"  onClick={() => navigate('/extrato')} >Ver extrato <i className="fa-solid fa-angle-right"></i></p>
        </div>
       </div>
      </div>
    </div>
  );
}
