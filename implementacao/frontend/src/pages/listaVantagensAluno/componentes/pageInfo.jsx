import { useNavigate } from "react-router-dom";

export default function PageInfo({ vantagens = [], loading = false, error = null, onRefresh }) {
  const navigate = useNavigate();

  const handleVantagemClick = (vantagem) => {
    // Aqui você pode implementar a lógica para resgatar a vantagem
    console.log("Vantagem clicada:", vantagem);
    // navigate(`/vantagem/${vantagem.idVantagem}`); // Exemplo de navegação para detalhes
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
          onClick={() => navigate('/homePage')}
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
        onClick={() => navigate('/homePage')}
      >
        <i className="fa-solid fa-angle-left mr-2"></i> Voltar
      </button>

      <div className="pt-20 px-5">
        <h3 className="text-4xl font-bold">Vantagens</h3>

        <div className="flex flex-wrap gap-6 justify-center overflow-y-auto">
          {vantagens.length > 0 ? (
            vantagens.map((vantagem) => (
              <div 
                key={vantagem.idVantagem}
                className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300"
                onClick={() => handleVantagemClick(vantagem)}
              >
                <div className="flex items-center justify-center !w-52 h-32 bg-gray-200">
                  {vantagem.foto ? (
                    <img 
                      src={vantagem.foto} 
                      alt={vantagem.nome}
                      className="w-full h-full object-cover"
                    />
                  ) : (
                    <i className="fas fa-image text-5xl text-gray-500"></i>
                  )}
                </div>
                <div>
                  <h2 className="text-2xl font-bold mb-2">{vantagem.nome}</h2>
                  <p className="text-gray-700 mb-1 line-clamp-3 w-80">
                    {vantagem.descricao || "Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem."}
                  </p>
                  <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">
                    Custo: {vantagem.custo || 100} Moedas
                  </p>
                  {/* Adicione esta linha se quiser mostrar o nome da empresa */}
                  {vantagem.empresaNome && (
                    <p className="text-sm text-gray-600 mt-1">
                      Oferecido por: {vantagem.empresaNome}
                    </p>
                  )}
                </div>
              </div>
            ))
          ) : (
            <div className="flex justify-center items-center h-64">
              <p className="text-xl text-gray-600">Nenhuma vantagem disponível no momento.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}