# ZG Hero Project – TODO List (Console Java)

Este repositório contém uma **aplicação de TODO List em Java executada via console**, desenvolvida como parte da trilha **K1-T3 do ZG Hero Project**. O projeto foca em lógica de programação, orientação a objetos, coleções, enums, tratamento de exceções e persistência simples em arquivo.

---

## 📌 Objetivo do Projeto

Implementar um gerenciador de tarefas executado no terminal que permita ao usuário:

* Criar tarefas com nome, descrição, categoria, datas, prioridade e status
* Listar tarefas com diferentes filtros (categoria, prioridade, status e data)
* Atualizar qualquer atributo de uma tarefa existente
* Remover tarefas pelo ID
* Gerar um relatório consolidado por status
* Persistir tarefas em arquivo entre execuções

---

## 🛠️ Tecnologias Utilizadas

As tecnologias **reais** utilizadas neste projeto são:

* **Java (JDK 11+)** – Linguagem principal
* **Java Collections (Set / HashSet)** – Armazenamento das tarefas em memória
* **java.time.LocalDate** – Manipulação de datas
* **Enums** – Representação de Prioridade e Status
* **Maven** – Gerenciamento de dependências e build (`pom.xml`)
* **Aplicação Console (CLI)** – Interface baseada em menu interativo

> ⚠️ Este projeto **não utiliza framework web, front-end, React ou Spring Boot**.

---

## 📂 Estrutura do Projeto

```
ZG-Hero-Project-trilha-K1-T3-TODO-List
├── src
│   └── main
│       └── java
│           └── org
│               └── example
│                   ├── Main.java
│                   ├── domain
│                   │   ├── Tarefa.java
│                   │   ├── Prioridade.java
│                   │   └── Status.java
│                   ├── service
│                   │   └── GerenciadorDeArquivos.java
│                   └── expeption
│                       ├── ErroAoCarregarArquivoException.java
│                       └── ErroAoSalvarArquivoException.java
├── src/main/resources
│   └── tarefas_db.text
├── pom.xml
└── README.md
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* **Java JDK 11 ou superior**
* **Apache Maven** (opcional, mas recomendado)
* Terminal ou IDE (IntelliJ, Eclipse ou VS Code)

### Execução via IDE

1. Importe o projeto como **Maven Project**
2. Execute a classe:

   ```
   org.example.Main
   ```

### Execução via Maven (se configurado)

```bash
mvn compile
mvn exec:java
```

---

## 🧠 Comentários sobre a Solução

* A aplicação utiliza um **menu interativo em loop** para controlar o fluxo do sistema.
* As tarefas são armazenadas em um **Set (HashSet)**, garantindo unicidade das entidades.
* O ID das tarefas é controlado na entidade `Tarefa` e sincronizado após carga do arquivo.
* A persistência é feita em **arquivo texto**, permitindo manter dados entre execuções.
* A classe `GerenciadorDeArquivos` centraliza toda a lógica de leitura e escrita em disco.
* O formato de persistência utiliza separador `;`, facilitando leitura e manutenção.
* Exceções customizadas encapsulam falhas críticas de I/O.

---

## 📊 Funcionalidades Disponíveis

* ➕ Criar tarefa
* 📋 Listar tarefas com filtros
* ✏️ Atualizar tarefas
* 🗑️ Remover tarefas
* 📈 Relatório de status
* 💾 Salvamento automático ao sair

---

## 📄 Licença

Projeto desenvolvido para fins educacionais no **ZG Hero Project**.

---

## ✍️ Autor

**Fernando Santos** 🚀
