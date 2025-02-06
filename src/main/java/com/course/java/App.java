package com.course.java;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Escolha uma funções da agenda:");






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
