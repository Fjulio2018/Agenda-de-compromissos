package com.course.java.controller;

import com.course.java.service.AgendaService;
import com.course.java.ui.AgendaUI;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgendaController {
    private AgendaUI ui;
    private AgendaService service;

    public AgendaController(AgendaService service) {
        this.service = service;
        this.ui = new AgendaUI(this);
    }

    public void iniciar() {
        ui.start();
    }

    public void adicionarCompromisso(String titulo, String data, String hora, String descricao) {
        if (!service.validarCampos(titulo, data, hora, descricao)) {
            ui.mostrarMensagem("Preencha todos os campos corretamente.", "Erro");
            return;
        }
        if (!service.validarDataHora(data, hora)) {
            ui.mostrarMensagem("A data ou hora informada é anterior ao momento atual. Corrija para uma data/hora futura.", "Erro");
            return;
        }
        JSONObject compromisso = new JSONObject();
        compromisso.put("titulo", titulo);
        compromisso.put("data", data);
        compromisso.put("hora", hora);
        compromisso.put("descricao", descricao);
        service.salvarCompromisso(compromisso);
        ui.atualizarTabela(service.buscarCompromissosFormatados());
        ui.limparCampos();
        ui.mostrarMensagem("Compromisso adicionado com sucesso!", "Info");
    }

    public void visualizarCompromissos() {
        ui.atualizarTabela(service.buscarCompromissosFormatados());
    }

    public void carregarCompromissoParaEdicao(int row) {
        if (row == -1) {
            ui.mostrarMensagem("Selecione exatamente um compromisso para alterar.", "Erro");
            return;
        }
        ArrayList<Object[]> compromissos = service.buscarCompromissosFormatados();
        Object[] compromisso = compromissos.get(row);
        ui.preencherCampos((String) compromisso[2], (String) compromisso[3], (String) compromisso[4], (String) compromisso[5]);
        ui.mostrarMensagem("Edite os campos e clique em 'Salvar Alteração'.", "Info");
    }

    public void salvarAlteracao(int row, String titulo, String data, String hora, String descricao) {
        if (row == -1) {
            ui.mostrarMensagem("Nenhum compromisso selecionado para alteração.", "Erro");
            return;
        }
        if (service.validarCampos(titulo, data, hora, descricao) && service.validarDataHora(data, hora)) {
            ArrayList<Object[]> compromissos = service.buscarCompromissosFormatados();
            String id = (String) compromissos.get(row)[1];
            service.alterarCompromisso(id, titulo, descricao, data, hora);
            ui.atualizarTabela(service.buscarCompromissosFormatados());
            ui.limparCampos();
            ui.mostrarMensagem("Compromisso alterado com sucesso!", "Info");
        }
    }

    public void excluirCompromissos(ArrayList<Integer> rows) {
        if (rows.isEmpty()) {
            ui.mostrarMensagem("Selecione pelo menos um compromisso para excluir.", "Erro");
            return;
        }
        ArrayList<Object[]> compromissos = service.buscarCompromissosFormatados();
        for (int i = rows.size() - 1; i >= 0; i--) {
            String id = (String) compromissos.get(rows.get(i))[1];
            service.deletarCompromisso(id);
        }
        ui.atualizarTabela(service.buscarCompromissosFormatados());
        ui.mostrarMensagem("Compromissos excluídos com sucesso!", "Info");
    }

    public void excluirTodosCompromissos() {
        service.deletarTodosCompromissos();
        ui.atualizarTabela(service.buscarCompromissosFormatados());
        ui.mostrarMensagem("Todos os compromissos foram excluídos.", "Info");
    }
}