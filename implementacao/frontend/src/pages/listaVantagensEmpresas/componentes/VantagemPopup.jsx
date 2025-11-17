import { useState, useEffect } from "react";

export default function VantagemPopup({ isOpen, mode = 'create', initialData = null, onClose, onSave, onDelete }) {
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [custo, setCusto] = useState(0);
  const [cupom, setCupom] = useState('');
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState(null);
  const [imageError, setImageError] = useState(false);

  // Função para obter o token do localStorage
  const getToken = () => {
    return localStorage.getItem('authToken'); // ou onde você armazena o token
  };

  useEffect(() => {
    if (initialData) {
      setNome(initialData.nome || '');
      setDescricao(initialData.descricao || '');
      setCusto(initialData.custo || 0);
      setCupom(initialData.cupom || '');
      setImageFile(null);
      setImageError(false);
      
      // Se já existe uma imagem, criar preview a partir do endpoint
      if (initialData.idVantagem) {
        const token = getToken();
        const url = `http://localhost:8081/api/vantagens/${initialData.idVantagem}/imagem?t=${Date.now()}`;
        
        // Fazer a requisição com o token no header
        fetch(url, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        .then(response => {
          if (response.ok) {
            return response.blob();
          }
          throw new Error('Erro ao carregar imagem');
        })
        .then(blob => {
          const imageUrl = URL.createObjectURL(blob);
          setPreview(imageUrl);
        })
        .catch(error => {
          console.error('Erro ao carregar imagem:', error);
          setImageError(true);
        });
      } else {
        setPreview(null);
      }
    } else {
      setNome('');
      setDescricao('');
      setCusto(0);
      setCupom('');
      setImageFile(null);
      setPreview(null);
      setImageError(false);
    }
  }, [initialData, isOpen]);

  useEffect(() => {
    if (!imageFile) return;
    
    if (typeof imageFile === 'string') return; // já é uma URL
    
    const reader = new FileReader();
    reader.onload = () => {
      setPreview(reader.result);
      setImageError(false);
    };
    reader.readAsDataURL(imageFile);
  }, [imageFile]);

  // Cleanup para revogar a URL do blob
  useEffect(() => {
    return () => {
      if (preview && preview.startsWith('blob:')) {
        URL.revokeObjectURL(preview);
      }
    };
  }, [preview]);

  if (!isOpen) return null;

  function handleFileChange(e) {
    const file = e.target.files?.[0] ?? null;
    setImageFile(file);
    setImageError(false);
  }

  function handleSubmit(e) {
    e.preventDefault();

    const formData = new FormData();
    const token = getToken();

    // 1. Anexa os dados da vantagem como um Blob JSON
    const vantagemJson = {
      nome: nome,
      descricao: descricao,
      custo: Number.parseFloat(custo || 0),
      cupom: cupom
    };
    
    // Para edição, inclui o ID se disponível
    if (mode === 'edit' && initialData && initialData.idVantagem) {
      vantagemJson.idVantagem = initialData.idVantagem;
    }

    formData.append('vantagem', new Blob([JSON.stringify(vantagemJson)], { type: 'application/json' }));

    // 2. Anexa o arquivo da imagem, se for um novo arquivo
    if (imageFile && imageFile instanceof File) {
      formData.append('file', imageFile);
    }

    if (onSave) onSave(formData, token); // Passa o token para o onSave
  }

  function handleDelete() {
    if (onDelete) onDelete();
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose}></div>

      <div className="relative bg-white w-11/12 max-w-2xl rounded-lg shadow-lg p-6 z-10">
        <h2 className="text-2xl font-bold mb-4">
          {mode === 'create' ? 'Cadastrar Vantagem' : 'Editar Vantagem'}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block font-medium mb-1">Nome</label>
            <input 
              value={nome} 
              onChange={(e) => setNome(e.target.value)} 
              required 
              className="w-full border rounded px-3 py-2" 
            />
          </div>

          <div>
            <label className="block font-medium mb-1">Descrição</label>
            <textarea 
              value={descricao} 
              onChange={(e) => setDescricao(e.target.value)} 
              required 
              className="w-full border rounded px-3 py-2 h-24" 
            />
          </div>

          <div>
            <label className="block font-medium mb-1">Custo (Moedas)</label>
            <input 
              type="number" 
              value={custo} 
              onChange={(e) => setCusto(e.target.value)} 
              required 
              className="w-40 border rounded px-3 py-2" 
              min="0"
              step="0.01"
            />
          </div>

          <div>
            <label className="block font-medium mb-1">Cupom</label>
            <input 
              value={cupom} 
              onChange={(e) => setCupom(e.target.value)} 
              className="w-full border rounded px-3 py-2" 
              placeholder="Código do cupom (opcional)"
            />
          </div>

          <div>
            <label className="block font-medium mb-1">Imagem</label>
            <input 
              type="file" 
              accept="image/jpeg,image/png,image/webp" 
              onChange={handleFileChange} 
              className="w-full" 
            />
            <p className="text-sm text-gray-500 mt-1">
              Formatos suportados: JPEG, PNG, WebP (máx. 10MB)
            </p>
            
            {preview && !imageError ? (
              <div className="mt-3">
                <img 
                  src={preview} 
                  alt="Preview" 
                  className="w-40 h-24 object-cover rounded border"
                />
                <p className="text-xs text-gray-500 mt-1">
                  {imageFile instanceof File ? `Novo arquivo: ${imageFile.name}` : 'Imagem atual'}
                </p>
              </div>
            ) : (
              initialData && initialData.nomeArquivo && (
                <p className="text-sm text-gray-500 mt-2">
                  Imagem atual: {initialData.nomeArquivo}
                  {imageError && " (erro ao carregar)"}
                </p>
              )
            )}
          </div>

          <div className="flex justify-end gap-3">
            <button 
              type="button" 
              onClick={onClose} 
              className="px-4 py-2 rounded border hover:bg-gray-100"
            >
              Cancelar
            </button>

            {mode === 'create' ? (
              <button 
                type="submit" 
                className="px-4 py-2 rounded bg-purple-600 text-white hover:bg-purple-700"
              >
                Criar Vantagem
              </button>
            ) : (
              <>
                <button 
                  type="button" 
                  onClick={handleDelete} 
                  className="px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700"
                >
                  Excluir
                </button>
                <button 
                  type="submit" 
                  className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
                >
                  Salvar Alterações
                </button>
              </>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}