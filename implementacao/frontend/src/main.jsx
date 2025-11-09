import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'
import WelcomePage from './pages/welcomePage/HomePage.jsx'
import Cadastro from './pages/cadastro/Cadastro.jsx'
import HomePage from './pages/homePage/homePage.jsx'
import Extrato from './pages/extrato/extratos.jsx'
import ListaAlunos from './pages/listaAlunos/ListAlunos.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/homePage" element={<HomePage />} />
        <Route path="/extrato" element={<Extrato />} />
        <Route path="/listaAlunos" element={<ListaAlunos />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
