import React from "react";
import Header from "../welcomePage/coponentes/header.jsx";
import PageInfo from "./componentes/PageInfo.jsx";

export default function HomePage() {
  return (
    <div className="bg-purple-400">
      <Header/>
      <PageInfo/>
    </div>
  );
}
