import { useState, useEffect } from "react";

export default function VantagemPopup({ isOpen, mode = 'create', initialData = null, onClose, onSave, onDelete }) {
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [preco, setPreco] = useState(0);
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState(null);

  useEffect(() => {
    if (initialData) {
      setNome(initialData.name || '');
      setDescricao(initialData.description || '');
      setPreco(initialData.price ?? 0);
      setImageFile(initialData.image || null);
      if (initialData.image && typeof initialData.image === 'string') setPreview(initialData.image);
    } else {
      setNome('');
      setDescricao('');
      setPreco(0);
      setImageFile(null);
      setPreview(null);
    }
  }, [initialData, isOpen]);

  useEffect(() => {
    if (!imageFile) return setPreview(null);
    if (typeof imageFile === 'string') return; // already a URL
    const reader = new FileReader();
    reader.onload = () => setPreview(reader.result);
    reader.readAsDataURL(imageFile);
  }, [imageFile]);

  if (!isOpen) return null;

  function handleFileChange(e) {
    const f = e.target.files?.[0] ?? null;
    setImageFile(f);
  }

  function handleSubmit(e) {
    e.preventDefault();

    // Cria um objeto FormData para enviar arquivos e dados JSON juntos.
    const formData = new FormData();

    // 1. Anexa os dados da vantagem como um Blob JSON.
    // O backend espera uma parte chamada "vantagem".
    const vantagemJson = {
      nome: nome,
      descricao: descricao,
      custo: Number.parseInt(preco || 0, 10),
    };
    formData.append('vantagem', new Blob([JSON.stringify(vantagemJson)], { type: 'application/json' }));

    // 2. Anexa o arquivo da imagem, se for um novo arquivo.
    // O backend espera uma parte chamada "file".
    if (imageFile && imageFile instanceof File) {
      formData.append('file', imageFile);
    }

    if (onSave) onSave(formData);
  }

  function handleDelete() {
    if (onDelete) onDelete(initialData || { name: nome });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose}></div>

      <div className="relative bg-white w-11/12 max-w-2xl rounded-lg shadow-lg p-6 z-10">
        <h2 className="text-2xl font-bold mb-4">{mode === 'create' ? 'Cadastrar Vantagem' : 'Editar Vantagem'}</h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block font-medium mb-1">Nome</label>
            <input value={nome} onChange={(e) => setNome(e.target.value)} required className="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label className="block font-medium mb-1">Descrição</label>
            <textarea value={descricao} onChange={(e) => setDescricao(e.target.value)} required className="w-full border rounded px-3 py-2 h-24" />
          </div>

          <div>
            <label className="block font-medium mb-1">Preço (Moedas)</label>
            <input type="number" value={preco} onChange={(e) => setPreco(e.target.value)} required className="w-40 border rounded px-3 py-2" />
          </div>

          <div>
            <label className="block font-medium mb-1">Imagem</label>
            <input type="file" accept="image/*" onChange={handleFileChange} className="w-full" />
            {preview ? (
              <img src={preview} alt="preview" className="mt-3 w-40 h-24 object-cover rounded" />
            ) : null}
          </div>

          <div className="flex justify-end gap-3">
            <button type="button" onClick={onClose} className="px-4 py-2 rounded border">Cancelar</button>

            {mode === 'create' ? (
              <button type="submit" className="px-4 py-2 rounded bg-purple-600 text-white">Criar Vantagem</button>
            ) : (
              <>
                <button type="button" onClick={handleDelete} className="px-4 py-2 rounded bg-red-600 text-white">Excluir</button>
                <button type="submit" className="px-4 py-2 rounded bg-green-600 text-white">Salvar Alterações</button>
              </>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}
