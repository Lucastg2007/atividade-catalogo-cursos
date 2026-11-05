# Catálogo de Cursos

API REST independente para cadastrar instrutores e cursos, com relacionamento obrigatório de um para muitos. O projeto usa Java 21, Maven Wrapper, Spring Boot 3.5.16, PostgreSQL, Spring Data JPA, Bean Validation e Liquibase.

## Integrantes

- Mateus de Melo
- Lucas Garcia
- Victor Querubim

## Requisitos

- JDK 21
- PostgreSQL 14 ou superior
- Git

## Banco de desenvolvimento

Crie um banco vazio chamado `catalogocursos_dev`. A aplicação cria e valida o esquema exclusivamente pelo Liquibase. Não configure o Hibernate para criar ou atualizar tabelas.

Defina a conexão por variáveis de ambiente. Os valores abaixo são apenas padrões locais para desenvolvimento:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/catalogocursos_dev"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "sua-senha-local"
```

No Linux/macOS, use `export DB_URL=...`, `export DB_USER=...` e `export DB_PASSWORD=...`. Não versione credenciais reais. `.env.example` mostra somente valores ilustrativos.

## Executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

## Testes

Os testes usam um banco H2 isolado em modo PostgreSQL e aplicam o mesmo changelog do Liquibase:

```powershell
.\mvnw.cmd clean test
```

Na verificação desta entrega, os 7 testes automatizados passaram sem falhas ou erros. A suíte cobre associação bidirecional, cadastro com instrutor inexistente, validação HTTP `400`, conflito de e-mail `409`, filtro e ordenação da listagem e prevenção de recursão JSON.

## API

| Método | Rota | Resultado |
| --- | --- | --- |
| `POST` | `/api/instrutores` | Cadastra instrutor (`201`, `Location`) |
| `GET` | `/api/instrutores/{id}` | Consulta instrutor (`200` ou `404`) |
| `POST` | `/api/cursos` | Cadastra curso vinculado a instrutor (`201`, `Location`) |
| `GET` | `/api/cursos/{id}` | Consulta curso (`200` ou `404`) |
| `GET` | `/api/instrutores/{id}/cursos` | Lista cursos do instrutor em ordem alfabética (`200` ou `404`) |

### Exemplo: cadastrar instrutor

```http
POST /api/instrutores
Content-Type: application/json
```

```json
{
  "nome": "Marina Costa",
  "email": "marina.costa@example.com"
}
```

### Exemplo: cadastrar curso

```http
POST /api/cursos
Content-Type: application/json
```

```json
{
  "titulo": "APIs REST com Spring Boot",
  "cargaHoraria": 40,
  "nivel": "INTERMEDIARIO",
  "instrutorId": 1
}
```

A resposta do curso inclui um resumo do instrutor, sem sua coleção de cursos. Assim a API não serializa o ciclo `Curso -> Instrutor -> Cursos`. O lado proprietário é `Curso`, que mantém a chave estrangeira `curso.instrutor_id`; o método de domínio `Instrutor.adicionarCurso` mantém as duas referências em memória.

## Códigos de resposta

- `400 Bad Request`: corpo inválido ou violações de validação.
- `404 Not Found`: instrutor ou curso inexistente.
- `409 Conflict`: e-mail de instrutor já cadastrado ou conflito de integridade.
- `201 Created`: cadastro concluído, acompanhado do cabeçalho `Location`.

Erros seguem o formato `{ "status", "message", "path", "timestamp" }`.

## Respostas solicitadas na entrega

1. `Curso` é o lado proprietário da associação, pois contém a chave estrangeira.
2. A chave estrangeira fica em `curso.instrutor_id` e aponta para `instrutor.id`.
3. A API usa DTOs: a resposta do curso contém somente um resumo do instrutor, e a listagem contém somente resumos de cursos. Isso evita a recursão `Curso -> Instrutor -> Cursos` e mantém o contrato HTTP independente das entidades JPA.

## Estrutura

```text
src/main/java/br/edu/catalogocursos/
├── api/controller
├── api/dto
├── api/exception
├── api/mapper
├── application
├── domain
└── repository
src/main/resources/db/changelog/
```

## Criar e publicar o repositório

O diretório entregue é um projeto independente. Para publicar, crie no GitHub um repositório vazio chamado `atividade-catalogo-cursos` e conecte-o como `origin`:

```bash
git init -b main
git add .
git commit -m "build: criar catálogo independente de cursos"
git remote add origin https://github.com/SEU_USUARIO/atividade-catalogo-cursos.git
git push -u origin main
git rev-parse HEAD
```

Substitua `SEU_USUARIO` pelo usuário real do GitHub. A URL e o hash remoto só existem depois de publicar na conta do estudante.
