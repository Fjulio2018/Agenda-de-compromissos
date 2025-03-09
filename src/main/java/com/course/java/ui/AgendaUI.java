package com.course.java.ui;

import com.course.java.helper.DatabaseHelper;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AgendaUI {
    private DefaultTableModel tableModel;
    private JTable compromissosTable;
    private JTextField tituloField, dataField, horaField;
    private JTextArea descricaoArea;
    private int selectedRow = -1; // Para rastrear a linha sendo editada
    private JFrame frame;

    public void start() {
        createUI();
    }

    private void createUI() {
        frame = new JFrame("Agenda de Compromissos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 568);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        createFormPanel(panel);
        createTable(panel);
        createActionButtons(panel);

        frame.pack();
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setVisible(true);
    }


    // Cria o formulário de compromisso
    private void createFormPanel(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        panel.add(formPanel, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(formPanel, gbc, "Título:", tituloField = new JTextField(20), 0);
        addFormField(formPanel, gbc, "Data (DD/MM/AAAA):", dataField = new JTextField(10), 1);
        addFormField(formPanel, gbc, "Hora (HH:MM):", horaField = new JTextField(5), 2);
        addFormField(formPanel, gbc, "Descrição:", descricaoArea = new JTextArea(3, 20), 3);

        JButton adicionarButton = new JButton("Adicionar");
        JButton salvarAlteracaoButton = new JButton("Salvar Alteração");
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(adicionarButton, gbc);
        gbc.gridx = 2; formPanel.add(salvarAlteracaoButton, gbc);

        adicionarButton.addActionListener(e -> adicionarCompromisso());
        salvarAlteracaoButton.addActionListener(e -> salvarAlteracao());
    }

    // Adiciona um campo ao formulário
    private void addFormField(JPanel formPanel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(label, gbc);
        gbc.gridx = 1;
        if (field instanceof JTextArea) {
            JScrollPane scrollPane = new JScrollPane(field);
            formPanel.add(scrollPane, gbc);
        } else {
            formPanel.add(field, gbc);
        }
    }

    private void createTable(JPanel panel) {
        String[] colunas = {"Selecionar", "ID", "Título", "Data", "Hora", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };
        compromissosTable = new JTable(tableModel);
        compromissosTable.setFillsViewportHeight(true);
        compromissosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Habilitar ordenação automática nas colunas da tabela
        compromissosTable.setAutoCreateRowSorter(true);

        JScrollPane tableScrollPane = new JScrollPane(compromissosTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
    }


    private void createActionButtons(JPanel panel) {
        JPanel actionPanel = new JPanel();
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Adiciona uma borda com título "Ações" no painel de ações
        actionPanel.setBorder(BorderFactory.createTitledBorder("Ações"));

        JButton visualizarButton = new JButton("Visualizar");
        JButton alterarButton = new JButton("Alterar");
        JButton excluirButton = new JButton("Excluir");
        JButton excluirTodosButton = new JButton("Excluir Todos");
        JButton encerrarButton = new JButton("Encerrar");

        actionPanel.add(visualizarButton);
        actionPanel.add(alterarButton);
        actionPanel.add(excluirButton);
        actionPanel.add(excluirTodosButton);
        actionPanel.add(encerrarButton);

        visualizarButton.addActionListener(e -> visualizarCompromissos());
        alterarButton.addActionListener(e -> alterarCompromisso());
        excluirButton.addActionListener(e -> excluirCompromissos());
        excluirTodosButton.addActionListener(e -> excluirTodosCompromissos());
        encerrarButton.addActionListener(e -> encerrarPrograma());
    }


    // Adiciona um novo compromisso
    private void adicionarCompromisso() {
        String titulo = tituloField.getText().trim();
        String data = dataField.getText().trim();
        String hora = horaField.getText().trim();
        String descricao = descricaoArea.getText().trim();

        if (!validarCampos(titulo, data, hora, descricao)) {
            return;
        }

        if (!validarDataHora(data, hora)) {
            return;
        }

        JSONObject compromisso = new JSONObject();
        compromisso.put("titulo", titulo);
        compromisso.put("data", data);
        compromisso.put("hora", hora);
        compromisso.put("descricao", descricao);

        ArrayList<JSONObject> listaCompromissos = new ArrayList<>();
        listaCompromissos.add(compromisso);
        DatabaseHelper.salvarCompromissos(listaCompromissos);

        tableModel.addRow(new Object[]{false, "", titulo, data, hora, descricao});
        JOptionPane.showMessageDialog(frame, "Compromisso adicionado com sucesso!");
        limparCampos();
    }

    // Valida se os campos estão preenchidos
    private boolean validarCampos(String titulo, String data, String hora, String descricao) {
        if (titulo.isEmpty() || data.isEmpty() || hora.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Valida data e hora
    private boolean validarDataHora(String data, String hora) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalDate dataInserida = LocalDate.parse(data, dateFormatter);
            LocalTime horaInserida = LocalTime.parse(hora, timeFormatter);
            LocalDate hoje = LocalDate.now();

            if (dataInserida.isBefore(hoje) || (dataInserida.equals(hoje) && horaInserida.isBefore(LocalTime.now()))) {
                JOptionPane.showMessageDialog(frame, "Data ou hora no passado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Formato inválido! Use DD/MM/AAAA e HH:MM.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Limpa os campos do formulário
    private void limparCampos() {
        tituloField.setText("");
        dataField.setText("");
        horaField.setText("");
        descricaoArea.setText("");
    }

    // Visualiza todos os compromissos
    private void visualizarCompromissos() {
        tableModel.setRowCount(0);
        ArrayList<JSONObject> compromissos = DatabaseHelper.busqueCompromissos();
        for (JSONObject compromisso : compromissos) {
            tableModel.addRow(new Object[]{
                    false,
                    compromisso.getString("id"),
                    compromisso.getString("titulo"),
                    compromisso.getString("data"),
                    compromisso.getString("hora"),
                    compromisso.getString("descricao")
            });
        }
    }

    // Altera um compromisso existente
    private void alterarCompromisso() {
        ArrayList<Integer> selectedRows = getSelectedRows();
        if (selectedRows.size() != 1) {
            JOptionPane.showMessageDialog(frame, "Selecione exatamente um compromisso para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedRow = selectedRows.get(0);
        carregarCompromissoParaEdicao(selectedRow);
    }

    // Salva as alterações de um compromisso
    private void salvarAlteracao() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Nenhum compromisso selecionado para alteração. Clique em 'Alterar' primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novoTitulo = tituloField.getText().trim();
        String novaData = dataField.getText().trim();
        String novaHora = horaField.getText().trim();
        String novaDescricao = descricaoArea.getText().trim();

        if (!validarCampos(novoTitulo, novaData, novaHora, novaDescricao)) {
            return;
        }

        if (!validarDataHora(novaData, novaHora)) {
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 1);
        DatabaseHelper.alterarCompromisso(id, novoTitulo, novaDescricao, novaData, novaHora);
        atualizarTabelaAposEdicao(selectedRow, novoTitulo, novaData, novaHora, novaDescricao);
        limparCampos();
        selectedRow = -1;
    }

    // Exclui compromissos selecionados
    private void excluirCompromissos() {
        ArrayList<Integer> selectedRows = getSelectedRows();
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Selecione pelo menos um compromisso para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Deseja excluir os compromissos selecionados?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = selectedRows.size() - 1; i >= 0; i--) {
                int row = selectedRows.get(i);
                String id = (String) tableModel.getValueAt(row, 1);
                if (id != null && !id.isEmpty()) {
                    DatabaseHelper.deletarCompromisso(id);
                }
                tableModel.removeRow(row);
            }
            JOptionPane.showMessageDialog(frame, "Compromissos excluídos com sucesso!");
        }
    }

    // Exclui todos os compromissos
    private void excluirTodosCompromissos() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Deseja excluir todos os compromissos?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            DatabaseHelper.deletarTudo();
            JOptionPane.showMessageDialog(frame, "Todos os compromissos foram excluídos.");
        }
    }

    // Encerra o programa
    private void encerrarPrograma() {
        frame.dispose();
        System.exit(0);
    }

    // Retorna as linhas selecionadas na tabela
    private ArrayList<Integer> getSelectedRows() {
        ArrayList<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 0)) {
                selectedRows.add(i);
            }
        }
        return selectedRows;
    }

    // Carrega um compromisso para edição
    private void carregarCompromissoParaEdicao(int row) {
        String tituloAtual = (String) tableModel.getValueAt(row, 2);
        String dataAtual = (String) tableModel.getValueAt(row, 3);
        String horaAtual = (String) tableModel.getValueAt(row, 4);
        String descricaoAtual = (String) tableModel.getValueAt(row, 5);

        tituloField.setText(tituloAtual);
        dataField.setText(dataAtual);
        horaField.setText(horaAtual);
        descricaoArea.setText(descricaoAtual);

        JOptionPane.showMessageDialog(frame, "Edite os campos e clique em 'Salvar Alteração'.", "Editar Compromisso", JOptionPane.INFORMATION_MESSAGE);
    }

    // Atualiza a tabela após edição
    private void atualizarTabelaAposEdicao(int row, String novoTitulo, String novaData, String novaHora, String novaDescricao) {
        tableModel.setValueAt(novoTitulo, row, 2);
        tableModel.setValueAt(novaData, row, 3);
        tableModel.setValueAt(novaHora, row, 4);
        tableModel.setValueAt(novaDescricao, row, 5);
        tableModel.setValueAt(false, row, 0); // Desmarcar o checkbox
        JOptionPane.showMessageDialog(frame, "Compromisso alterado com sucesso!");
    }
}