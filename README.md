# Projeto de Agenda de Compromissos

Este projeto implementa uma agenda de compromissos em Java, utilizando um arquivo JSON para armazenar os dados. O projeto oferece funcionalidades CRUD (Create, Read, Update, Delete) para gerenciar os compromissos da semana.

## Requisitos

*   Java 23 ou versão posterior
*   Maven

## Escopo do Projeto

O projeto abrange as seguintes funcionalidades do CRUD:

*   **Criar (Create):** Incluir novos compromissos na agenda, armazenando-os no arquivo JSON.
*   **Visualizar (Read):** Exibir os compromissos da semana, lendo os dados do arquivo JSON.
*   **Alterar (Update):** Modificar um compromisso existente, atualizando as informações no arquivo JSON.
*   **Deletar (Delete):** Remover compromissos passados da agenda, excluindo-os do arquivo JSON.

## Como Criar o Banco

1. Faça o download do SQLiteStudio
2. Crie um banco com o nome de IBM_STUDIO.db
3. Crie a tabela:
   CREATE TABLE IF NOT EXISTS compromissos (
   id INTEGER PRIMARY KEY AUTOINCREMENT,
   titulo TEXT NOT NULL,
   descricao TEXT,
   data TEXT NOT NULL
   );



## Como Executar

1.  Clone o repositório do projeto.
2.  Navegue até o diretório do projeto no terminal.
3.  Execute o comando `mvn compile` para compilar o código.
4.  Execute o comando `mvn exec:java -Dexec.mainClass="com.seu_pacote.Main"` para executar a aplicação.

## Como Usar

A aplicação deve apresentar um menu com as opções para criar, visualizar, alterar e deletar compromissos. O usuário deve interagir com o menu para realizar as operações desejadas.
