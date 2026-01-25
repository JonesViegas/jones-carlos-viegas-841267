-- 1. Tabela de Regionais (Sincronização API Argus)
CREATE TABLE regionais (
    id INTEGER PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabela de Artistas
CREATE TABLE artists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    regional_id INTEGER REFERENCES regionais(id)
);

-- 3. Tabela de Álbuns
CREATE TABLE albums (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_date DATE,
    cover_url VARCHAR(512)
);

-- 4. Tabela de Ligação N:N (EXIGÊNCIA DO EDITAL)
CREATE TABLE artist_album (
    artist_id INTEGER NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    album_id INTEGER NOT NULL REFERENCES albums(id) ON DELETE CASCADE,
    PRIMARY KEY (artist_id, album_id)
);