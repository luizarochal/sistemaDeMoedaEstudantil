import React from "react";

export default function PageInfo() {
  return (
    <div className="flex justify-center items-center mt-40">
      <div className="bg-white rounded-3xl shadow-xl text-center p-10 max-w-md animate-bounce-slow">

        <h1 className="text-2xl font-bold text-purple-700 mb-4">
          Cadastro de Usuário
        </h1>
        <div className="flex flex-col">

        <select className="mb-6 p-2 rounded-md border border-gray-200">
          <option value="aluno">Aluno</option>
          <option value="professor">Professor</option>
          <option value="administrador">Empresa</option>
        </select>

        

        <button className="bg-amber-400 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition">
          Entrar
        </button>

        </div>

      </div>
    </div>
  );
}
