# Projeto Seletivo SEPLAG-MT 2026
## Cargo: Analista de TI - Perfil Engenheiro da Computação (Sênior)
**Candidato:** Jones Carlos Viegas

### 🏗 Arquitetura do Projeto
O sistema foi desenvolvido seguindo os padrões de senioridade exigidos pelo edital, utilizando uma arquitetura de microsserviços isolada via Docker.

*   **Backend:** Java 17 com Spring Boot 3.4.
*   **Banco de Dados:** PostgreSQL 15 (Isolado na porta 5433 para evitar conflitos).
*   **Migrações:** Flyway para controle de versão de banco.
*   **Segurança:** Spring Security + JWT (Expiração de 5 minutos).
*   **Storage:** MinIO (S3 compatible) para armazenamento de capas de álbuns.
*   **Resiliência:** Rate Limiting (Bucket4j) limitado a 10 req/min por usuário.
*   **Tempo Real:** WebSocket para notificações de novos álbuns.
*   **Integração:** Sincronização automática com API de Regionais (Argus API).

### 🚀 Como Executar o Projeto

1.  **Pré-requisitos:** Docker Desktop e Java 17 instalados.
2.  **Subir Infraestrutura:** No terminal, execute:
    ```bash
    docker-compose up -d
    ```
3.  **Rodar Aplicação:** 
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Acessar Swagger (Documentação):**
    `http://localhost:8080/swagger-ui/index.html`

### 🔑 Credenciais de Teste
*   **Endpoint de Login:** `POST /api/auth/login`
*   **Usuário:** `admin`
*   **Senha:** `admin123`

### 🛠 Funcionalidades Implementadas (Checklist Edital)
*   [x] **6.3.1-b:** Autenticação JWT 5 min.
*   [x] **6.3.1-c:** WebSockets para novos álbuns.
*   [x] **6.3.1-d:** Rate Limit 10 req/min.
*   [x] **6.3.1-e:** Sincronização com Regionais (Inserir/Inativar/Versionar).
*   [x] **6.3.1-h:** Armazenamento MinIO (S3).
*   [x] **6.3.1-i:** Links pré-assinados de 30 min para capas.
*   [x] **6.3.1-k:** Migrações Flyway.