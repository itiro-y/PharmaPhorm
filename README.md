<h1 align="center">💊 PharmaPhorm</h1>

<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com/?lines=Sistema+de+Gestão+Farmacêutica;Controle+de+Estoque,+Vendas+e+Clientes&center=true&width=500&height=45">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/SpringBoot-2.7.5-brightgreen?style=for-the-badge&logo=springboot">
  <img src="https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql">
  <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Status-Em%20Desenvolvimento-orange?style=for-the-badge">
</p>

---

## 📌 Descrição

O **PharmaPhorm** é um sistema completo de gestão farmacêutica desenvolvido com foco em controle de estoque, gerenciamento de vendas e cadastro de clientes. Idealizado para farmácias e drogarias de pequeno e médio porte, ele permite uma administração prática e eficiente das operações do dia a dia.

---

## 🚀 Tecnologias Utilizadas

- ✅ **Java 17**
- ✅ **Spring Boot**
- ✅ **Thymeleaf**
- ✅ **PostgreSQL (via Docker)**
- ✅ **Maven**
- ✅ **Docker Compose**
- ✅ **Lombok**

---

## 📦 Estrutura do Projeto

PharmaPhorm/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   │   ├── templates/    # Arquivos Thymeleaf
│   │   │   ├── static/       # CSS, JS, imagens
│   └── test/                # Testes unitários
├── docker/
│   └── postgres/             # Arquivos de configuração do PostgreSQL
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

---

🐳 Como Rodar com Docker

# 1. Clone o repositório
git clone git@github.com:matheuskya/PharmaPhorm.git

# 2. Navegue até a pasta do projeto
cd PharmaPhorm

# 3. Suba o banco de dados
docker-compose up -d

# 4. Rode o projeto
./mvnw spring-boot:run

---

🧪 Funcionalidades Principais

- Cadastro de clientes
- Controle de estoque de medicamentos
- Registro de vendas
- Dashboard com métricas
- Controle de usuários e permissões

---

💡 Contribuindo

Sinta-se à vontade para abrir uma issue, enviar um pull request ou sugerir melhorias!

📫 Participantes

Desenvolvido por Matheus Kenzo, João Vitor Ferrari, Guilherme Pinheiro Moura, Ayrton Itiro e Kevin Luiz

<p align="center">
  <img src="https://forthebadge.com/images/badges/made-with-java.svg">
  <img src="https://forthebadge.com/images/badges/powered-by-coffee.svg">
</p>
