import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

export default function PageInfoResgatadas({
  resgates = [],
  loading = false,
  error = null,
  onRefresh,
  alunoInfo = {}
}) {
  const navigate = useNavigate();
  const [imageUrls, setImageUrls] = useState({});

  // Função para carregar imagem com token
  const loadImageWithToken = async (vantagemId) => {
    try {
      const token = localStorage.getItem("authToken") || sessionStorage.getItem("authToken");
      if (!token) {
        console.log("Token não encontrado");
        return null;
      }

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
        return imageUrl;
      } else {
        console.log(`Erro ao carregar imagem ${vantagemId}: ${response.status}`);
        return null;
      }
    } catch (error) {
      console.error("Erro ao carregar imagem:", error);
      return null;
    }
  };

  // Carregar imagens quando os resgates mudarem
  useEffect(() => {
    const loadImages = async () => {
      if (resgates.length === 0) return;

      const newImageUrls = {};

      for (const resgate of resgates) {
        if (resgate.vantagemId) {
          const imageUrl = await loadImageWithToken(resgate.vantagemId);
          if (imageUrl) {
            newImageUrls[resgate.vantagemId] = imageUrl;
          }
        }
      }

      if (Object.keys(newImageUrls).length > 0) {
        setImageUrls((prev) => ({ ...prev, ...newImageUrls }));
      }
    };

    loadImages();
  }, [resgates]);

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

  const getStatusCupom = (resgate) => {
    if (resgate.utilizado) {
      return { text: "Utilizado", color: "bg-green-500", icon: "fa-check" };
    } else {
      return { text: "Disponível", color: "bg-blue-500", icon: "fa-ticket" };
    }
  };

  function formatDate(dateString) {
    try {
      if (!dateString) return "Data não disponível";
      
      const date = new Date(dateString);
      if (isNaN(date.getTime())) return dateString;
      
      return date.toLocaleDateString("pt-BR", { 
        day: "2-digit", 
        month: "2-digit", 
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
      });
    } catch {
      return dateString || "Data não disponível";
    }
  }

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
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-4xl font-bold">Vantagens Resgatadas</h3>
            {alunoInfo.id && (
              <div className="bg-purple-500 text-white p-3 rounded-lg">
                <div className="flex items-center">
                  <i className="fa-solid fa-coins text-yellow-400 mr-2"></i>
                  <span className="font-semibold">Saldo: {alunoInfo.quantidadeMoedas} moedas</span>
                </div>
              </div>
            )}
          </div>
          <div className="flex justify-center items-center h-64">
            <p className="text-xl text-gray-600">Carregando vantagens resgatadas...</p>
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
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-4xl font-bold">Vantagens Resgatadas</h3>
            {alunoInfo.id && (
              <div className="bg-purple-500 text-white p-3 rounded-lg">
                <div className="flex items-center">
                  <i className="fa-solid fa-coins text-yellow-400 mr-2"></i>
                  <span className="font-semibold">Saldo: {alunoInfo.quantidadeMoedas} moedas</span>
                </div>
              </div>
            )}
          </div>
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
          <h3 className="text-4xl font-bold">Vantagens Resgatadas</h3>
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
          {resgates.length > 0 ? (
            resgates.map((resgate) => {
              const status = getStatusCupom(resgate);
              
              return (
                <div
                  key={resgate.id}
                  className="flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 rounded-lg transition-shadow duration-300 bg-gray-50 w-full max-w-4xl"
                >
                  <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 rounded">
                    {resgate.vantagemId && imageUrls[resgate.vantagemId] ? (
                      <img
                        src={imageUrls[resgate.vantagemId]}
                        alt={resgate.vantagemNome}
                        className="w-full h-full object-cover rounded"
                        onError={(e) => {
                          e.target.style.display = "none";
                        }}
                      />
                    ) : (
                      <div className="flex flex-col items-center justify-center">
                        <i className="fas fa-image text-5xl text-gray-500 mb-2"></i>
                        <span className="text-xs text-gray-500">
                          Sem imagem
                        </span>
                      </div>
                    )}
                  </div>
                  <div className="min-w-0 flex-1">
                    <h2 className="text-2xl text-black font-bold mb-2">
                      {resgate.vantagemNome || "Vantagem não encontrada"}
                    </h2>
                    <p className="text-gray-700 mb-1 line-clamp-3 w-80">
                      {resgate.vantagemDescricao || "Descrição indisponível"}
                    </p>
                    <div className="flex flex-wrap gap-2 mt-2">
                      <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">
                        <i className="fa-solid fa-ticket mr-1"></i>
                        Cupom: {resgate.codigoCupom}
                      </p>
                      <p className={`text-lg font-semibold ${status.color} w-fit px-2 rounded text-white`}>
                        <i className={`fa-solid ${status.icon} mr-1`}></i>
                        {status.text}
                      </p>
                    </div>
                    <p className="text-sm text-gray-600 mt-1">
                      <i className="fa-solid fa-calendar mr-1"></i>
                      Resgatado em: {formatDate(resgate.dataResgate)}
                    </p>
                    {resgate.vantagemCusto && (
                      <p className="text-sm text-gray-600 mt-1">
                        <i className="fa-solid fa-coins mr-1"></i>
                        Custo: {resgate.vantagemCusto} moedas
                      </p>
                    )}
                    {resgate.empresaNome && (
                      <p className="text-sm text-gray-600 mt-1">
                        <i className="fa-solid fa-building mr-1"></i>
                        Oferecido por: {resgate.empresaNome}
                      </p>
                    )}
                  </div>
                </div>
              );
            })
          ) : (
            <div className="flex justify-center items-center h-64">
              <p className="text-xl text-gray-600">
                Nenhuma vantagem resgatada até o momento.
              </p>
            </div>
          )}
        </div>

        {/* Botão para recarregar */}
        {!loading && (
          <div className="text-center mt-6">
            <button 
              onClick={onRefresh}
              className="bg-purple-600 hover:bg-purple-700 text-white px-6 py-2 rounded-lg transition flex items-center mx-auto"
            >
              <i className="fa-solid fa-rotate mr-2"></i>
              Atualizar Lista
            </button>
          </div>
        )}
      </div>
    </div>
  );
}