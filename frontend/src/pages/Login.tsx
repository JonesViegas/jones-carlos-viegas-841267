import React, { useState } from 'react';
import api from '../service/api';

export function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault();
    
    // Validação básica Sênior: não envia requisição vazia
    if (!username || !password) {
      setError('Por favor, preencha todos os campos.');
      return;
    }

    setIsLoading(true);
    try {
      // O password é enviado em texto puro via POST (HTTPS garante o túnel)
      // A criptografia BCrypt ocorre no Backend, como configuramos.
     const response = await api.post('/auth/login', { 
      username: username.trim(), 
      password: password.trim() 
    });
      
      localStorage.setItem('token', response.data.token);
      window.location.href = '/dashboard';
    } catch (err) {
      setError('Usuário ou senha inválidos');
    } finally {
      setIsLoading(false);
    }
  }

  return (
    // 'notranslate' evita que o Google Tradutor quebre o React (erro removeChild)
    <div className="min-h-screen w-screen flex items-center justify-center bg-blue-900 notranslate">
      <div className="bg-white p-10 rounded-[32px] shadow-2xl w-full max-w-md border-t-8 border-blue-500">
        
        {/* LOGO OFICIAL SEPLAG */}
        <div className="flex justify-center mb-10">
          <img src="/logo-seplag.png" alt="Logo SEPLAG MT" className="h-24 object-contain" />
        </div>

        <div className="text-center mb-10">
          <h2 className="text-2xl font-black text-gray-800 tracking-tighter uppercase">
            Acesso ao Sistema
          </h2>
          <p className="text-gray-400 text-xs font-bold uppercase tracking-widest mt-1">
            Painel de Gestão de Artistas - SEPLAG MT
          </p>
        </div>

        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <label className="block text-[10px] font-black text-gray-400 uppercase mb-2 ml-1">Identificação do Usuário</label>
            <input
              type="text"
              placeholder="Digite seu usuário"
              className="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 focus:bg-white focus:border-blue-500 p-4 outline-none transition-all font-medium"
              value={username}
              onChange={(e) => {
                setUsername(e.target.value);
                setError(''); // Limpa o erro ao digitar (UX Sênior)
              }}
            />
          </div>

          <div>
            <label className="block text-[10px] font-black text-gray-400 uppercase mb-2 ml-1">Senha de Acesso</label>
            <input
              type="password"
              placeholder="••••••••"
              className="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 focus:bg-white focus:border-blue-500 p-4 outline-none transition-all"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
                setError('');
              }}
            />
          </div>
          
          {error && (
            <div className="bg-red-50 text-red-600 p-3 rounded-xl text-sm text-center font-bold animate-pulse">
              ⚠️ {error}
            </div>
          )}
          
          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-blue-600 text-white p-5 rounded-2xl font-black text-sm hover:bg-blue-700 transition-all shadow-xl shadow-blue-100 active:scale-95 disabled:bg-gray-300"
          >
            {isLoading ? 'AUTENTICANDO...' : 'ENTRAR NO SISTEMA'}
          </button>
        </form>
        
        <div className="mt-12 text-center border-t border-gray-50 pt-6">
          <p className="text-[10px] text-gray-300 font-bold uppercase tracking-widest">
            Governo do Estado de Mato Grosso © 2026
          </p>
        </div>
      </div>
    </div>
  );
}