import React from "react";
import Header from "../welcomePage/coponentes/header.jsx";
import HomePageInfo from "./componentes/pageInfo.jsx";

export default function HomePage() {
  return (
    <div className="bg-purple-400">
      <Header />
      <HomePageInfo/>
    </div>
  );
}