package com.course.java;

import com.course.java.helper.DatabaseHelper;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class App4 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        switch (coletaOpcao(sc)) {
            case "01":
                JSONObject novoCompromisso = coletaCompromisso(sc);
                ArrayList<JSONObject> listaCompromissos = new ArrayList<>();
                listaCompromissos.add(novoCompromisso);
                DatabaseHelper.salvarCompromissos(listaCompromissos);
                System.out.println("✅ Compromisso agendado com sucesso!");
                break;
            case "02":
                exibirCompromissos();
                break;
            case "03":
                alterarCompromisso(sc);
                break;
            case "04":
                deletarCompromisso(sc);
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static String coletaOpcao(Scanner sc) {
        while (true) {
            System.out.println("Escolha uma função da agenda:");
            System.out.println("(01) Inclusão");
            System.out.println("(02) Visualização");
            System.out.println("(03) Alteração");
            System.out.println("(04) Exclusão");
            String opcao = sc.nextLine().trim();
            if (opcao.matches("01|02|03|04")) {
                return opcao;
            } else {
                System.out.println("❌ Opção inválida! Escolha entre 01, 02, 03 ou 04.");
            }
        }
    }

    private static JSONObject coletaCompromisso(Scanner sc) {
        System.out.println("📅 Novo Compromisso");

        System.out.print("Título: ");
        String titulo = sc.nextLine().trim();

        String data;
        LocalDate dataInserida;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate hoje = LocalDate.now();

        while (true) {
            System.out.print("Data (AAAA-MM-DD): ");
            data = sc.nextLine().trim();
            try {
                dataInserida = LocalDate.parse(data, dateFormatter);
                if (!dataInserida.isBefore(hoje)) {
                    break;
                } else {
                    System.out.println("❌ Data inválida! O compromisso deve ser a partir de hoje.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Data inválida! Use o formato AAAA-MM-DD.");
            }
        }

        System.out.print("Descrição: ");
        String descricao = sc.nextLine().trim();

        JSONObject compromisso = new JSONObject();
        compromisso.put("titulo", titulo);
        compromisso.put("data", data);
        compromisso.put("descricao", descricao);

        return compromisso;
    }

    private static void exibirCompromissos() {
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
            System.out.println("📝 Descrição: " + compromisso.getString("descricao"));
            System.out.println("--------------------------------------------------");
        }
    }

    private static void alterarCompromisso(Scanner sc) {
        System.out.print("Digite o ID do compromisso que deseja alterar: ");
        String id = sc.nextLine().trim();

        System.out.print("Novo título: ");
        String novoTitulo = sc.nextLine().trim();

        System.out.print("Nova descrição: ");
        String novaDescricao = sc.nextLine().trim();

        System.out.print("Nova data (AAAA-MM-DD): ");
        String novaData = sc.nextLine().trim();

        DatabaseHelper.alterarCompromisso(id, novoTitulo, novaDescricao, novaData);
    }

    private static void deletarCompromisso(Scanner sc) {
        System.out.print("Digite o ID do compromisso que deseja excluir: ");
        String id = sc.nextLine().trim();

        DatabaseHelper.deletarCompromisso(id);
    }
}
