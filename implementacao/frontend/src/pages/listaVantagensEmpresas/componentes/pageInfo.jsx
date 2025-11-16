import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import VantagemPopup from "./VantagemPopup";
import axios from 'axios';

// URL base da sua API. É uma boa prática centralizar isso.
const API_URL = 'http://localhost:8081/api/vantagens';

export default function PageInfo() {
  const navigate = useNavigate();
  const [popupOpen, setPopupOpen] = useState(false);
  const [popupMode, setPopupMode] = useState('create');
  const [selectedVantagem, setSelectedVantagem] = useState(null);
  const [vantagens, setVantagens] = useState([]);

  // TODO: Obter o CNPJ da empresa logada (ex: de um contexto de autenticação)
  const empresaCnpj = '12345678000195'; // CNPJ de exemplo

  async function fetchVantagens() {
    try {
      // Usando o endpoint que busca por empresa, mas poderia ser o geral
      const response = await axios.get(`${API_URL}/empresa/${empresaCnpj}`);
      // O backend retorna um DTO com `idVantagem`, `nome`, `descricao`, `custo`, `foto`
      // Mapeamos para o formato que o frontend espera: `id`, `name`, `description`, `price`, `image`
      const mappedVantagens = response.data.map(v => ({
        id: v.idVantagem,
        name: v.nome,
        description: v.descricao,
        // Adiciona a URL base para que a imagem possa ser carregada
        image: v.foto ? `http://localhost:8081${v.foto}` : null,
        price: v.custo,
      }));
      setVantagens(mappedVantagens);
    } catch (error) {
      console.error("Erro ao buscar vantagens:", error);
      // TODO: Mostrar um feedback de erro para o usuário
    }
  }

  useEffect(() => {
    fetchVantagens();
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

  async function handleSave(formData) {
    // O formData já vem pronto do VantagemPopup

    try {
      const config = { headers: { 'Content-Type': 'multipart/form-data' } };

      if (popupMode === 'create') {
        // O endpoint de criação precisa do CNPJ da empresa
        await axios.post(`${API_URL}/empresa/${empresaCnpj}`, formData, config);
        console.log('Vantagem criada com sucesso!');
      } else {
        // O endpoint de atualização precisa do ID da vantagem
        await axios.put(`${API_URL}/${selectedVantagem.id}`, formData, config);
        console.log('Vantagem atualizada com sucesso!');
      }
      setPopupOpen(false);
      fetchVantagens(); // Recarrega a lista para mostrar a nova/alterada vantagem
    } catch (error) {
      console.error("Erro ao salvar vantagem:", error);
      // TODO: Mostrar um feedback de erro para o usuário
    }
  }

  async function handleDelete() {
    if (!selectedVantagem || !selectedVantagem.id) return;

    if (window.confirm(`Tem certeza que deseja excluir a vantagem "${selectedVantagem.name}"?`)) {
      try {
        await axios.delete(`${API_URL}/${selectedVantagem.id}`);
        console.log('Vantagem excluída com sucesso!');
        setPopupOpen(false);
        fetchVantagens(); // Recarrega a lista
      } catch (error) {
        console.error("Erro ao excluir vantagem:", error);
        // TODO: Mostrar um feedback de erro para o usuário
      }
    }
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
        <div className="flex justify-between">
          <h3 className="text-4xl font-bold">Vantagens</h3>
          <button onClick={openCreate} className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 hover:border-purple-400 transition">+ Cadastrar vantagem</button>
        </div>

        <div className="flex flex-wrap gap-6 justify-center overflow-y-auto mt-8">
          {vantagens.length > 0 ? (
            vantagens.map((vantagem) => (
              <div key={vantagem.id} onClick={() => openEdit(vantagem)} className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 rounded">
                  {vantagem.image ? (
                    <img src={vantagem.image} alt={vantagem.name} className="w-full h-full object-cover rounded" />
                  ) : (
                    <i className="fas fa-image text-5xl text-gray-500"></i>
                  )}
                </div>
                <div>
                  <h2 className="text-2xl font-bold text-black mb-2">{vantagem.name}</h2>
                  <p className="text-gray-700 mb-1 line-clamp-3 w-80">{vantagem.description}</p>
                  <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: {vantagem.price} Moedas</p>
                </div>
              </div>
            ))
          ) : (
            <p className="text-gray-500 mt-10">Nenhuma vantagem cadastrada para esta empresa.</p>
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