-- Tabela de Artistas
CREATE TABLE artists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL
);

-- Tabela de Álbuns
CREATE TABLE albums (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_date DATE,
    cover_url VARCHAR(512)
);

-- Tabela de Ligação N:N (Exigência do item "Relacionamento Artista-Álbum N:N")
CREATE TABLE artist_album (
    artist_id INTEGER NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    album_id INTEGER NOT NULL REFERENCES albums(id) ON DELETE CASCADE,
    PRIMARY KEY (artist_id, album_id)
);

-- Tabela para Sincronização de Regionais (Item 6.3.1-e)
CREATE TABLE regionais (
    id INTEGER PRIMARY KEY, -- ID que vem da API externa
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);