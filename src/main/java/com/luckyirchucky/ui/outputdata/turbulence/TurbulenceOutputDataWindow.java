package com.luckyirchucky.ui.outputdata.turbulence;

import com.luckyirchucky.ui.main.MainWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class TurbulenceOutputDataWindow extends JFrame {

    private static ChartPanel chartPanelX;
    private static ChartPanel chartPanelY;
    private JPanel contentPaneTurbulence = new JPanel(new GridLayout(2, 2));

    public TurbulenceOutputDataWindow(){
        DefaultCategoryDataset datasetX = new DefaultCategoryDataset();
        DefaultCategoryDataset datasetY = new DefaultCategoryDataset();

        JFreeChart chartX = ChartFactory.createLineChart("Траектория движения в зоне турбулентности по оси X", "t",
                "X", datasetX, PlotOrientation.VERTICAL, false, true, false);
        JFreeChart chartY = ChartFactory.createLineChart("Траектория движения в зоне турбулентности по оси Y", "t",
                "Y", datasetY, PlotOrientation.VERTICAL, false, true, false);

        chartPanelX = new ChartPanel(chartX);
        chartPanelY = new ChartPanel(chartY);

        JButton acceptButton = new JButton("Принять");
        JButton cancelButton = new JButton("Отмена");

        acceptButton.addActionListener(e -> {
            MainWindow.addTurbulentMotionTrajectories();
            MainWindow.getButtonTurbulent().setVisible(false);
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        // Панель для размещения кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        contentPaneTurbulence.add(chartPanelX);
        contentPaneTurbulence.add(chartPanelY);
        contentPaneTurbulence.add(buttonPanel);
        this.add(contentPaneTurbulence);

        addTurbulentMotionTrajectories();

        this.setSize(800, 600);
        this.setVisible(true);
    }

    private static double[] generateTurbulentValues(double[] originalValues) {
        double[] turbulentValues = new double[originalValues.length];
        for (int i = 0; i < turbulentValues.length; i++) {
            turbulentValues[i] = originalValues[i] + (Math.random() - 0.5) * 10;
        }
        return turbulentValues;
    }

    private static void addTurbulentMotionTrajectories() {
        // Генерация шумовых значений для графика X
        double[] turbulentXValues = generateTurbulentValues(MainWindow.getXValues());
        for (int i = 0; i < 1000; i++) {
            ((DefaultCategoryDataset) chartPanelX.getChart().getCategoryPlot().getDataset()).addValue(turbulentXValues[i], "TurbulentX", String.valueOf(i));
        }

        // Генерация шумовых значений для графика Y
        double[] turbulentYValues = generateTurbulentValues(MainWindow.getYValues());
        for (int i = 0; i < 1000; i++) {
            ((DefaultCategoryDataset) chartPanelY.getChart().getCategoryPlot().getDataset()).addValue(turbulentYValues[i], "TurbulentY", String.valueOf(i));
        }

        // Обновление отображения графиков
        chartPanelX.repaint();
        chartPanelY.repaint();
    }
}
