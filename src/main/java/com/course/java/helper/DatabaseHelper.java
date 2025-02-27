package com.course.java.helper;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/julio/OneDrive/Documentos/Aulas IBM/IBM_STUDIO.db";


    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }


    public static void salvarCompromissos(ArrayList<JSONObject> compromissos) {
        String insertSQL = "INSERT INTO compromissos (titulo, descricao, data) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for (JSONObject compromisso : compromissos) {
                pstmt.setString(1, compromisso.getString("titulo"));
                pstmt.setString(2, compromisso.getString("descricao"));
                pstmt.setString(3, compromisso.getString("data"));
                pstmt.executeUpdate();
            }
            System.out.println("✅ Compromissos salvos no banco de dados com sucesso!");
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao salvar compromissos no banco de dados: " + e.getMessage());
        }
    }


    public static ArrayList<JSONObject> busqueCompromissos() {
        ArrayList<JSONObject> compromissos = new ArrayList<>();
        String querySQL = "SELECT id, titulo, descricao, data FROM compromissos";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                JSONObject compromisso = new JSONObject();
                compromisso.put("id", rs.getString("id"));
                compromisso.put("titulo", rs.getString("titulo"));
                compromisso.put("descricao", rs.getString("descricao"));
                compromisso.put("data", rs.getString("data"));
                compromissos.add(compromisso);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao buscar compromissos: " + e.getMessage());
        }
        return compromissos;
    }


    public static void alterarCompromisso(String id, String novoTitulo, String novaDescricao, String novaData) {
        String updateSQL = "UPDATE compromissos SET titulo = ?, descricao = ?, data = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, novoTitulo);
            pstmt.setString(2, novaDescricao);
            pstmt.setString(3, novaData);
            pstmt.setString(4, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Compromisso atualizado com sucesso!");
            } else {
                System.out.println("❌ Nenhum compromisso encontrado com esse ID.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao atualizar compromisso: " + e.getMessage());
        }
    }


    public static void deletarCompromisso(String id) {
        String deleteSQL = "DELETE FROM compromissos WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setString(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Compromisso excluído com sucesso!");
            } else {
                System.out.println("❌ Nenhum compromisso encontrado com esse ID.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao excluir compromisso: " + e.getMessage());
        }
    }
}
