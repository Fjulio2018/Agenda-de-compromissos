package com.course.java.ui;

import com.course.java.controller.AgendaController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AgendaUI {
    private DefaultTableModel tableModel;
    private JTable compromissosTable;
    private JTextField tituloField, dataField, horaField;
    private JTextArea descricaoArea;
    private JFrame frame;
    private AgendaController controller;

    public AgendaUI(AgendaController controller) {
        this.controller = controller;
    }

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

    private void createFormPanel(JPanel panel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        panel.add(formPanel, BorderLayout.NORTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormField(formPanel, gbc, "Título:", tituloField = new JTextField(40), 0);
        addFormField(formPanel, gbc, "Data (DD/MM/AAAA):", dataField = new JTextField(10), 1);
        addFormField(formPanel, gbc, "Hora (HH:MM):", horaField = new JTextField(5), 2);
        addFormField(formPanel, gbc, "Descrição:", descricaoArea = new JTextArea(3, 20), 3);

        JButton adicionarButton = new JButton("Adicionar");
        JButton salvarAlteracaoButton = new JButton("Salvar Alteração");
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(adicionarButton, gbc);
        gbc.gridx = 2; formPanel.add(salvarAlteracaoButton, gbc);

        adicionarButton.addActionListener(e -> controller.adicionarCompromisso(
                tituloField.getText(), dataField.getText(), horaField.getText(), descricaoArea.getText()));
        salvarAlteracaoButton.addActionListener(e -> controller.salvarAlteracao(getSelectedRow(),
                tituloField.getText(), dataField.getText(), horaField.getText(), descricaoArea.getText()));
    }

    private void addFormField(JPanel formPanel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(label, gbc);
        gbc.gridx = 1;
        if (field instanceof JTextArea) {
            formPanel.add(new JScrollPane(field), gbc);
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
        panel.add(new JScrollPane(compromissosTable), BorderLayout.CENTER);
    }

    private void createActionButtons(JPanel panel) {
        JPanel actionPanel = new JPanel();
        panel.add(actionPanel, BorderLayout.SOUTH);
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

        visualizarButton.addActionListener(e -> controller.visualizarCompromissos());
        alterarButton.addActionListener(e -> controller.carregarCompromissoParaEdicao(getSelectedRow()));
        excluirButton.addActionListener(e -> controller.excluirCompromissos(getSelectedRows()));
        excluirTodosButton.addActionListener(e -> controller.excluirTodosCompromissos());
        encerrarButton.addActionListener(e -> frame.dispose());
    }

    // Métodos para interação com o Controller
    public void atualizarTabela(ArrayList<Object[]> compromissos) {
        tableModel.setRowCount(0);
        for (Object[] compromisso : compromissos) {
            tableModel.addRow(compromisso);
        }
    }

    public void preencherCampos(String titulo, String data, String hora, String descricao) {
        tituloField.setText(titulo);
        dataField.setText(data);
        horaField.setText(hora);
        descricaoArea.setText(descricao);
    }

    public void limparCampos() {
        tituloField.setText("");
        dataField.setText("");
        horaField.setText("");
        descricaoArea.setText("");
    }

    public void mostrarMensagem(String mensagem, String tipo) {
        int tipoMsg = tipo.equals("Erro") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(frame, mensagem, tipo, tipoMsg);
    }

    private int getSelectedRow() {
        ArrayList<Integer> selectedRows = getSelectedRows();
        return selectedRows.size() == 1 ? selectedRows.get(0) : -1;
    }

    private ArrayList<Integer> getSelectedRows() {
        ArrayList<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Boolean) tableModel.getValueAt(i, 0)) {
                selectedRows.add(i);
            }
        }
        return selectedRows;
    }
}