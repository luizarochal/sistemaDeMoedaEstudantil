import React from "react";
import { useNavigate } from "react-router-dom";

export default function FunctionButtons() {
    const navigate = useNavigate();
    return (
    <div className=" mx-32 mt-10">
        <button className="bg-amber-500 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2" onClick={() => navigate('/listaAlunos')}>Premiar um aluno</button>
        <button className="bg-amber-500 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">.....</button>
        <button className="bg-amber-500 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">.....</button>
        <button className="bg-amber-500 hover:bg-amber-500 text-white font-bold py-3 px-8 rounded-full shadow-md transition mx-2 my-2">.....</button>

    </div>
  );
}
