import React from "react";

export default function PageinfoEntrar({ onToggle }) {
  const [formData, setFormData] = React.useState({
    email: "",
    password: ""
  });
  const [loading, setLoading] = React.useState(false);


  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleLogin = async (event) => {
    event.preventDefault();
    
    // Validação básica
    if (!formData.email || !formData.password) {
      alert("Por favor, preencha todos os campos");
      return;
    }

    setLoading(true);

    try {
      console.log("Tentando login com:", formData.email);
      
      const response = await fetch("http://localhost:8081/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: 'include', // Importante para CORS com credenciais
        body: JSON.stringify({
          email: formData.email,
          password: formData.password
        }),
      });

      console.log("Status da resposta:", response.status);
      
      if (response.ok) {
        const data = await response.json();
        console.log("Resposta do login:", data);
        
        // Salvar token e informações do usuário no localStorage
        localStorage.setItem("authToken", data.token);
        localStorage.setItem("userName", data.nome);
        localStorage.setItem("userEmail", data.email);
        localStorage.setItem("userRole", data.role);
        
        console.log("Login realizado com sucesso!");
        alert("Login realizado com sucesso!");
        
        // Redirecionar para dashboard
        setTimeout(() => {
          window.location.href = "/homePage";
        }, 1000);
        
      } else {
        const errorText = await response.text();
        console.error("Erro na resposta:", errorText);
        alert(`Erro no login: ${errorText || "Credenciais inválidas"}`);
      }
    } catch (error) {
      console.error("Erro completo:", error);
      alert(`Erro de conexão: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center">
      <div className="bg-white rounded-3xl shadow-xl text-center p-10 w-11/12 max-w-2xl mt-10">
        <h1 className="text-2xl font-bold text-purple-700 mb-4">
          Entrar na Plataforma
        </h1>
        
        <form onSubmit={handleLogin} className="flex flex-col">
          

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
          />
        
          <button 
            type="submit"
            disabled={loading}
            className="bg-amber-400 hover:bg-amber-500 disabled:bg-amber-300 text-white font-bold py-3 px-8 rounded-full shadow-md transition"
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>
          
          <p className="mt-2">
            Ainda não tem uma conta?{" "}
            <button 
              type="button"
              onClick={onToggle} 
              className="text-purple-700 cursor-pointer hover:underline bg-transparent border-none"
            >
              Cadastre-se
            </button>
          </p>
        </form>
      </div>
    </div>
  );
}