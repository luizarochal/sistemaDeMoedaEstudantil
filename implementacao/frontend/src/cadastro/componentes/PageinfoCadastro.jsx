import React from "react";

export default function PageinfoCadastro({ onToggle}) {
  const [userType, setUserType] = React.useState("aluno");

  const handleUserTypeChange = (event) => {
    setUserType(event.target.value);
  };

  const cpforcnpj = (type) => {
    if (type === "aluno" || type === "professor") {
      return "CPF";
    } else if (type === "empresa") {
      return "CNPJ";
    }
    return "";
  };

  const renderUserFields = () => {
    switch (userType) {
      case "aluno":
        return (
          <>
            <input 
              type="text" 
              placeholder="RG" 
              className="mb-4 p-2 rounded-md border border-gray-200"
            />
            <input 
              type="text" 
              placeholder="Endereço" 
              className="mb-4 p-2 rounded-md border border-gray-200"
            />
            <input 
              type="text" 
              placeholder="Instituição de Ensino" 
              className="mb-6 p-2 rounded-md border border-gray-200"
            />
            <input 
              type="text" 
              placeholder="Curso" 
              className="mb-6 p-2 rounded-md border border-gray-200"
            />
          </>
        );
      
      case "empresa":
        return (
          <>
            <input 
              type="text" 
              placeholder="endereço da Empresa" 
              className="mb-4 p-2 rounded-md border border-gray-200"
            />
          </>
        );
      
      default:
        return null;
    }
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
          <option value="professor" disabled>Professor</option>
          <option value="empresa">Empresa</option>
        </select>


        <input 
            type="text" 
            placeholder="Nome completo" 
            className="mb-4 p-2 rounded-md border border-gray-200"
          />
          <input 
            type="text" 
            placeholder={cpforcnpj(userType)}
            className="mb-4 p-2 rounded-md border border-gray-200"
          />
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

           {renderUserFields()}
        

        <button className="bg-amber-400 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition">
          Entrar
        </button>
        <p className="mt-2">Já tem uma conta? <a onClick={onToggle} className="text-purple-700 cursor-pointer">Entre</a></p>

        </div>

      </div>
    </div>
  );
}
