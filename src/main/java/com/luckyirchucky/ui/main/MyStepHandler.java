package com.luckyirchucky.ui.main;

import org.apache.commons.math3.ode.sampling.FixedStepHandler;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class MyStepHandler implements FixedStepHandler {
    DefaultCategoryDataset datasetX = (DefaultCategoryDataset) MainWindow.getChartPanelX().getChart().getCategoryPlot().getDataset();
    DefaultCategoryDataset datasetY = (DefaultCategoryDataset) MainWindow.getChartPanelY().getChart().getCategoryPlot().getDataset();

    public void init(double t0, double[] y0, double t) {
        // Initialize the datasets
        datasetX.clear();
        datasetY.clear();
    }

    public void handleStep(double t, double[] y, double[] yDot, boolean isLast) {
        // Add data points to datasets
        datasetX.addValue(y[0], "X", Double.toString(t));
        datasetY.addValue(y[1], "Y", Double.toString(t));

        // If it's the last step, update the chart
        if (isLast) {
            SwingUtilities.invokeLater(() -> {
                MainWindow.getChartPanelX().repaint();
                MainWindow.getChartPanelY().repaint();
            });
        }
    }
}
