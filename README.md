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

## Como Executar

1.  Clone o repositório do projeto.
2.  Navegue até o diretório do projeto no terminal.
3.  Execute o comando `mvn compile` para compilar o código.
4.  Execute o comando `mvn exec:java -Dexec.mainClass="com.seu_pacote.Main"` para executar a aplicação.

## Como Usar

A aplicação deve apresentar um menu com as opções para criar, visualizar, alterar e deletar compromissos. O usuário deve interagir com o menu para realizar as operações desejadas.

## Formato do Arquivo JSON

O arquivo `agenda.json` deve armazenar os compromissos em formato JSON. Por exemplo:

```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",  
    "titulo": "Reunião com cliente",
    "data": "2024-08-22",
    "hora": "10:00",
    "descricao": "Discutir o novo projeto"
  },
  {
    "id": "f0e1d2c3-b4a5-9876-5432-109876fedcba",  
    "titulo": "Almoço com a equipe",
    "data": "2024-08-23",
    "hora": "12:00",
    "descricao": "Comemoração do projeto"
  }
]