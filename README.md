# Climby ☁

## 🚀 Proposta de Solução Geral do Ecossistema Climby

**O Problema:**
Eventos climáticos extremos representam uma ameaça crescente, e a dificuldade em acessar informações centralizadas sobre abrigos, alertas e previsões agrava o impacto na população.

**A Solução Climby (Ecossistema):**
O Climby é um ecossistema de soluções projetado para mitigar os impactos de eventos extremos. Ele é composto por:
1.  **Aplicativo Móvel (Frontend para Usuários):** Permite que usuários encontrem abrigos, recebam alertas de eventos (baseados na OpenWeather API e processados pela API C#) e visualizem previsões de risco. Usuários podem criar suas contas através do app.
2.  **API Backend em C# (Core da Solução):** Serve como backend para o aplicativo móvel, processa alertas da OpenWeather API, informações dos sensores IOT e integra a IA de previsões de eventos extremos.
3.  **Aplicação Web Administrativa em Java Spring MVC (Este Repositório):** Uma interface web para os administradores da plataforma Climby gerenciarem o cadastro de abrigos e usuários. Também oferece um assistente virtual (com Spring AI) para suporte e informações gerais.

**Este repositório foca na Aplicação Web Administrativa desenvolvida em Java Spring MVC.**

**Inovação no Contexto da Aplicação Java Admin:**
A aplicação Java Admin contribui para a solução global ao fornecer uma interface robusta e segura (com OAuth2) para a gestão de dados críticos (abrigos e usuários). A inclusão de um assistente virtual com Spring AI oferece um canal de suporte e informação para os administradores. O uso de RabbitMQ para notificações internas de cadastro demonstra uma arquitetura preparada para desacoplamento e escalabilidade.

---

## ℹ️ Sobre o Projeto (Global Solution FIAP - Java Advanced)

Este projeto refere-se à entrega da **Aplicação Web Administrativa (Java Spring MVC)** da Global Solution para a disciplina de Java Advanced.

**Equipe:**
* Celeste Mayumi Pereira Tanaka - RM552865
* Lívia Mariana Lopes - RM552558
* Luana Vieira Santos da Silva - RM552994

---

## ✨ Funcionalidades Implementadas (Aplicação Java Admin Web)

* **Autenticação Segura com OAuth2:** Login social para administradores utilizando Google e/ou GitHub.
* **Gerenciamento de Usuários (CRUD):** Cadastro, listagem, edição e exclusão de contas de usuários (que podem ter sido criadas também via app mobile). Validação dos dados de entrada.
* **Gerenciamento de Abrigos (CRUD):** Cadastro, listagem, edição e exclusão de abrigos. Validação dos dados de entrada. 
* **Internacionalização:** Suporte para Português (Brasil) e Inglês.
* **Assistente Virtual (Spring AI):** Interface de chat para administradores fazerem perguntas sobre o tema de eventos extremos ou buscarem apoio emocional simulado.
* **Mensageria Assíncrona com RabbitMQ:**
    * **Produtor:** Envia mensagens quando um novo abrigo é cadastrado pelos administradores.
    * **Consumidor:** Processa essas mensagens (ex: para log ou futuras integrações).
* **Testes Automatizados:** Testes unitários para serviços e testes de integração para fluxos chave.
* **Templates Dinâmicos com Thymeleaf:** Interface administrativa web.

---

## ☁️ Implantação em Nuvem com ACR/ACI 
Esta seção descreve o processo de implantação da API Odontoprev na nuvem utilizando Azure Container Registry (ACR) e Azure Container Instances (ACI), além da configuração do banco de dados no SQL Azure.

### ✅ Passo a passo para execução da aplicação em nuvem
### 1️⃣ Clone o repositório

```sh
git clone https://github.com/LiviaMarianaLopes/climby-java-mvc.git

cd climby-java-mvc

```
### 2️⃣ Criação do Banco de Dados no Azure
No portal da Azure, crie um Azure SQL Database.

### 3️⃣ Ajustar Dependências para o Deploy
Para preparar a aplicação para o deploy, é necessário desativar as dependências do OAuth2.

Abra o arquivo build.gradle.

Localize o seguinte bloco e, conforme a instrução, comente as duas linhas implementation adicionando // no início de cada uma.


```Groovy

// comente essas duas linhas caso for fazer o deploy da aplicação
implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
implementation 'org.springframework.security:spring-security-oauth2-jose'
```

### 4️⃣ Configuração do Banco de Dados na Aplicação

No arquivo application.properties, comente as configurações existentes do banco de dados (comente também as linhas do github login e do goolge login) e adicione as linhas abaixo (substitua as credenciais pelas suas):
```properties
# Desativação do RabbitMQ
spring.rabbitmq.listener.simple.auto-startup=false

#Configuração do banco de dados
spring.datasource.url=jdbc:sqlserver://<SEU_HOST>:1433;database=<SEU_BANCO>;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=<SEU_USUARIO>
spring.datasource.password=<SUA_SENHA>
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Configurações do Hibernate
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
### 5️⃣ Deploy da Aplicação no Azure

Login na Azure e Criação do Grupo de Recursos

```sh
az login
```
Depois, crie um Grupo de Recursos:

```sh
az group create --name rg-climby --location eastus
```
Crie o Azure Container Registry (ACR):

```sh

az acr create --resource-group rg-climby --name climbyjavarm552558 --sku Basic
```
Autentique-se no ACR:

```sh

az acr login --name climbyjavarm552558
```
Construa a imagem Docker:

```sh
docker build -t climby-java .
```
Marque e envie a imagem para o ACR:

```sh
docker tag climby-java climbyjavarm552558.azurecr.io/climby-java:v1
docker push climbyjavarm552558.azurecr.io/climby-java:v1
```
Crie o Azure Container Instance (ACI) para rodar a aplicação na nuvem:

```sh

az container create --resource-group rg-climby --name climbyjavarm552558 \
  --image climbyjavarm552558.azurecr.io/climby-java:v1 \
  --cpu 1 --memory 1 \
  --registry-login-server climbyjavarm552558.azurecr.io \
  --registry-username climbyjavarm552558 \
  --registry-password <SUA_SENHA> \
  --ip-address Public --dns-name-label climbyjavarm552558 \
  --ports 3000 80 8080 --os-type Linux
```
Após a implantação, obtenha o endereço IP da API rodando o comando:

```sh
az container show --resource-group rg-climby --name climbyjavarm552558 --query ipAddress.ip --output tsv
```
A aplicação estará disponível em:

```cpp
http://<endereço-ip>:8080
```
### 6️⃣ Vídeo demonstrando o deploy até a persistência de dados em Nuvem 
🎥 [Devops-gs](https://www.youtube.com/watch?v=g8D0gsSWPLg)

---

## 🛠️ Tecnologias Utilizadas (Aplicação Java Admin Web)

* **Backend:** Java 21, Spring Boot 3.5.0, Spring MVC, Spring Data JPA, Spring Security (OAuth2), Spring AMQP (RabbitMQ), Spring AI.
* **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 5.3.3.
* **Banco de Dados:** Oracle.
* **Persistência:** Hibernate.
* **Mensageria:** RabbitMQ.
* **Inteligência Artificial (Assistente Virtual):** llama2 via Spring AI.
* **Testes:** JUnit 5, Mockito, Spring Test, AssertJ.
* **Build:** Gradle.
* **Outros:** Lombok.

---

## 🚀 Configuração do Ambiente de Desenvolvimento (Aplicação Java Admin Web)

**Pré-requisitos:**
* Java JDK 21.
* Gradle.
* IDE Java.
* Docker Desktop (para RabbitMQ).
* Ollama (se usando para o Assistente Virtual na App Java) instalado e com o modelo `llama2` baixado.
* Acesso à internet.

**Variáveis de Ambiente / Propriedades Essenciais (para `application.properties`):**

* **OAuth2 (Google & GitHub para login dos Admins):**
    * `GOOGLE_CLIENT_ID` / `spring.security.oauth2.client.registration.google.client-id`
    * `GOOGLE_CLIENT_SECRET` / `spring.security.oauth2.client.registration.google.client-secret`
    * `GITHUB_CLIENT_ID` / `spring.security.oauth2.client.registration.github.client-id`
    * `GITHUB_CLIENT_SECRET` / `spring.security.oauth2.client.registration.github.client-secret`

*(Instruções detalhadas sobre como obter essas chaves no documento PDF).*

**Passos para Execução Local:**
1.  Clone o repositório: `git clone https://github.com/LiviaMarianaLopes/climby-java-mvc.git`
2.  Navegue até a pasta do projeto: `cd climby-java-mvc`
3.  **Inicie os Serviços Externos:**
    * **RabbitMQ (Docker):** `docker run -d --name climby-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management` (Docker desktop precisa estar aberto)
    * **Ollama (se usado para o Assistente Virtual):** Inicie o aplicativo Ollama e certifique-se que o modelo configurado está disponível.
4.  Configure as propriedades necessárias no `src/main/resources/application.properties` ou via variáveis de ambiente.
5.  Execute a aplicação pela sua IDE.
6.  Acesse: `http://localhost:8080`

---

## 🔗 Links dos vídeos

* **Vídeo Demonstração:** 
* **Vídeo Pitch:**

---
