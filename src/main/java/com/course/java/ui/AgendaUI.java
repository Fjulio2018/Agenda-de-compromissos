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

    public AgendaUI() {
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Agenda de Compromissos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 568);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        // Formulário de Compromisso
        JPanel formPanel = new JPanel(new GridBagLayout());
        panel.add(formPanel, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tituloLabel = new JLabel("Título:");
        tituloField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(tituloLabel, gbc);
        gbc.gridx = 1; formPanel.add(tituloField, gbc);

        JLabel dataLabel = new JLabel("Data (DD/MM/AAAA):");
        dataField = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(dataLabel, gbc);
        gbc.gridx = 1; formPanel.add(dataField, gbc);

        JLabel horaLabel = new JLabel("Hora (HH:MM):");
        horaField = new JTextField(5);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(horaLabel, gbc);
        gbc.gridx = 1; formPanel.add(horaField, gbc);

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoArea = new JTextArea(3, 20);
        JScrollPane descricaoScroll = new JScrollPane(descricaoArea);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(descricaoLabel, gbc);
        gbc.gridx = 1; formPanel.add(descricaoScroll, gbc);

        JButton adicionarButton = new JButton("Adicionar");
        JButton salvarAlteracaoButton = new JButton("Salvar Alteração"); // Novo botão
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(adicionarButton, gbc);
        gbc.gridx = 2; formPanel.add(salvarAlteracaoButton, gbc); // Posicionado ao lado

        // Tabela de Compromissos com Checkbox
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

        JScrollPane tableScrollPane = new JScrollPane(compromissosTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Botões de Ação
        JPanel actionPanel = new JPanel();
        panel.add(actionPanel, BorderLayout.SOUTH);

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

        // Ação do Botão Adicionar
        adicionarButton.addActionListener(e -> {
            String titulo = tituloField.getText().trim();
            String data = dataField.getText().trim();
            String hora = horaField.getText().trim();
            String descricao = descricaoArea.getText().trim();

            if (titulo.isEmpty() || data.isEmpty() || hora.isEmpty() || descricao.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalDate dataInserida = LocalDate.parse(data, dateFormatter);
                LocalTime horaInserida = LocalTime.parse(hora, timeFormatter);
                LocalDate hoje = LocalDate.now();
                if (dataInserida.isBefore(hoje) || (dataInserida.equals(hoje) && horaInserida.isBefore(LocalTime.now()))) {
                    JOptionPane.showMessageDialog(frame, "Data ou hora no passado!", "Erro", JOptionPane.ERROR_MESSAGE);
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
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato inválido! Use DD/MM/AAAA e HH:MM.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do Botão Visualizar
        visualizarButton.addActionListener(e -> {
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
        });

        // Ação do Botão Alterar
        alterarButton.addActionListener(e -> {
            ArrayList<Integer> selectedRows = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((Boolean) tableModel.getValueAt(i, 0)) {
                    selectedRows.add(i);
                }
            }

            if (selectedRows.size() != 1) {
                JOptionPane.showMessageDialog(frame, "Selecione exatamente um compromisso para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedRow = selectedRows.get(0);
            String tituloAtual = (String) tableModel.getValueAt(selectedRow, 2);
            String dataAtual = (String) tableModel.getValueAt(selectedRow, 3);
            String horaAtual = (String) tableModel.getValueAt(selectedRow, 4);
            String descricaoAtual = (String) tableModel.getValueAt(selectedRow, 5);

            tituloField.setText(tituloAtual);
            dataField.setText(dataAtual);
            horaField.setText(horaAtual);
            descricaoArea.setText(descricaoAtual);

            JOptionPane.showMessageDialog(frame, "Edite os campos e clique em 'Salvar Alteração'.", "Editar Compromisso", JOptionPane.INFORMATION_MESSAGE);
        });

        // Ação do Botão Salvar Alteração
        salvarAlteracaoButton.addActionListener(e -> {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Nenhum compromisso selecionado para alteração. Clique em 'Alterar' primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoTitulo = tituloField.getText().trim();
            String novaData = dataField.getText().trim();
            String novaHora = horaField.getText().trim();
            String novaDescricao = descricaoArea.getText().trim();

            if (novoTitulo.isEmpty() || novaData.isEmpty() || novaHora.isEmpty() || novaDescricao.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalDate dataInserida = LocalDate.parse(novaData, dateFormatter);
                LocalTime horaInserida = LocalTime.parse(novaHora, timeFormatter);
                LocalDate hoje = LocalDate.now();
                if (dataInserida.isBefore(hoje) || (dataInserida.equals(hoje) && horaInserida.isBefore(LocalTime.now()))) {
                    JOptionPane.showMessageDialog(frame, "Data ou hora no passado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = (String) tableModel.getValueAt(selectedRow, 1);
                DatabaseHelper.alterarCompromisso(id, novoTitulo, novaDescricao, novaData, novaHora);
                tableModel.setValueAt(novoTitulo, selectedRow, 2);
                tableModel.setValueAt(novaData, selectedRow, 3);
                tableModel.setValueAt(novaHora, selectedRow, 4);
                tableModel.setValueAt(novaDescricao, selectedRow, 5);
                tableModel.setValueAt(false, selectedRow, 0); // Desmarcar o checkbox

                JOptionPane.showMessageDialog(frame, "Compromisso alterado com sucesso!");
                limparCampos();
                selectedRow = -1; // Resetar a seleção
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato inválido! Use DD/MM/AAAA e HH:MM.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do Botão Excluir
        excluirButton.addActionListener(e -> {
            ArrayList<Integer> selectedRows = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((Boolean) tableModel.getValueAt(i, 0)) {
                    selectedRows.add(i);
                }
            }

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
        });

        // Ação do Botão Excluir Todos
        excluirTodosButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Deseja excluir todos os compromissos?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.setRowCount(0);
                DatabaseHelper.deletarTudo();
                JOptionPane.showMessageDialog(frame, "Todos os compromissos foram excluídos.");
            }
        });

        // Ação do Botão Encerrar
        encerrarButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        frame.setVisible(true);
    }

    private void limparCampos() {
        tituloField.setText("");
        dataField.setText("");
        horaField.setText("");
        descricaoArea.setText("");
    }
}