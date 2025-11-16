import React from "react";

export default function PageinfoCadastro({ onToggle }) {
  const [userType, setUserType] = React.useState("aluno");
  const [formData, setFormData] = React.useState({
    nome: "",
    cpf: "",
    cnpj: "",
    email: "",
    password: "",
    rg: "",
    endereco: "",
    instituicao: "",
    curso: ""
  });
  const [loading, setLoading] = React.useState(false);

  const handleUserTypeChange = (event) => {
    setUserType(event.target.value);
    // Limpar campos específicos ao mudar o tipo
    setFormData(prev => ({
      ...prev,
      cpf: "",
      cnpj: "",
      rg: "",
      instituicao: "",
      curso: ""
    }));
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const getPlaceholder = () => {
    if (userType === "aluno") {
      return "CPF";
    } else if (userType === "empresa") {
      return "CNPJ";
    }
    return "CPF/CNPJ";
  };

  const getFieldName = () => {
    if (userType === "aluno") {
      return "cpf";
    } else if (userType === "empresa") {
      return "cnpj";
    }
    return "cpf";
  };

  const handleRegister = async (event) => {
    event.preventDefault();
    setLoading(true);

    // Determinar role baseado no tipo de usuário
    let role;
    switch (userType) {
      case "aluno":
        role = "ROLE_ALUNO";
        break;
      case "empresa":
        role = "ROLE_EMPRESA";
        break;
      default:
        role = "ROLE_ALUNO";
    }

    // Preparar dados para envio
    const registerData = {
      nome: formData.nome,
      email: formData.email,
      password: formData.password,
      role: role,
      endereco: formData.endereco || ""
    };

    // Adicionar campos específicos
    if (userType === "aluno") {
      registerData.cpf = formData.cpf;
      registerData.rg = formData.rg;
      registerData.instituicao = formData.instituicao;
      registerData.curso = formData.curso;
    } else if (userType === "empresa") {
      registerData.cnpj = formData.cnpj;
    }

    try {
      const response = await fetch("http://localhost:8081/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(registerData),
      });

      if (response.ok) {
        alert("Cadastro realizado com sucesso!");
        onToggle(); // Voltar para a tela de login
      } else {
        const errorText = await response.text();
        alert(`Erro no cadastro: ${errorText}`);
      }
    } catch (error) {
      console.error("Erro ao cadastrar:", error);
      alert("Erro ao conectar com o servidor");
    } finally {
      setLoading(false);
    }
  };

  const renderUserFields = () => {
    switch (userType) {
      case "aluno":
        return (
          <>
            <input 
              type="text" 
              name="rg"
              placeholder="RG" 
              className="mb-4 p-2 rounded-md border border-gray-200"
              value={formData.rg}
              onChange={handleInputChange}
              required
            />
            <input 
              type="text" 
              name="endereco"
              placeholder="Endereço" 
              className="mb-4 p-2 rounded-md border border-gray-200"
              value={formData.endereco}
              onChange={handleInputChange}
            />
            <input 
              type="text" 
              name="instituicao"
              placeholder="Instituição de Ensino" 
              className="mb-4 p-2 rounded-md border border-gray-200"
              value={formData.instituicao}
              onChange={handleInputChange}
              required
            />
            <input 
              type="text" 
              name="curso"
              placeholder="Curso" 
              className="mb-6 p-2 rounded-md border border-gray-200"
              value={formData.curso}
              onChange={handleInputChange}
              required
            />
          </>
        );
      
      case "empresa":
        return (
          <>
            <input 
              type="text" 
              name="endereco"
              placeholder="Endereço da Empresa" 
              className="mb-6 p-2 rounded-md border border-gray-200"
              value={formData.endereco}
              onChange={handleInputChange}
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
        
        <form onSubmit={handleRegister} className="flex flex-col">
          <select 
            className="mb-6 p-2 rounded-md border border-gray-200"
            value={userType}
            onChange={handleUserTypeChange}
          >
            <option value="aluno">Aluno</option>
            <option value="professor" disabled>Professor</option>
            <option value="empresa">Empresa</option>
          </select>

          <input 
            type="text" 
            name="nome"
            placeholder="Nome completo" 
            className="mb-4 p-2 rounded-md border border-gray-200"
            value={formData.nome}
            onChange={handleInputChange}
            required
          />
          
          <input 
            type="text" 
            name={getFieldName()}
            placeholder={getPlaceholder()}
            className="mb-4 p-2 rounded-md border border-gray-200"
            value={userType === "empresa" ? formData.cnpj : formData.cpf}
            onChange={handleInputChange}
            required
          />
          
          <input 
            type="email" 
            name="email"
            placeholder="E-mail" 
            className="mb-4 p-2 rounded-md border border-gray-200"
            value={formData.email}
            onChange={handleInputChange}
            required
          />
          
          <input 
            type="password" 
            name="password"
            placeholder="Senha" 
            className="mb-4 p-2 rounded-md border border-gray-200"
            value={formData.password}
            onChange={handleInputChange}
            required
            minLength={6}
          />
          <input 
            type="password" 
            name="confirmPassword"
            placeholder="Confirmar Senha" 
            className="mb-4 p-2 rounded-md border border-gray-200"
            value={formData.confirmPassword}
            onChange={handleInputChange}
            required
            minLength={6}
          />

          {renderUserFields()}
        
          <button 
            type="submit"
            disabled={loading}
            className="bg-amber-400 hover:bg-amber-500 disabled:bg-amber-300 text-white font-bold py-3 px-8 rounded-full shadow-md transition"
          >
            {loading ? "Cadastrando..." : "Cadastrar"}
          </button>
          
          <p className="mt-2">
            Já tem uma conta?{" "}
            <button 
              type="button"
              onClick={onToggle} 
              className="text-purple-700 cursor-pointer hover:underline bg-transparent border-none"
            >
              Entre
            </button>
          </p>
        </form>
      </div>
    </div>
  );
}