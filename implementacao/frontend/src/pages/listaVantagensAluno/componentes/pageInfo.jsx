import { useNavigate } from "react-router-dom";

export default function PageInfo() {
  const navigate = useNavigate();

  
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



            <div className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                      <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 ">
                          <i className="fas fa-image text-5xl text-gray-500"></i>
                      </div>
                      <div>
                          <h2 className="text-2xl font-bold mb-2">Nome da Vantagem</h2>
                          <p className="text-gray-700 mb-1 line-clamp-3 w-80">Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem.</p>
                          <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: 100 Moedas</p>
                      </div>
            </div>

            <div className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                      <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 ">
                          <i className="fas fa-image text-5xl text-gray-500"></i>
                      </div>
                      <div>
                          <h2 className="text-2xl font-bold mb-2">Nome da Vantagem</h2>
                          <p className="text-gray-700 mb-1 line-clamp-3 w-80">Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem.</p>
                          <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: 100 Moedas</p>
                      </div>
            </div>

            <div className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                      <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 ">
                          <i className="fas fa-image text-5xl text-gray-500"></i>
                      </div>
                      <div>
                          <h2 className="text-2xl font-bold mb-2">Nome da Vantagem</h2>
                          <p className="text-gray-700 mb-1 line-clamp-3 w-80">Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem.</p>
                          <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: 100 Moedas</p>
                      </div>
            </div>

            <div className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                      <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 ">
                          <i className="fas fa-image text-5xl text-gray-500"></i>
                      </div>
                      <div>
                          <h2 className="text-2xl font-bold mb-2">Nome da Vantagem</h2>
                          <p className="text-gray-700 mb-1 line-clamp-3 w-80">Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem.</p>
                          <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: 100 Moedas</p>
                      </div>
            </div>

            <div className="cursor-pointer flex flex-row items-center p-10 m-5 gap-6 border-x-2 border-y-2 hover:transform hover:scale-105 hover:shadow-lg rounded-lg transition-shadow duration-300">
                      <div className="flex items-center justify-center !w-52 h-32 bg-gray-200 ">
                          <i className="fas fa-image text-5xl text-gray-500"></i>
                      </div>
                      <div>
                          <h2 className="text-2xl font-bold mb-2">Nome da Vantagem</h2>
                          <p className="text-gray-700 mb-1 line-clamp-3 w-80">Descrição detalhada da vantagem oferecida. Inclui todos os benefícios e informações relevantes que o aluno precisa saber antes de resgatar a vantagem.</p>
                          <p className="text-lg font-semibold bg-purple-500 w-fit px-2 rounded text-white">Custo: 100 Moedas</p>
                      </div>
            </div>



            
        </div>
      </div>
    </div>
  );
}