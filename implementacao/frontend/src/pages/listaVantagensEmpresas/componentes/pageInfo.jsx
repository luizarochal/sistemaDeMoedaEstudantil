import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import VantagemPopup from "./VantagemPopup";

export default function PageInfo({ vantagens = [], loading = false, error = null, onRefresh, empresaInfo }) {
  const navigate = useNavigate();
  const [popupOpen, setPopupOpen] = useState(false);
  const [popupMode, setPopupMode] = useState('create');
  const [selectedVantagem, setSelectedVantagem] = useState(null);
  const [imageUrls, setImageUrls] = useState({});

  // Função para carregar imagem com token
  const loadImageWithToken = async (vantagemId) => {
    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        console.log("Token não encontrado");
        return null;
      }

      console.log(`Carregando imagem para vantagem ${vantagemId}`);
      const response = await fetch(`http://localhost:8081/api/vantagens/${vantagemId}/imagem?t=${Date.now()}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        const blob = await response.blob();
        const imageUrl = URL.createObjectURL(blob);
        console.log(`Imagem carregada para vantagem ${vantagemId}`);
        return imageUrl;
      } else {
        console.log(`Erro ao carregar imagem ${vantagemId}: ${response.status}`);
        return null;
      }
    } catch (error) {
      console.error('Erro ao carregar imagem:', error);
      return null;
    }
  };

  // Carregar imagens quando as vantagens mudarem
  useEffect(() => {
    const loadImages = async () => {
      if (vantagens.length === 0) return;

      console.log("Iniciando carregamento de imagens para", vantagens.length, "vantagens");
      const newImageUrls = {};
      
      for (const vantagem of vantagens) {
        if (vantagem.idVantagem && !imageUrls[vantagem.idVantagem]) {
          const imageUrl = await loadImageWithToken(vantagem.idVantagem);
          if (imageUrl) {
            newImageUrls[vantagem.idVantagem] = imageUrl;
          }
        }
      }
      
      if (Object.keys(newImageUrls).length > 0) {
        setImageUrls(prev => ({ ...prev, ...newImageUrls }));
      }
    };

    loadImages();
  }, [vantagens]); // Executa quando vantagens mudam

  // Cleanup function para revogar URLs
  useEffect(() => {
    return () => {
      Object.values(imageUrls).forEach(url => {
        if (url && url.startsWith('blob:')) {
          URL.revokeObjectURL(url);
        }
      });
    };
  }, []);

  function openCreate() {
    setPopupMode('create');
    setSelectedVantagem(null);
    setPopupOpen(true);
  }

  function openEdit(vantagem) {
    setPopupMode('edit');
    setSelectedVantagem(vantagem);
    setPopupOpen(true);
  }

  const handleSave = async (formData) => {
    try {
      const token = localStorage.getItem("authToken");
      
      if (!token || !empresaInfo.cnpj) {
        console.error("Token ou CNPJ não disponível");
        return;
      }

      if (popupMode === 'create') {
        const response = await fetch(`http://localhost:8081/api/vantagens/empresa/${empresaInfo.cnpj}`, {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`,
          },
          body: formData
        });

        if (!response.ok) {
          throw new Error(`Erro ${response.status} ao criar vantagem`);
        }

        console.log('Vantagem criada com sucesso!');
      } else {
        const response = await fetch(`http://localhost:8081/api/vantagens/${selectedVantagem.idVantagem}`, {
          method: "PUT",
          headers: {
            "Authorization": `Bearer ${token}`,
          },
          body: formData
        });

        if (!response.ok) {
          throw new Error(`Erro ${response.status} ao atualizar vantagem`);
        }

        console.log('Vantagem atualizada com sucesso!');
      }
      
      setPopupOpen(false);
      onRefresh(); // Recarrega a lista
    } catch (error) {
      console.error("Erro ao salvar vantagem:", error);
      alert("Erro ao salvar vantagem: " + error.message);
    }
  };

  const handleDelete = async () => {
    if (!selectedVantagem || !selectedVantagem.idVantagem) return;

    if (window.confirm(`Tem certeza que deseja excluir a vantagem "${selectedVantagem.nome}"?`)) {
      try {
        const token = localStorage.getItem("authToken");
        
        const response = await fetch(`http://localhost:8081/api/vantagens/${selectedVantagem.idVantagem}`, {
          method: "DELETE",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        });

        if (!response.ok) {
          throw new Error(`Erro ${response.status} ao excluir vantagem`);
        }

        console.log('Vantagem excluída com sucesso!');
        setPopupOpen(false);
        onRefresh(); // Recarrega a lista
      } catch (error) {
        console.error("Erro ao excluir vantagem:", error);
        alert("Erro ao excluir vantagem: " + error.message);
      }
    }
  };

  if (loading) {
    return (
      <div className="w-full bg-white min-h-screen">
        <button 
          type="button" 
          className="absolute mt-5 ml-3 justify-start bg-purple-600 text-white hover:bg-purple-700 px-4 py-2 rounded-lg transition hover:border-b-purple-400"
          onClick={() => navigate('/homePage')}
        >
          <i className="fa-solid fa-angle-left mr-2"></i> Voltar
        </button>

        <div className="pt-20 px-5">
          <h3 className="text-4xl font-bold">Vantagens da Empresa</h3>
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
          onClick={() => navigate('/homePage')}
        >
          <i className="fa-solid fa-angle-left mr-2"></i> Voltar
        </button>

        <div className="pt-20 px-5">
          <h3 className="text-4xl font-bold">Vantagens da Empresa</h3>
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
        onClick={() => navigate('/homePage')}
      >
        <i className="fa-solid fa-angle-left mr-2"></i> Voltar
      </button>

      <div className="pt-20 px-5">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h3 className="text-4xl font-bold">Vantagens da Empresa</h3>
            {empresaInfo.nome && (
              <p className="text-lg text-gray-600 mt-2">Empresa: {empresaInfo.nome}</p>
            )}
          </div>
          <button 
            onClick={openCreate} 
            className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition"
          >
            + Cadastrar vantagem
          </button>
        </div>

        <div className="flex flex-wrap gap-6 justify-center overflow-y-auto">
          {vantagens.length > 0 ? (
            vantagens.map((vantagem) => (
              <div 
                key={vantagem.idVantagem}
                onClick={() => openEdit(vantagem)} 
                className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300"
              >
                <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 rounded">
                  {imageUrls[vantagem.idVantagem] ? (
                    <img 
                      src={imageUrls[vantagem.idVantagem]} 
                      alt={vantagem.nome}
                      className="w-full h-full object-cover rounded"
                      onError={(e) => {
                        console.log(`Erro ao exibir imagem ${vantagem.idVantagem}`);
                        e.target.style.display = 'none';
                      }}
                    />
                  ) : (
                    <div className="flex flex-col items-center justify-center">
                      <i className="fas fa-image text-5xl text-gray-500 mb-2"></i>
                      <span className="text-xs text-gray-500">Carregando...</span>
                    </div>
                  )}
                </div>
                <div>
                  <h2 className="text-2xl font-bold text-black mb-2">{vantagem.nome}</h2>
                  <p className="text-gray-700 mb-1 line-clamp-3 w-80">{vantagem.descricao}</p>
                  <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">
                    Custo: {vantagem.custo} Moedas
                  </p>
                  {vantagem.cupom && (
                    <p className="text-sm text-gray-600 mt-1">Cupom: {vantagem.cupom}</p>
                  )}
                </div>
              </div>
            ))
          ) : (
            <div className="flex justify-center items-center h-64 w-full">
              <p className="text-gray-500 text-xl">Nenhuma vantagem cadastrada para esta empresa.</p>
            </div>
          )}
        </div>
      </div>

      <VantagemPopup
        isOpen={popupOpen}
        mode={popupMode}
        initialData={selectedVantagem}
        onClose={() => setPopupOpen(false)}
        onSave={handleSave}
        onDelete={handleDelete}
      />
    </div>
  );
}