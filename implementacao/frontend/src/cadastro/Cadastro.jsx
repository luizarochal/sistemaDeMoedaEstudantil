import React from "react";
import Header from "./coponentes/header.jsx";
import Cadastro from "./coponentes/pageinfo.jsx";

export default function HomePage() {
  return (
    <div className="bg-purple-400">
      <Header />
      <Cadastro />
    </div>
  );
}