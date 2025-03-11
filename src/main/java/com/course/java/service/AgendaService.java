package com.course.java.service;

import com.course.java.database.DataBase;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AgendaService {
    private DataBase dataBase;

    public AgendaService(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void salvarCompromisso(JSONObject compromisso) {
        dataBase.salvarCompromisso(compromisso);
    }

    public ArrayList<JSONObject> buscarCompromissos() {
        return dataBase.busqueCompromissos();
    }

    public ArrayList<Object[]> buscarCompromissosFormatados() {
        ArrayList<JSONObject> compromissos = buscarCompromissos();
        ArrayList<Object[]> formatados = new ArrayList<>();
        for (JSONObject c : compromissos) {
            formatados.add(new Object[]{false, c.getString("id"), c.getString("titulo"),
                    c.getString("data"), c.getString("hora"), c.getString("descricao")});
        }
        return formatados;
    }

    public void alterarCompromisso(String id, String titulo, String descricao, String data, String hora) {
        dataBase.alterarCompromisso(id, titulo, descricao, data, hora);
    }

    public void deletarCompromisso(String id) {
        dataBase.deletarCompromisso(id);
    }

    public void deletarTodosCompromissos() {
        dataBase.deletarTudo();
    }

    public boolean validarCampos(String titulo, String data, String hora, String descricao) {
        return !(titulo.isEmpty() || data.isEmpty() || hora.isEmpty() || descricao.isEmpty());
    }

    public boolean validarDataHora(String data, String hora) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalDate dataInserida = LocalDate.parse(data, dateFormatter);
            LocalTime horaInserida = LocalTime.parse(hora, timeFormatter);
            LocalDate hoje = LocalDate.now();
            return !(dataInserida.isBefore(hoje) || (dataInserida.equals(hoje) && horaInserida.isBefore(LocalTime.now())));
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}