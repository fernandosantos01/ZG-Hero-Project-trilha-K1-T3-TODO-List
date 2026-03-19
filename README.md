# ZG Hero Project – TODO List (Backend Java & Frontend Web)

Este repositório contém uma aplicação de **TODO List**, desenvolvida como parte da trilha **K1-T3 do ZG Hero Project**. O projeto evoluiu para um *monorepo* contendo o Backend (Console Java) com foco em lógica de programação, persistência de dados e testes automatizados, e um Frontend (Interface Web) construído com tecnologias web padrão.

---

## 📌 Objetivo do Projeto

Implementar um gerenciador de tarefas completo, abordando os conceitos de ponta a ponta:

**No Backend (Java):**

* Criar tarefas com nome, descrição, categoria, datas, prioridade, status **e alarme**.
* Listar tarefas com diferentes filtros (categoria, prioridade, status e data).
* Atualizar qualquer atributo de uma tarefa existente.
* Remover tarefas pelo ID.
* Gerar um relatório consolidado por status.
* Ser notificado quando uma tarefa atingir a data configurada para o alarme.
* Persistir tarefas em arquivo (`tarefas_db.text`) entre execuções.

**No Frontend (Web):**

* Fornecer uma interface gráfica intuitiva para o usuário gerenciar suas tarefas (CRUD em memória).
* Possibilitar a filtragem visual rápida das tarefas por status (TODO, DOING, DONE).

---

## 🛠️ Tecnologias Utilizadas

**Backend:**

* **Java (JDK 21)** – Linguagem principal.
* **Java Collections (Set / HashSet)** – Armazenamento em memória.
* **java.time.LocalDateTime** – Manipulação de datas e alarmes.
* **Enums** – Representação de Prioridade e Status.
* **Maven** – Gerenciamento de dependências e build (`pom.xml`).
* **Spock Framework & Groovy** – Criação de testes automatizados e BDD.

**Frontend:**

* **HTML5** – Estruturação semântica da página.
* **CSS3** – Estilização, layout e responsividade.
* **JavaScript (Vanilla)** – Lógica de manipulação do DOM e gerenciamento de estado na interface.

---

## 📂 Estrutura do Projeto

O projeto adota uma arquitetura de pastas separadas para isolar as responsabilidades:

```text
ZG-Hero-Project-trilha-K1-T3-TODO-List
├── backend/
│   ├── src/main/java/org/example/      # Código fonte Java (Domínio, Exceções, Serviços e Main)
│   ├── src/main/resources/             # Banco de dados em texto (tarefas_db.text)
│   ├── src/test/groovy/                # Testes automatizados (Spock)
│   └── pom.xml                         # Configurações do Maven
├── frontend/
│   ├── css/style.css                   # Estilos visuais da página
│   ├── js/script.js                    # Lógica de interface e CRUD temporário
│   └── index.html                      # Estrutura principal da página
└── README.md
```

---

## 🧪 Testes Automatizados (Backend)

O backend possui uma suíte de testes automatizados desenvolvida utilizando o **Spock Framework (Groovy)**. Os testes garantem a confiabilidade das regras de negócio e da manipulação do console.

**Cenários testados incluem:**

* Criação de tarefas garantindo a entrada correta de dados via `Scanner` simulado.
* Remoção de tarefas por ID (existentes e não existentes).
* Atualização de atributos de tarefas.
* Verificação da listagem no console redirecionando o `System.out`.

---

## 🖥️ Interface Gráfica (Frontend)

Uma interface amigável foi desenvolvida para ilustrar como o usuário final interage com a aplicação.

> ⚠️ Nesta etapa, o frontend realiza o CRUD apenas em memória (JavaScript), sem integração com o backend.

**Recursos visuais:**

* Formulário dinâmico (criação/edição)
* Lista de tarefas em formato de cards
* Badges coloridos por status
* Filtro em tempo real (Todos, TODO, DOING, DONE)

---

## ⏰ Funcionalidade de Alarme (Backend)

O sistema conta com um recurso de **alarme de tarefas**, permitindo notificar o usuário sobre prazos importantes.

* Cada tarefa pode possuir um tempo de antecedência para alerta
* O sistema utiliza `ScheduledExecutorService` para verificação periódica (thread separada)
* Quando o momento do alarme é atingido:

  * A tarefa é destacada no console
  * Uma notificação visual é exibida

---

## 🚀 Como Executar o Projeto

### 🔹 Backend (Java)

```bash
cd backend
mvn compile
mvn exec:java
```

Ou execute pela IDE a classe:

```
org.example.Main
```

### 🔹 Frontend (Web)

* Navegue até a pasta `frontend`
* Abra o arquivo `index.html` no navegador

(Opcional: usar extensão Live Server no VS Code)

---

## 🧠 Comentários sobre a Solução

* Forte uso de validação de entrada via `Scanner`
* Uso de exceções customizadas para controle de falhas
* Testes com Spock utilizando abordagem BDD (Given/When/Then)
* Separação clara entre backend e frontend (monorepo)
* Estrutura preparada para futura evolução com API REST

---

## 📄 Licença

Projeto desenvolvido para fins educacionais no **ZG Hero Project**.

---

## ✍️ Autor

**Fernando Santos** 🚀
