import React from "react";
import Header from "../welcomePage/coponentes/header.jsx";
import HomePageInfo from "./componentes/pageInfo.jsx";
import FunctionButtons from "./componentes/functionButtons.jsx";

export default function HomePage() {
  return (
    <div className="bg-purple-400">
      <Header />
      <HomePageInfo/>
      <FunctionButtons/>
    </div>
  );
}