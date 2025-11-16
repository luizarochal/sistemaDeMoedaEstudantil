import React from "react";

export default function Header() {
  const logado = localStorage.getItem("authToken");
  const clickSair = () =>{
    localStorage.clear()
    window.location.reload();
  }
  
  return (
    <header className="bg-yellow-400 text-purple-700 py-4 px-8 shadow-md flex justify-between items-center">
      <div className="text-2xl font-bold flex items-center gap-2">
       Sistema de Moeda Estudantil
      </div>
      <nav className="flex gap-4">
        <a
          href="#"
          className="bg-white/20 px-4 py-2 rounded-full hover:bg-white/30 transition"
        >
          Início
        </a>
        <a
          href="#"
          className="bg-white/20 px-4 py-2 rounded-full hover:bg-white/30 transition"
        >
          Sobre
        </a>
        <a
          href="#"
          className="bg-white/20 px-4 py-2 rounded-full hover:bg-white/30 transition"
        >
          Contato
        </a>

        {logado &&(
          <button onClick={() =>clickSair()}>Sair</button>
        )}
        
      </nav>

    </header>
  );
}
