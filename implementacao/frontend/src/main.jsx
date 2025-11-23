import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'
import WelcomePage from './pages/welcomePage/HomePage.jsx'
import Cadastro from './pages/cadastro/Cadastro.jsx'
import HomePage from './pages/homePage/homePage.jsx'
import Extrato from './pages/extrato/extratos.jsx'
import ListaAlunos from './pages/listaAlunos/ListAlunos.jsx'
import ListaVantagensAluno from './pages/listaVantagensAluno/ListaVantagensAluno.jsx'
import ListaVantagensEmpresas from './pages/listaVantagensEmpresas/ListaVantagensEmpresas.jsx'
import VantagensResgatadas from './pages/vantagensResgatadas/VantagensResgatadas.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/homePage" element={<HomePage />} />
        <Route path="/extrato" element={<Extrato />} />
        <Route path="/listaAlunos" element={<ListaAlunos />} />
        <Route path="/listaVantagensAluno" element={<ListaVantagensAluno />} />
        <Route path="/listaVantagensEmpresas" element={<ListaVantagensEmpresas />} />
        <Route path="/vantagensResgatadas" element={<VantagensResgatadas />} />

      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
