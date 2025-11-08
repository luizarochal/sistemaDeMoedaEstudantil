import React from "react";

export default function PageinfoEntrar({ onToggle }) {
  const [userType, setUserType] = React.useState("aluno");

  const handleUserTypeChange = (event) => {
    setUserType(event.target.value);
  };


  return (
    <div className="flex justify-center items-center">
      <div className="bg-white rounded-3xl shadow-xl text-center p-10 w-11/12 max-w-2xl mt-10">

        <h1 className="text-2xl font-bold text-purple-700 mb-4">
          Cadastro de Usuário
        </h1>
        <div className="flex flex-col">

        <select className="mb-6 p-2 rounded-md border border-gray-200"
        value={userType}
        onChange={handleUserTypeChange}
        >
          <option value="aluno">Aluno</option>
          <option value="professor">Professor</option>
          <option value="administrador">Empresa</option>
        </select>

          <input 
            type="email" 
            placeholder="E-mail" 
            className="mb-4 p-2 rounded-md border border-gray-200"
          />
          <input 
            type="password" 
            placeholder="Senha" 
            className="mb-4 p-2 rounded-md border border-gray-200"
          />
        

        <button className="bg-amber-400 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition">
          Entrar
        </button>
        <p className="mt-2">Ainda não tem uma conta? <a onClick={onToggle} className="text-purple-700 cursor-pointer">Cadastre-se</a></p>

        </div>

      </div>
    </div>
  );
}
