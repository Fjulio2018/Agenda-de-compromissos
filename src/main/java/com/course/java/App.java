package com.course.java;

public class App {
    public static void main(String[] args) {
        // Vamos incluir os compromissos da semana em um arquivo Json
        agendarCompromisso();

        // Vamos alterar algum compromisso
        alterarCompromisso();

        // Vamos visualizar os compromisso
        visualizarCompromisso();

        // Vamos deletar os compromissos passados
        deletarCompromisso();




    }

    private static void alterarCompromisso() {

        System.out.println("Neste metodo serão alterados os compromissos");
    }

    private static void deletarCompromisso() {

        System.out.println("Neste metodo serão deletados os compromissos");

    }

    private static void visualizarCompromisso() {
        System.out.println("Neste metodo serão visualizados os compromissos");
    }

    private static void agendarCompromisso() {

        System.out.println("Neste metodo serão agendados os compromissos");

    }
}
