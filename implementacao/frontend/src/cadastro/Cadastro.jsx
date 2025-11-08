import React from "react";
import Header from "../homePage/coponentes/header.jsx";
import Cadastro from "./componentes/PageinfoCadastro.jsx";
import Entrar from "./componentes/PageinfoEntrar.jsx";

export default function CadastroPage() {
  const [pageInfoType, setPageInfoType] = React.useState("Entrar");
  return (
    <div className="bg-purple-400">
      <Header />
      
      {pageInfoType === "Entrar" ? 
      <Entrar onToggle={() => setPageInfoType("cadastro")} /> :
      <Cadastro onToggle={() => setPageInfoType("Entrar")} />
      }

    </div>
  );
}