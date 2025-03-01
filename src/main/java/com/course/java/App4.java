package com.course.java;

import com.course.java.helper.DatabaseHelper;
import com.course.java.helper.ExecHelper;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Scanner;

public class App4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String opcao = ExecHelper.coletaOpcao(sc);
            switch (opcao) {
                case "01":
                    JSONObject novoCompromisso = ExecHelper.coletaCompromisso(sc);
                    ArrayList<JSONObject> listaCompromissos = new ArrayList<>();
                    listaCompromissos.add(novoCompromisso);
                    DatabaseHelper.salvarCompromissos(listaCompromissos);
                    System.out.println("✅ Compromisso agendado com sucesso!");
                    break;
                case "02":
                    ExecHelper.exibirCompromissos();
                    break;
                case "03":
                    ExecHelper.alterarCompromisso(sc);
                    break;
                case "04":
                    ExecHelper.deletarCompromisso(sc);
                    break;
                case "05":
                    ExecHelper.deletarTodosCompromissos(sc);
                    break;
                case "06":
                    System.out.println("👋 Encerrando o programa. Até logo!");
                    sc.close();
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
