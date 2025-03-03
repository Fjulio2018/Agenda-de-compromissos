package com.course.java.helper;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExecHelper {

    public static String coletaOpcao(Scanner sc) {
        while (true) {
            System.out.println("Escolha uma função da agenda:");
            System.out.println("(01) Inclusão");
            System.out.println("(02) Visualização");
            System.out.println("(03) Alteração");
            System.out.println("(04) Exclusão");
            System.out.println("(05) Exclusão de TODOS compromissos");
            System.out.println("(06) Encerrar Programa");
            String opcao = sc.nextLine().trim();
            if (opcao.matches("01|02|03|04|05|06")) {
                return opcao;
            } else {
                System.out.println("❌ Opção inválida! Escolha entre 01, 02, 03, 04, 05 ou 06.");
            }
        }
    }


    public static JSONObject coletaCompromisso(Scanner sc) {
        System.out.println("📅 Novo Compromisso");

        System.out.print("Título: ");
        String titulo = sc.nextLine().trim();

        LocalDate dataInserida = coletaData(sc);
        LocalTime horaInserida = coletaHora(sc, dataInserida);

        System.out.print("Descrição: ");
        String descricao = sc.nextLine().trim();

        JSONObject compromisso = new JSONObject();
        compromisso.put("titulo", titulo);
        compromisso.put("data", dataInserida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        compromisso.put("hora", horaInserida.format(DateTimeFormatter.ofPattern("HH:mm")));
        compromisso.put("descricao", descricao);

        return compromisso;
    }

    private static LocalDate coletaData(Scanner sc) {
        String data;
        LocalDate dataInserida;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoje = LocalDate.now();

        while (true) {
            System.out.print("Data (DD/MM/AAAA): ");
            data = sc.nextLine().trim();
            try {
                dataInserida = LocalDate.parse(data, dateFormatter);
                if (!dataInserida.isBefore(hoje)) {
                    break;
                } else {
                    System.out.println("❌ Data inválida! O compromisso deve ser a partir de hoje.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida! Use o formato DD/MM/AAAA.");
            }
        }
        return dataInserida;
    }

    private static LocalTime coletaHora(Scanner sc, LocalDate dataInserida) {
        String hora;
        LocalTime horaInserida;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate hoje = LocalDate.now();

        while (true) {
            System.out.print("Hora (HH:MM): ");
            hora = sc.nextLine().trim();
            try {
                horaInserida = LocalTime.parse(hora, timeFormatter);
                if (dataInserida.equals(hoje) && horaInserida.isBefore(LocalTime.now())) {
                    System.out.println("❌ Hora inválida! Para compromissos de hoje, a hora deve ser no futuro.");
                } else {
                    break;
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Hora inválida! Use o formato HH:MM.");
            }
        }
        return horaInserida;
    }

    public static void exibirCompromissos() {
        ArrayList<JSONObject> compromissos = DatabaseHelper.busqueCompromissos();

        if (compromissos.isEmpty()) {
            System.out.println("📭 Nenhum compromisso encontrado.");
            return;
        }

        System.out.println("\n📅 Lista de Compromissos:\n");

        for (JSONObject compromisso : compromissos) {
            System.out.println("🆔 ID: " + compromisso.getString("id"));
            System.out.println("📌 Título: " + compromisso.getString("titulo"));
            System.out.println("📆 Data: " + compromisso.getString("data"));
            System.out.println("📆 Hora: " + compromisso.getString("hora"));
            System.out.println("📝 Descrição: " + compromisso.getString("descricao"));
            System.out.println("--------------------------------------------------");
        }
    }

    public static void alterarCompromisso(Scanner sc) {
        System.out.print("Digite o ID do compromisso que deseja alterar: ");
        String id = sc.nextLine().trim();

        System.out.print("Novo título: ");
        String novoTitulo = sc.nextLine().trim();

        System.out.print("Nova descrição: ");
        String novaDescricao = sc.nextLine().trim();

        System.out.print("Nova data (DD-MM-AAAA): ");
        String novaData = sc.nextLine().trim();

        System.out.print("Nova hora (HH:MM): ");
        String novaHora = sc.nextLine().trim();

        DatabaseHelper.alterarCompromisso(id, novoTitulo, novaDescricao, novaData,  novaHora);
    }

    public static void deletarCompromisso(Scanner sc) {
        System.out.print("Digite o ID do compromisso que deseja excluir: ");
        String id = sc.nextLine().trim();

        DatabaseHelper.deletarCompromisso(id);
    }

    public static void deletarTodosCompromissos(Scanner sc) {
        while (true) {
            System.out.print("Deseja excluir todos seus compromissos? S para sim ou N para não: ");
            String confirma = sc.nextLine().trim();

            if (confirma.equalsIgnoreCase("N")) {
                System.out.println("Voltando ao menu principal...");
                return;
            } else if (confirma.equalsIgnoreCase("S")) {
                DatabaseHelper.deletarTudo();
                System.out.println("✅ Todos os compromissos foram excluídos.");
                return;
            } else {
                System.out.println("❌ Opção inválida! Escolha S ou N.");
            }
        }
    }

}
