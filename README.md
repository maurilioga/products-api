# 🛒 Products API

API REST para CRUD de produtos, com validação, paginação e tratamento de erros.

---

## 📌 Tecnologias

- Java 17
- Spring Boot 4.0.2
- PostgreSQL 18
- Liquibase
- Maven
- JUnit + Mockito (unitários)
- UUID para IDs
- Docker

---

## ⚙️ Configuração

### 📝 Pré-Requisitos
1. Java 17
2. Maven
3. Docker

### 🕹️ Executando o projeto

1. Clone o projeto:
```bash
   git clone https://github.com/maurilioga/products-api
   cd products-api
```

2. Banco de dados (PostgreSQL via Docker):
```bash
   docker run --name products-api-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=products-api -p 5432:5432 -d postgres:18
```

3. Configuração `application.properties`:
```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/products-api
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=validate
   spring.liquibase.enabled=true
   spring.liquibase.change-log: classpath:db/changelog/changelog-master.xml
   spring.data.web.pageable.one-indexed-parameters: true
```

4. Build e execução:
```bash
   mvn clean package
   java -jar target/products-api-0.0.1-SNAPSHOT.jar
```

#### 📍 Outra alternativa

Execução direta no docker
```bash
   docker-compose up -d --build
```

---

## 🚀 Endpoints

### 🗺️ URL Base do projeto:
```bash
   http://localhost:8080
```

| Método | URL                 | Descrição                  |
|--------|-------------------|----------------------------|
| POST   | /products          | Cria um produto           |
| GET    | /products/{id}     | Busca produto por ID       |
| PUT    | /products/{id}     | Atualiza produto           |
| DELETE | /products/{id}     | Remove produto             |
| GET    | /products          | Lista produtos (paginação) |

---

## 💻 Exemplos de requisição

**Criar produto:**
```bash
   curl -X POST http://localhost:8080/products \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Notebook",
   "price": 3999.90,
   "description": "Ultrabook 14\""
   }'
```

**Listar produtos:**
```bash
  curl "http://localhost:8080/products?page=0&size=10&sort=price,desc"
```

**Atualizar produto:**

```bash
   curl -X PUT http://localhost:8080/products/{id} \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Notebook Pro",
   "price": 4499.90,
   "description": "Versão atualizada"
   }'
```

**Deletar produto:**

```bash
    curl -X DELETE http://localhost:8080/products/{id}
```
---

## ⚠️ Validações

- `name` obrigatório
- `price` obrigatório e >= 0
- Retorna **400** se dados inválidos
- Retorna **404** se produto não encontrado
- Em casos de erro, a API retorna um objeto padronizado:
```json
   {
     "message": "Parâmetro informado inválido!"
   }
```

---

## 🧭 Swagger
A API está documentada em Swagger. Acesse:
```bash
    http://localhost:8080/swagger-ui.html
```

---

## 🧪 Testes

- Unitários com Mockito
- Executar todos os testes:
```bash
  mvn test
```
---

## 📝 Observações

- IDs são UUIDs V7 (gerado baseado na hora e data de criação)
- Preços como BigDecimal
- Campos `createdAt` e `updatedAt` gerenciados automaticamente
- Paginação padrão: 20 itens/página, ordenação por `createdAt` e `id` decrescente
- É possível filtrar produtos pelos seguintes parâmetros


| Parâmetro | Tipo                | Descrição                  |
|--------|---------------------|----------------------------|
| name   | String              | Busca parcial por nome (case insensitive)          |
| priceMin    | BigDecimal          | Preço mínimo       |
| priceMax    | BigDecimal          | Preço máximo           |
| createdAtFrom | LocalDate           | Data inicial (yyyy-MM-dd)             |
| createdAtTo    | LocalDate           | Data final (yyyy-MM-dd) |


---
