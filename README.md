# 🚀 EduFlow API: Gestão de Cursos & Streaming de Vídeo

O **EduFlow** é uma API de nível profissional desenvolvida com **Spring Boot 3.4**, focada em gerenciar o ecossistema completo de uma plataforma de cursos online. O projeto vai além do CRUD básico, implementando segurança robusta, processamento assíncrono de vídeos e uma arquitetura de banco de dados otimizada.



---

## 🛠️ Stack Tecnológica

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot 3.4
* **Segurança:** Spring Security + JWT (Stateless)
* **Banco de Dados:** PostgreSQL
* **Persistência:** Spring Data JPA / Hibernate
* **Comunicação API:** Spring WebClient (Reativo)
* **Integração de Vídeo:** Mux Video API
* **Testes:** JUnit 5, Mockito e AssertJ
* **Produtividade:** Lombok e Java Records

---

## 🏗️ Arquitetura e Diferenciais Técnicos

### 1. Modelagem de Dados Inteligente
Utilização de **Shared Primary Key** para as entidades `Student` e `Instructor`. Ambas compartilham o mesmo ID da entidade `User`, garantindo integridade e eliminando a necessidade de mapeamentos complexos.

### 2. Processamento Assíncrono de Vídeo
Integração com a API do **Mux** para streaming.
* O upload é feito via `WebClient`.
* Um serviço agendado (`@Scheduled`) faz o *polling* automático para verificar quando o vídeo terminou de processar, atualizando o link de reprodução sem intervenção manual.



### 3. Integridade e Transacionalidade
Uso rigoroso de `@Transactional` em processos críticos, como o cadastro de usuários, garantindo que o sistema nunca crie um usuário sem seu respectivo perfil (aluno/instrutor).

---

## ✨ Funcionalidades

- **Autenticação & Autorização:**
    - Login via JWT.
    - Controle de acesso baseado em Roles (**ADMIN**, **INSTRUCTOR**, **STUDENT**).
- **Gestão de Cursos:**
    - Cadastro e edição de cursos (exclusivo para instrutores donos do curso).
    - Listagem com **Paginação** e filtros por categoria.
- **Aulas (Lessons):**
    - Upload de vídeos integrado ao Mux.
    - Sincronização automática de status de vídeo.
- **Matrículas (Enrollments):**
    - Fluxo de inscrição de alunos com validação de duplicidade.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java 21
* Docker (opcional, para o banco) ou PostgreSQL local.
* Conta na Mux para as chaves de API.

### Configuração

1. Clone o repositório:
```bash
git clone [https://github.com/seu-usuario/api-cursos.git](https://github.com/seu-usuario/api-cursos.git)
```

2. Configure as variáveis no seu `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
jwt.secret=${JWT_SECRET}
mux.token.id=${MUX_TOKEN_ID}
mux.token.secret=${MUX_TOKEN_SECRET}
```

3. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

## 🧪 Suíte de Testes
O projeto conta com testes unitários cobrindo as regras de negócio críticas, garantindo que atualizações futuras não quebrem funcionalidades existentes.

```bash
./mvnw test
```

**Desenvolvido por Ryan Dias** *Projeto focado em boas práticas de engenharia de software, integridade de dados e escalabilidade.*
