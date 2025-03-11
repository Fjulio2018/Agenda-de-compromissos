package com.course.java.database;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/julio/OneDrive/Documentos/Aulas IBM/IBM_STUDIO.db";
    private Connection conn;

    public DataBase() {
        this.conn = connect();
        initializeDatabase();
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS compromissos (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descricao TEXT, data TEXT, hora TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public void salvarCompromisso(JSONObject compromisso) {
        String insertSQL = "INSERT INTO compromissos (titulo, descricao, data, hora) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, compromisso.getString("titulo"));
            pstmt.setString(2, compromisso.getString("descricao"));
            pstmt.setString(3, compromisso.getString("data"));
            pstmt.setString(4, compromisso.getString("hora"));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao salvar compromisso: " + e.getMessage());
        }
    }

    public ArrayList<JSONObject> busqueCompromissos() {
        ArrayList<JSONObject> compromissos = new ArrayList<>();
        String querySQL = "SELECT id, titulo, descricao, data, hora FROM compromissos";
        try (PreparedStatement pstmt = conn.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                JSONObject compromisso = new JSONObject();
                compromisso.put("id", rs.getString("id"));
                compromisso.put("titulo", rs.getString("titulo"));
                compromisso.put("descricao", rs.getString("descricao"));
                compromisso.put("data", rs.getString("data"));
                compromisso.put("hora", rs.getString("hora"));
                compromissos.add(compromisso);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao buscar compromissos: " + e.getMessage());
        }
        return compromissos;
    }

    public void alterarCompromisso(String id, String novoTitulo, String novaDescricao, String novaData, String novaHora) {
        String updateSQL = "UPDATE compromissos SET titulo = ?, descricao = ?, data = ?, hora = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, novoTitulo);
            pstmt.setString(2, novaDescricao);
            pstmt.setString(3, novaData);
            pstmt.setString(4, novaHora);
            pstmt.setString(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao atualizar compromisso: " + e.getMessage());
        }
    }

    public void deletarCompromisso(String id) {
        String deleteSQL = "DELETE FROM compromissos WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao excluir compromisso: " + e.getMessage());
        }
    }

    public void deletarTudo() {
        String deleteSQL = "DELETE FROM compromissos";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao excluir todos os compromissos: " + e.getMessage());
        }
    }
}