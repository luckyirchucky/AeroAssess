package com.luckyirchucky.ui.inputdata.ekf;

import com.luckyirchucky.ui.constraint.NumericDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EKFInputDataWindow extends JFrame {

    private final Font font = new Font("System", Font.PLAIN, 12);
    private static final String CAPTION = "Входные данные для расширенного фильтра Калмана";
    private final JButton estimateButton = new JButton("Оценить состояние");
    private final JButton cancelButton = new JButton("Отмена");
    private final JPanel matrixPanelCovProc = new JPanel(new GridLayout(12, 12, 2, 2));
    private final JPanel matrixPanelCovSost = new JPanel(new GridLayout(12, 12, 2, 2));
    private JCheckBox isEditMatrixCovProc = new JCheckBox("Редактировать матрицу ковариации процесса");
    private JCheckBox isEditMatrixCovSost = new JCheckBox("Редактировать матрицу ковариации состояния");

    public EKFInputDataWindow() {
        super("");
        JLabel title = new JLabel(CAPTION);
        title.setFont(font);
        this.setTitle(title.getText());

        JPanel contentPanel = new JPanel(new GridLayout(7, 1));

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                JTextField covarianceProcCell = new JTextField(2);
                matrixPanelCovProc.add(covarianceProcCell);
                covarianceProcCell.setEditable(false);
            }
        }

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                JTextField covarianceSostCell = new JTextField(2);
                matrixPanelCovSost.add(covarianceSostCell);
                covarianceSostCell.setEditable(false);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(estimateButton);
        buttonPanel.add(cancelButton);

        JLabel labelCovProc = new JLabel("<html><b>Матрица ковариации процесса</b></html>");
        labelCovProc.setFont(font);
        labelCovProc.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelCovSost = new JLabel("<html><b>Матрица ковариации состояния</b></html>");
        labelCovSost.setFont(font);
        labelCovSost.setHorizontalAlignment(SwingConstants.CENTER);

        isEditMatrixCovProc.setFont(font);
        isEditMatrixCovProc.setSelected(false);
        isEditMatrixCovSost.setFont(font);
        isEditMatrixCovSost.setSelected(false);

        contentPanel.add(labelCovProc);
        contentPanel.add(new JScrollPane(matrixPanelCovProc));
        contentPanel.add(isEditMatrixCovProc);
        contentPanel.add(labelCovSost);
        contentPanel.add(new JScrollPane(matrixPanelCovSost));
        contentPanel.add(isEditMatrixCovSost);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(contentPanel);

        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        setMatrixOnlyNumbers();

        // Добавление слушателей
        listenerCloseWindow();
        listenerEditMatrixCovProc();
        listenerEditMatrixCovSost();
    }

    private void listenerCloseWindow() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(cancelButton);
                frame.dispose();
            }
        });
    }

    private void listenerEditMatrixCovProc(){
        isEditMatrixCovProc.addActionListener(e -> {
            setMatrixEditable(matrixPanelCovProc, isEditMatrixCovProc.isSelected());
        });
    }

    private void listenerEditMatrixCovSost(){
        isEditMatrixCovSost.addActionListener(e -> {
            setMatrixEditable(matrixPanelCovSost, isEditMatrixCovSost.isSelected());
        });
    }

    // Метод для установки состояния редактирования матрицы
    private void setMatrixEditable(JPanel matrixPanel, boolean editable) {
        Component[] components = matrixPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setEditable(editable);
            }
        }
    }

    private void setMatrixOnlyNumbers() {
        for (Component comp : matrixPanelCovProc.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setDocument(new NumericDocument());
            }
        }
        for (Component comp : matrixPanelCovSost.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setDocument(new NumericDocument());
            }
        }
    }
}
