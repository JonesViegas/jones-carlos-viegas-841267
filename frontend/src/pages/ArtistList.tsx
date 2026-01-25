import { useEffect, useState } from 'react';
import api from '../service/api';

// Interfaces para tipagem sênior
interface Album { id: number; title: string; coverUrl: string; presignedUrl?: string; }
interface Artist { id: number; name: string; genre: string; regionalId: number; albums: Album[]; }
interface Regional { id: number; nome: string; ativo: boolean; }

export function ArtistList() {
  const [artists, setArtists] = useState<Artist[]>([]);
  const [regionais, setRegionais] = useState<Regional[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedRegId, setSelectedRegId] = useState<number | null>(null);
  
  const [name, setName] = useState('');
  const [genre, setGenre] = useState('');
  const [albumTitle, setAlbumTitle] = useState('');
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  
  const [showAlbumModal, setShowAlbumModal] = useState(false);
  const [activeArtist, setActiveArtist] = useState<Artist | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    try {
      const regRes = await api.get('/regionais');
      setRegionais(regRes.data || []);

      const artRes = await api.get('/artists');
      const artistsData: Artist[] = artRes.data || [];
      
      for (let artist of artistsData) {
        if (artist.albums) {
          for (let album of artist.albums) {
            try {
              if (album.coverUrl) {
                const fileNameEncoded = encodeURIComponent(album.coverUrl);
                const urlRes = await api.get(`/albums/${album.id}/cover-url?fileName=${fileNameEncoded}`);
                album.presignedUrl = urlRes.data.url;
              }
            } catch (e) { console.warn("Erro ao carregar link S3", e); }
          }
        }
      }
      setArtists(artistsData);
    } catch (err: any) {
      if (err.response?.status === 403) logout();
    }
  };

  const logout = () => { localStorage.clear(); window.location.href = '/'; };
  useEffect(() => { fetchData(); }, []);

  const handleCreateArtist = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRegId) return alert("Selecione uma Unidade Regional no menu à esquerda!");
    setLoading(true);
    try {
      await api.post('/artists', { name, genre, regionalId: selectedRegId });
      setName(''); setGenre(''); fetchData();
      alert("Artista cadastrado com sucesso!");
    } finally { setLoading(false); }
  };

  const handleUploadAlbum = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedFile || !activeArtist) return;
    setLoading(true);
    const formData = new FormData();
    formData.append('title', albumTitle);
    formData.append('artistId', activeArtist.id.toString());
    formData.append('cover', selectedFile);

    try {
      await api.post('/albums', formData);
      setShowAlbumModal(false); setAlbumTitle(''); setSelectedFile(null);
      fetchData();
      alert("Capa enviada ao MinIO S3 com sucesso!");
    } catch (err) { alert("Erro no upload"); }
    finally { setLoading(false); }
  };

  const filteredArtists = artists.filter(a => {
    const mSearch = a.name?.toLowerCase().includes(searchTerm.toLowerCase());
    const mReg = selectedRegId ? a.regionalId === selectedRegId : true;
    return mSearch && mReg;
  });

  return (
    <div className="min-h-screen bg-gray-50 flex w-full notranslate" key="main-app">
      
      {/* SIDEBAR (Barra Lateral Azul) */}
      <aside className="w-80 bg-blue-900 text-white p-6 shadow-2xl hidden md:flex flex-col sticky top-0 h-screen">
        <img src="/seplag-logo.svg" alt="SEPLAG" className="h-14 mx-auto mb-10" />
        <h2 className="text-[12px] font-black text-blue-300 uppercase mb-4 tracking-widest text-center">SISTEMA DE GESTÃO DE ARTISTAS</h2>
        <ul className="space-y-1 flex-1 overflow-y-auto custom-scrollbar pr-2">
          <li 
            onClick={() => setSelectedRegId(null)} 
            className={`cursor-pointer p-4 rounded-2xl transition-all ${!selectedRegId ? 'bg-blue-600 font-bold shadow-lg' : 'hover:bg-blue-800 text-blue-100'}`}
          >
            🌎 Visão Regional (Todos)
          </li>
          
          {/* LÓGICA SÊNIOR: Filtrando apenas regionais de cidades reais e limpando o nome */}
          {regionais
            .filter(r => r.nome.startsWith('REGIONAL DE')) 
            .map(reg => (
            <li 
              key={`reg-${reg.id}`} 
              onClick={() => setSelectedRegId(reg.id)} 
              className={`cursor-pointer p-3 rounded-xl text-sm transition-all ${selectedRegId === reg.id ? 'bg-blue-600 font-bold shadow-lg' : 'hover:bg-blue-800'}`}
            >
              • {reg.nome.replace('REGIONAL DE ', '')}
            </li>
          ))}
        </ul>
        <button onClick={logout} className="mt-6 w-full p-4 bg-red-600 hover:bg-red-700 rounded-2xl font-black transition shadow-lg active:scale-95">SAIR DO SISTEMA</button>
      </aside>

      {/* MAIN CONTENT */}
      <main className="flex-1 p-6 md:p-12 overflow-y-auto">
        <div className="max-w-6xl mx-auto">
          
          {/* HEADER */}
          <div className="flex flex-col md:flex-row justify-between items-center mb-10 gap-6">
            <div>
              <h1 className="text-4xl font-black text-gray-800 uppercase tracking-tighter">
                {selectedRegId ? <span>{regionais.find(r => r.id === selectedRegId)?.nome}</span> : <span>Gestão de Artistas</span>}
              </h1>
              <p className="text-gray-400 font-medium italic">SEPLAG MT</p>
            </div>
            <div className="relative w-full md:w-96">
              <input 
                type="text" placeholder="🔍 Buscar artista por nome..." 
                className="w-full border-2 border-gray-100 p-4 pl-12 rounded-2xl shadow-sm focus:border-blue-500 outline-none transition-all bg-white"
                value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          {/* FORMULÁRIO DE CADASTRO COM CONTEXTO */}
          <section className="bg-white p-8 rounded-[40px] shadow-sm border-2 border-blue-50 mb-12">
            <h2 className="text-xs font-black text-blue-600 uppercase mb-6 tracking-widest flex items-center gap-2">
              <span className="w-2 h-2 bg-blue-600 rounded-full animate-pulse"></span>
              {selectedRegId 
                ? `Novo Artista p/ ${regionais.find(r => r.id === selectedRegId)?.nome}` 
                : "Selecione uma Regional ao lado para habilitar cadastro"}
            </h2>
            <form onSubmit={handleCreateArtist} className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <input type="text" placeholder="Nome do Artista" className="bg-gray-50 border-none p-5 rounded-2xl outline-none focus:ring-2 focus:ring-blue-500" value={name} onChange={e => setName(e.target.value)} required disabled={!selectedRegId} />
              <input type="text" placeholder="Gênero Musical" className="bg-gray-50 border-none p-5 rounded-2xl outline-none focus:ring-2 focus:ring-blue-500" value={genre} onChange={e => setGenre(e.target.value)} required disabled={!selectedRegId} />
              <button 
                type="submit" 
                disabled={loading || !selectedRegId} 
                className="bg-blue-600 text-white p-4 rounded-2xl font-black hover:bg-blue-700 shadow-xl shadow-blue-100 transition-all disabled:bg-gray-200"
              >
                {loading ? 'SALVANDO...' : '+ CADASTRAR'}
              </button>
            </form>
          </section>

          {/* GRID DE ARTISTAS COM IMAGENS EM DESTAQUE */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
            {filteredArtists.map(artist => (
              <div key={artist.id} className="bg-white p-10 rounded-[50px] shadow-md border border-gray-100 flex flex-col hover:shadow-2xl transition-all group">
                <div className="flex justify-between items-start mb-6">
                  <span className="bg-green-50 text-green-700 text-[10px] font-black px-4 py-2 rounded-full uppercase border border-green-100 tracking-widest">Cadastro Ativo</span>
                  <span className="text-gray-200 font-mono text-4xl italic">#{artist.id}</span>
                </div>
                
                <h3 className="text-3xl font-black text-gray-800 mb-1 group-hover:text-blue-600 transition-colors uppercase"><span>{artist.name}</span></h3>
                <p className="text-blue-400 font-black mb-10 text-xs tracking-widest uppercase"><span>{artist.genre}</span></p>
                
                {/* LISTA DE ÁLBUNS COM IMAGENS GRANDES */}
                <div className="space-y-8 mb-10 flex-1">
                  <h4 className="text-[10px] font-black text-gray-300 uppercase tracking-[0.4em] text-center">Álbuns no Storage S3</h4>
                  {artist.albums?.map(album => (
                    <div key={album.id} className="bg-gray-50 p-6 rounded-[40px] border border-gray-100 flex flex-col items-center gap-6 group/item">
                      {/* CONTAINER DA IMAGEM GRANDE */}
                      <div className="w-full h-56 rounded-[32px] overflow-hidden shadow-2xl border-4 border-white bg-gray-200">
                        <img 
                          src={album.presignedUrl || 'https://via.placeholder.com/400'} 
                          alt="Capa" 
                          className="w-full h-full object-cover group-hover/item:scale-110 transition-transform duration-700" 
                        />
                      </div>
                      <span className="text-lg font-black text-gray-700 uppercase italic tracking-tighter"><span>{album.title}</span></span>
                    </div>
                  ))}
                  {(!artist.albums || artist.albums.length === 0) && (
                    <div className="text-center py-10 bg-gray-50 rounded-3xl border-2 border-dashed border-gray-200">
                      <p className="text-xs text-gray-300 font-bold uppercase tracking-widest leading-loose">Aguardando envio de<br/>capa para o MinIO</p>
                    </div>
                  )}
                </div>

                <button 
                  onClick={() => { setActiveArtist(artist); setShowAlbumModal(true); }}
                  className="w-full py-5 bg-blue-600 text-white rounded-[24px] font-black hover:bg-blue-700 transition-all shadow-xl shadow-blue-50 uppercase tracking-widest"
                >
                  + Novo Álbum (Upload S3)
                </button>
              </div>
            ))}
          </div>
        </div>
      </main>

      {/* MODAL DE UPLOAD S3 */}
      {showAlbumModal && (
        <div className="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50 p-4 backdrop-blur-md">
          <div className="bg-white p-12 rounded-[60px] w-full max-w-lg shadow-2xl border-b-8 border-blue-600">
            <h2 className="text-3xl font-black mb-1 text-gray-800 tracking-tighter uppercase">Upload S3 Storage</h2>
            <p className="text-sm text-gray-400 mb-10 font-medium">Vinculando a: <span className="text-blue-600 font-black">{activeArtist?.name}</span></p>
            <form onSubmit={handleUploadAlbum} className="space-y-8">
              <input type="text" placeholder="Título do Disco" className="w-full border-2 border-gray-100 p-5 rounded-3xl outline-none focus:border-blue-500 transition-all bg-gray-50" value={albumTitle} onChange={e => setAlbumTitle(e.target.value)} required />
              <div className="bg-gray-50 p-8 rounded-3xl border-2 border-dashed border-gray-200 text-center">
                <input type="file" accept="image/*" className="w-full text-xs text-gray-400 file:mr-4 file:py-3 file:px-6 file:rounded-full file:border-0 file:bg-blue-600 file:text-white file:font-black cursor-pointer" onChange={e => setSelectedFile(e.target.files?.[0] || null)} required />
              </div>
              <div className="flex gap-4 pt-6">
                <button type="button" onClick={() => setShowAlbumModal(false)} className="flex-1 py-5 text-gray-400 font-black uppercase text-xs">Desistir</button>
                <button type="submit" disabled={loading} className="flex-1 py-5 bg-blue-600 text-white rounded-3xl font-black shadow-2xl hover:bg-blue-700">
                  {loading ? 'SALVANDO...' : 'CONFIRMAR UPLOAD'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}