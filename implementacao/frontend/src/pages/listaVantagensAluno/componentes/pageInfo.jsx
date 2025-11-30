import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

export default function PageInfo({
  vantagens = [],
  loading = false,
  error = null,
  onRefresh,
}) {
  const navigate = useNavigate();
  const [imageUrls, setImageUrls] = useState({});
  const [resgatando, setResgatando] = useState({});
  const [alunoInfo, setAlunoInfo] = useState({
    id: null,
    nome: "",
    email: "",
    quantidadeMoedas: 0
  });

  // Buscar informações do aluno como no PageInfo de extrato
  useEffect(() => {
    loadUserInfo();
  }, []);

  async function loadUserInfo() {
    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      const email = localStorage.getItem("userEmail") || sessionStorage.getItem("userEmail");
      
      if (!token || !email) {
        throw new Error("Usuário não autenticado");
      }

      // Buscar informações completas do usuário
      const userResponse = await fetch(`http://localhost:8081/api/usuarios/email/${email}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      });

      if (!userResponse.ok) {
        throw new Error("Erro ao buscar informações do usuário");
      }

      const userData = await userResponse.json();
      
      setAlunoInfo({
        id: userData.id,
        nome: userData.nome || "Aluno",
        email: userData.email || "",
        quantidadeMoedas: userData.quantidadeMoedas || 0
      });

    } catch (err) {
      console.error("Erro ao carregar informações do usuário:", err);
    }
  }

  // Função para carregar imagem com token
  const loadImageWithToken = async (vantagemId) => {
    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      if (!token) {
        console.log("Token não encontrado");
        return null;
      }

      console.log(`Carregando imagem para vantagem ${vantagemId}`);
      const response = await fetch(
        `http://localhost:8081/api/vantagens/${vantagemId}/imagem?t=${Date.now()}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const blob = await response.blob();
        const imageUrl = URL.createObjectURL(blob);
        console.log(`Imagem carregada para vantagem ${vantagemId}`);
        return imageUrl;
      } else {
        console.log(
          `Erro ao carregar imagem ${vantagemId}: ${response.status}`
        );
        return null;
      }
    } catch (error) {
      console.error("Erro ao carregar imagem:", error);
      return null;
    }
  };

  // Função para criar transação de resgate
  const criarTransacaoResgate = async (vantagem, codigoCupom) => {
    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      if (!token) {
        console.error("Token não encontrado para criar transação");
        return false;
      }

      const transacaoData = {
        alunoId: alunoInfo.id,
        alunoNome: alunoInfo.nome,
        vantagemId: vantagem.idVantagem,
        vantagemNome: vantagem.nome,
        custoMoedas: vantagem.custo,
        codigoCupom: codigoCupom
      };

      console.log("Criando transação de resgate:", transacaoData);

      const response = await fetch("http://localhost:8081/api/transacoes-resgate", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(transacaoData),
      });

      if (response.ok) {
        console.log("Transação de resgate criada com sucesso");
        return true;
      } else {
        console.error("Erro ao criar transação de resgate:", response.status);
        return false;
      }
    } catch (error) {
      console.error("Erro ao criar transação de resgate:", error);
      return false;
    }
  };

  // Função para resgatar vantagem
  const handleResgatarVantagem = async (vantagem) => {
    if (!alunoInfo.id) {
      alert("Erro: ID do aluno não encontrado. Faça login novamente.");
      return;
    }

    if (resgatando[vantagem.idVantagem]) return;

    setResgatando(prev => ({ ...prev, [vantagem.idVantagem]: true }));

    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      if (!token) {
        alert("Usuário não autenticado");
        return;
      }

      // Verificar saldo antes de confirmar
      if (alunoInfo.quantidadeMoedas < vantagem.custo) {
        alert(`Saldo insuficiente! Você possui ${alunoInfo.quantidadeMoedas} moedas, mas a vantagem custa ${vantagem.custo} moedas.`);
        setResgatando(prev => ({ ...prev, [vantagem.idVantagem]: false }));
        return;
      }

      // Confirmar resgate
      const confirmar = window.confirm(
        `Deseja resgatar a vantagem "${vantagem.nome}" por ${vantagem.custo} moedas?\nSeu saldo atual: ${alunoInfo.quantidadeMoedas} moedas`
      );

      if (!confirmar) {
        setResgatando(prev => ({ ...prev, [vantagem.idVantagem]: false }));
        return;
      }

      const resgateRequest = {
        alunoId: alunoInfo.id,
        vantagemId: vantagem.idVantagem
      };

      console.log("Enviando requisição de resgate:", resgateRequest);

      // 1. Primeiro, fazer o resgate
      const response = await fetch("http://localhost:8081/api/resgates", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(resgateRequest),
      });

      if (response.ok) {
        const resgate = await response.json();
        
        // 2. Depois do resgate bem-sucedido, criar a transação
        const transacaoCriada = await criarTransacaoResgate(vantagem, resgate.codigoCupom);
        
        if (transacaoCriada) {
          alert(`✅ Vantagem resgatada com sucesso!\n🎫 Cupom: ${resgate.codigoCupom}\n📧 Verifique seu email para mais detalhes.`);
        } else {
          alert(`✅ Vantagem resgatada com sucesso!\n🎫 Cupom: ${resgate.codigoCupom}\n⚠️ Erro ao registrar transação no extrato.`);
        }
        
        // Atualizar saldo localmente
        setAlunoInfo(prev => ({
          ...prev,
          quantidadeMoedas: prev.quantidadeMoedas - vantagem.custo
        }));
        
        // Atualizar a lista de vantagens se necessário
        if (onRefresh) onRefresh();
      } else {
        const errorText = await response.text();
        console.error("Erro no resgate:", errorText);
        
        if (response.status === 402) {
          alert("Saldo insuficiente de moedas para resgatar esta vantagem.");
        } else if (response.status === 404) {
          alert("Vantagem ou aluno não encontrado.");
        } else {
          alert(`Erro ao resgatar vantagem: ${errorText}`);
        }
      }
    } catch (error) {
      console.error("Erro ao resgatar vantagem:", error);
      alert("Erro de conexão ao tentar resgatar a vantagem.");
    } finally {
      setResgatando(prev => ({ ...prev, [vantagem.idVantagem]: false }));
    }
  };

  // Carregar imagens quando as vantagens mudarem
  useEffect(() => {
    const loadImages = async () => {
      if (vantagens.length === 0) return;

      console.log(
        "Iniciando carregamento de imagens para",
        vantagens.length,
        "vantagens"
      );
      const newImageUrls = {};

      for (const vantagem of vantagens) {
        if (vantagem.idVantagem) {
          const imageUrl = await loadImageWithToken(vantagem.idVantagem);
          if (imageUrl) {
            newImageUrls[vantagem.idVantagem] = imageUrl;
          }
        }
      }

      if (Object.keys(newImageUrls).length > 0) {
        setImageUrls((prev) => ({ ...prev, ...newImageUrls }));
      }
    };

    loadImages();
  }, [vantagens]);

  // Cleanup function para revogar URLs
  useEffect(() => {
    const currentImageUrls = imageUrls;

    return () => {
      Object.values(currentImageUrls).forEach((url) => {
        if (url && url.startsWith("blob:")) {
          URL.revokeObjectURL(url);
        }
      });
    };
  }, [imageUrls]);

  const handleVantagemClick = (vantagem) => {
    console.log("Vantagem clicada:", vantagem);
  };

  if (loading) {
    return (
      <div className="w-full bg-white min-h-screen">
        <button
          type="button"
          className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition hover:border-b-purple-400"
          onClick={() => navigate("/homePage")}
        >
          <i className="fa-solid fa-angle-left mr-2"></i> Voltar
        </button>

        <div className="pt-20 px-5">
          <h3 className="text-4xl font-bold">Vantagens</h3>
          <div className="flex justify-center items-center h-64">
            <p className="text-xl text-gray-600">Carregando vantagens...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="w-full bg-white min-h-screen">
        <button
          type="button"
          className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition hover:border-b-purple-400"
          onClick={() => navigate("/homePage")}
        >
          <i className="fa-solid fa-angle-left mr-2"></i> Voltar
        </button>

        <div className="pt-20 px-5">
          <h3 className="text-4xl font-bold">Vantagens</h3>
          <div className="flex flex-col justify-center items-center h-64">
            <p className="text-xl text-red-600 mb-4">{error}</p>
            <button
              onClick={onRefresh}
              className="bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition"
            >
              Tentar Novamente
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full bg-white min-h-screen">
      <button
        type="button"
        className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition hover:border-b-purple-400"
        onClick={() => navigate("/homePage")}
      >
        <i className="fa-solid fa-angle-left mr-2"></i> Voltar
      </button>

      <div className="pt-20 px-5">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-4xl font-bold">Vantagens Disponíveis</h3>
          {alunoInfo.id && (
            <div className="bg-purple-500 text-white p-3 rounded-lg">
              <div className="flex items-center">
                <i className="fa-solid fa-coins text-yellow-400 mr-2"></i>
                <span className="font-semibold">Saldo: {alunoInfo.quantidadeMoedas} moedas</span>
              </div>
            </div>
          )}
        </div>

        <div className="flex flex-wrap gap-6 justify-center overflow-y-auto">
          {vantagens.length > 0 ? (
            vantagens.map((vantagem) => (
              <div
                key={vantagem.idVantagem}
                className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300"
                onClick={() => handleVantagemClick(vantagem)}
              >
                <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 rounded">
                  {imageUrls[vantagem.idVantagem] ? (
                    <img
                      src={imageUrls[vantagem.idVantagem]}
                      alt={vantagem.nome}
                      className="w-full h-full object-cover rounded"
                      onError={(e) => {
                        console.log(
                          `Erro ao exibir imagem ${vantagem.idVantagem}`
                        );
                        e.target.style.display = "none";
                      }}
                    />
                  ) : (
                    <div className="flex flex-col items-center justify-center">
                      <i className="fas fa-image text-5xl text-gray-500 mb-2"></i>
                      <span className="text-xs text-gray-500">
                        Carregando...
                      </span>
                    </div>
                  )}
                </div>
                <div className="min-w-0 flex-1">
                  <h2 className="text-2xl text-black font-bold mb-2">
                    {vantagem.nome}
                  </h2>
                  <p className="text-gray-700 mb-1 line-clamp-3 w-80">
                    {vantagem.descricao ||
                      "Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem."}
                  </p>
                  <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">
                    Custo: {vantagem.custo || 100} Moedas
                  </p>
                  {vantagem.empresaNome && (
                    <p className="text-sm text-gray-600 mt-1">
                      Oferecido por: {vantagem.empresaNome}
                    </p>
                  )}
                </div>
                <button 
                  className={`ml-auto self-center mt-auto px-3 py-2 rounded flex items-center transition ${
                    resgatando[vantagem.idVantagem] 
                      ? 'bg-gray-400 cursor-not-allowed' 
                      : alunoInfo.quantidadeMoedas < vantagem.custo
                      ? 'bg-gray-400 cursor-not-allowed'
                      : 'bg-yellow-500 hover:bg-yellow-600 text-white'
                  }`}
                  onClick={(e) => {
                    e.stopPropagation();
                    handleResgatarVantagem(vantagem);
                  }}
                  disabled={resgatando[vantagem.idVantagem] || alunoInfo.quantidadeMoedas < vantagem.custo}
                  title={alunoInfo.quantidadeMoedas < vantagem.custo ? "Saldo insuficiente" : "Resgatar vantagem"}
                >
                  {resgatando[vantagem.idVantagem] ? (
                    <>
                      <i className="fas fa-spinner fa-spin mr-2"></i>
                      Resgatando...
                    </>
                  ) : alunoInfo.quantidadeMoedas < vantagem.custo ? (
                    <>
                      <i className="fa-solid fa-lock mr-2"></i>
                      Saldo Insuficiente
                    </>
                  ) : (
                    <>
                      <i className="fa-solid fa-arrow-right-arrow-left mr-2"></i>
                      Resgatar
                    </>
                  )}
                </button>
              </div>
            ))
          ) : (
            <div className="flex justify-center items-center h-64">
              <p className="text-xl text-gray-600">
                Nenhuma vantagem disponível no momento.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}