package com.luckyirchucky.ui.main;

import com.luckyirchucky.model.ResultSolution;
import com.luckyirchucky.service.localization.SingleRootFileSystemView;
import com.luckyirchucky.service.reader.JsonDataReader;
import com.luckyirchucky.service.reader.JsonReader;
import com.luckyirchucky.service.watchservice.DirectoryPreferences;
import com.luckyirchucky.service.watchservice.WatchCallable;
import com.luckyirchucky.ui.inputdata.ekf.EKFInputDataWindow;
import com.luckyirchucky.ui.inputdata.particlefilter.ParticleFilterInputDataWindow;
import com.luckyirchucky.ui.loadfile.LoadFileWindow;
import com.luckyirchucky.ui.loadfile.LoadResultFileWindow;
import com.luckyirchucky.ui.outputdata.reference.ReferenceOutputDataWindow;
import com.luckyirchucky.ui.outputdata.turbulence.TurbulenceOutputDataWindow;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.luckyirchucky.service.localization.LocalizeUIJFileChooser.setUpdateUIJFileChooser;

/**
 * Класс главной панели
 */
public class MainWindow extends JFrame implements IMainWindow {

    @Getter
    @Setter
    private static String EKFInputParamsFromFile;

    @Getter
    private static final JsonReader<ResultSolution> JSON_DATA_READER = new JsonDataReader<>(ResultSolution.class);

    @Getter
    @Setter
    private static ResultSolution resultSolution = new ResultSolution();

    private static final String CAPTION = "Оценка состояния объекта";

    private final JButton EKFOpenButton = new JButton("Расширенный фильтр Калмана");
    private final JButton particleFilterOpenButton = new JButton("Фильтр частиц");

    @Getter
    @Setter
    private static String fileName = "";

    @Getter
    @Setter
    private static String filePath = "";

    @Getter
    private File file;

    private JMenuItem menuWorkItemEKF;
    private JMenuItem menuWorkItemParticleFilter;

    private final Font font = new Font("System", Font.PLAIN, 12);
    private static File rootPath = new File(System.getProperty("user.home"));
    private static FileSystemView fileSystemView = new SingleRootFileSystemView(rootPath);

    @Getter
    private static JFileChooser openNewFileChooser = new JFileChooser(fileSystemView) {
        @Override
        public JDialog createDialog(Component parent) {
            JDialog dialog = super.createDialog(parent);
            dialog.setMinimumSize(new Dimension(600, 400));
            return dialog;
        }
    };

    @Getter
    private static JFileChooser openSavedFileChooser = new JFileChooser(fileSystemView) {
        @Override
        public JDialog createDialog(Component parent) {
            JDialog dialog = super.createDialog(parent);
            dialog.setMinimumSize(new Dimension(600, 400));
            return dialog;
        }
    };

    @Getter
    private static ChartPanel chartPanelX;
    @Getter
    private static ChartPanel chartPanelY;

    // Инициализация переменных для хранения извлеченных значений
    private static double Ft = 0, V_wind = 0, alpha1 = 0, alpha2 = 0, S_section = 0, rho = 0, m = 0, M = 0, Cy = 0, A = 0, B = 0, C = 0, d = 0, CC = 0, S = 0;
    private static double t = 0, dt = 0, total_time = 0;
    private static double initial_x = 0, initial_y = 0, initial_z = 0, initial_x_prime = 0, initial_y_prime = 0, initial_z_prime = 0;
    private static double initial_alpha = 0, initial_beta = 0, initial_gamma = 0, initial_alpha_prime = 0, initial_beta_prime = 0, initial_gamma_prime = 0;
    private String dx_dt_equation;
    private String dy_dt_equation;
    private String dz_dt_equation;
    private String dAlpha_dt_equation;
    private String dBeta_dt_equation;
    private String dGamma_dt_equation;

    private JPanel contentPane = new JPanel(new GridLayout(3, 2));
    private final JCheckBox checkBoxX = new JCheckBox("Скрыть график X");
    private final JCheckBox checkBoxY = new JCheckBox("Скрыть график Y");
    @Getter
    private static final JCheckBox checkBoxTurbulentX = new JCheckBox("Скрыть график с турбулентностью X");
    @Getter
    private static final JCheckBox checkBoxTurbulentY = new JCheckBox("Скрыть график с турбулентностью Y");
    @Getter
    private static final JButton buttonTurbulent = new JButton("Добавить турбулентность");

    @Getter
    private final static int numPoints = 1000;
    @Getter
    private static double[] xValues = new double[numPoints];
    @Getter
    private static double[] yValues = new double[numPoints];

    public MainWindow() {
        super("");
        JLabel title = new JLabel(CAPTION);
        title.setFont(font);
        this.setTitle(String.valueOf(title.getText()));
        EKFOpenButton.setFont(font);
        particleFilterOpenButton.setFont(font);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new WatchCallable(openNewFileChooser));
        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
        openNewFileChooser.setCurrentDirectory(new File(location));
        openNewFileChooser.setAcceptAllFileFilterUsed(false);
        openNewFileChooser.addActionListener(e -> {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                File selectedFile = openNewFileChooser.getSelectedFile();
                if (selectedFile.getName().equals("CON") || selectedFile.getName().equals("PRN") ||
                        selectedFile.getName().equals("AUX") || selectedFile.getName().equals("NUL") ||
                        selectedFile.getName().equals("COM2") || selectedFile.getName().equals("LPT9") ||
                        selectedFile.getName().equals("COM3") || selectedFile.getName().equals("COM4") ||
                        selectedFile.getName().equals("COM5") || selectedFile.getName().equals("COM6") ||
                        selectedFile.getName().equals("COM7") || selectedFile.getName().equals("COM8") ||
                        selectedFile.getName().equals("COM9") || selectedFile.getName().equals("LPT8") ||
                        selectedFile.getName().equals("LPT1") || selectedFile.getName().equals("LPT7") ||
                        selectedFile.getName().equals("LPT2") || selectedFile.getName().equals("LPT3") ||
                        selectedFile.getName().equals("LPT4") || selectedFile.getName().equals("LPT5") ||
                        selectedFile.getName().equals("LPT6")) {
                    JOptionPane.showMessageDialog(null, "<html><span style='font-family: System; font-size: 12pt; "
                            + "font-weight: normal;'>" + "Невозможно создать файл. " +
                            "Использовано зарезервированное имя." + "</span></html>", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else if (selectedFile.getName().contains("*") || selectedFile.getName().contains("?") ||
                        selectedFile.getName().contains(":") || selectedFile.getName().contains("/") ||
                        selectedFile.getName().contains("|") || selectedFile.getName().contains("\\") ||
                        selectedFile.getName().contains("\"") || selectedFile.getName().contains("<") ||
                        selectedFile.getName().contains(">")) {
                    JOptionPane.showMessageDialog(null, "<html><span style='font-family: System; font-size: 12pt; "
                                    + "font-weight: normal;'>" + "Невозможно создать файл." + "</span></html>",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setUpdateUIJFileChooser(false, openNewFileChooser);
        setUpdateUIJFileChooser(true, openSavedFileChooser);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeMenu();
        this.add(contentPane);
        this.setSize(1200, 800);
        this.setVisible(true);

        // Добавляем слушатели
        checkBoxX.addItemListener(e -> toggleChartXVisibility(checkBoxX.isSelected()));
        checkBoxY.addItemListener(e -> toggleChartYVisibility(checkBoxY.isSelected()));
        checkBoxTurbulentX.addItemListener(e -> toggleChartXTurbulentVisibility(checkBoxTurbulentX.isSelected()));
        checkBoxTurbulentY.addItemListener(e -> toggleChartYTurbulentVisibility(checkBoxTurbulentY.isSelected()));
        buttonTurbulent.addActionListener(e -> {
            new TurbulenceOutputDataWindow();
            JPanel checkboxTurbulentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            checkboxTurbulentPanel.add(checkBoxTurbulentX);
            checkboxTurbulentPanel.add(checkBoxTurbulentY);
            contentPane.add(checkboxTurbulentPanel, BorderLayout.NORTH);
        });
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("Файл");
        menuFile.setFont(font);
        menuBar.add(menuFile);

        JMenu menuWork = new JMenu("Оценка состояния");
        menuWork.setFont(font);
        menuBar.add(menuWork);

        JMenu menuResult = new JMenu("Результаты");
        menuResult.setFont(font);
        menuBar.add(menuResult);

        JMenu menuReference = new JMenu("Справка");
        menuReference.setFont(font);
        menuBar.add(menuReference);

        JButton exitButton = new JButton("Выход");
        exitButton.setFont(font);
        exitButton.addActionListener(e -> System.exit(0));
        menuBar.add(exitButton);

        JMenuItem menuFileItemOpen = new JMenuItem("Открыть");
        menuFileItemOpen.setFont(font);
        menuFileItemOpen.addActionListener(e -> {
            new LoadFileWindow(this, openNewFileChooser);
            // Извлечение параметров и уравнений
            // Отрисовка графиков
            if (EKFInputParamsFromFile != null) {
                extractAndSetParams();
                extractAndSetEquations();
                resultSolution.setEKFInputParamsFromFile(EKFInputParamsFromFile);
                computeMotionTrajectories(dx_dt_equation, dy_dt_equation);
                // Добавляем чекбоксы на панель
                JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                checkboxPanel.add(checkBoxX);
                checkboxPanel.add(checkBoxY);
                contentPane.add(checkboxPanel, BorderLayout.NORTH);
                JPanel turbulentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                turbulentPanel.add(buttonTurbulent);
                contentPane.add(turbulentPanel);
            }
        });
        menuFile.add(menuFileItemOpen);

        menuWorkItemEKF = new JMenuItem("Расширенный фильтр Калмана...");
        menuWorkItemEKF.setFont(font);
        menuWork.add(menuWorkItemEKF);
        menuWorkItemEKF.addActionListener(e -> new EKFInputDataWindow());
        menuWork.addSeparator();
        menuWorkItemParticleFilter = new JMenuItem("Фильтр частиц...");
        menuWorkItemParticleFilter.setFont(font);
        menuWorkItemEKF.addActionListener(e -> new ParticleFilterInputDataWindow());
        menuWork.add(menuWorkItemParticleFilter);
        setVisibleTaskMenuItems(false);

        JMenuItem menuUploadResultSolution = new JMenuItem("Загрузить из файла");
        menuUploadResultSolution.setFont(font);
        menuResult.add(menuUploadResultSolution);
        menuUploadResultSolution.addActionListener(e -> new LoadResultFileWindow(this, openSavedFileChooser));
        menuResult.addSeparator();
        JMenuItem menuResultItemToFile = new JMenuItem("Сохранить");
        menuResultItemToFile.setFont(font);
        menuResult.add(menuResultItemToFile);
        menuResultItemToFile.addActionListener(e -> FileJSON.saveResultSolutionToFileAsJson());

        JMenuItem aboutItem = new JMenuItem("О программе...");
        aboutItem.setFont(font);
        aboutItem.addActionListener(e -> new ReferenceOutputDataWindow());
        menuReference.add(aboutItem);

        this.setJMenuBar(menuBar);
    }

    public void setVisibleTaskMenuItems(boolean isEnabled) {
        menuWorkItemEKF.setEnabled(isEnabled);
        menuWorkItemParticleFilter.setEnabled(isEnabled);
    }

    public void setFile(File file) {
        this.file = file;
        this.setTitle(rewriteTitle(file.getName()));
    }

    private String rewriteTitle(String fileName) {
        return CAPTION + " - " + fileName;
    }

    public void extractAndSetParams() {
        // Парсинг строки для извлечения параметров и значений
        String[] params = EKFInputParamsFromFile.split(",");

        // Парсинг и извлечение параметров и значений из массива строк params
        for (String param : params) {
            if (param.startsWith("Ft")) {
                Ft = Double.parseDouble(param.split(" = ")[1]);
            } else if (param.startsWith("V_wind")) {
                V_wind = Double.parseDouble(param.split(" = ")[1]);
            } else if (param.startsWith("alpha1")){
                alpha1 = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("alpha2")){
                alpha2 = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("S_section")){
                S_section = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("rho")){
                rho = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("m")){
                m = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("M")){
                M = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("Cy")){
                Cy = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("A")){
                A = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("B")){
                B = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("C")){
                C = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("d")){
                d = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("CC")){
                CC = Double.parseDouble(param.split("=")[1]);
            } else if (param.startsWith("S")){
                S = Double.parseDouble(param.split("=")[1]);
            }

            // Обработка начальных параметров
            else if (param.startsWith("t")) {
                t = Double.parseDouble(param.split(" = ")[1]);
            } else if (param.startsWith("dt")) {
                dt = Double.parseDouble(param.split(" = ")[1]);
            } else if (param.startsWith("total_time")) {
                total_time = Double.parseDouble(param.split(" = ")[1]);
            }

            // Обработка начального состояния
            else if (param.startsWith("initialState")) {
                // последний элемент массива params содержит состояния
                String[] initialStateValues = params[params.length - 1].split(", ");
                initial_x = Double.parseDouble(initialStateValues[1]);
                initial_y = Double.parseDouble(initialStateValues[3]);
                initial_z = Double.parseDouble(initialStateValues[5]);
                initial_x_prime = Double.parseDouble(initialStateValues[7]);
                initial_y_prime = Double.parseDouble(initialStateValues[9]);
                initial_z_prime = Double.parseDouble(initialStateValues[11]);
                initial_alpha = Double.parseDouble(initialStateValues[13]);
                initial_beta = Double.parseDouble(initialStateValues[15]);
                initial_gamma = Double.parseDouble(initialStateValues[17]);
                initial_alpha_prime = Double.parseDouble(initialStateValues[19]);
                initial_beta_prime = Double.parseDouble(initialStateValues[21]);
                initial_gamma_prime = Double.parseDouble(initialStateValues[23]);
            }
        }
    }

    public void extractAndSetEquations() {
        // Разбиваем строку на уравнения
        String[] equationsArray = EKFInputParamsFromFile.split(",,");

        for (String equationText : equationsArray) {
            String[] equationParts = equationText.split(" = ");
            if (equationParts.length == 2) {
                String equationName = equationParts[0].trim();
                String equationValue = equationParts[1].trim();
                processEquation(equationName, equationValue);
            }
        }
    }

    public void processEquation(String equationName, String equationValue) {
        switch (equationName) {
            case "dx/dt":
                dx_dt_equation = equationValue;
                break;
            case "dy/dt":
                dy_dt_equation = equationValue;
                break;
            case "dz/dt":
                dz_dt_equation = equationValue;
                break;
            case "d(alpha)/dt":
                dAlpha_dt_equation = equationValue;
                break;
            case "d(beta)/dt":
                dBeta_dt_equation = equationValue;
                break;
            case "d(gamma)/dt":
                dGamma_dt_equation = equationValue;
                break;
            default:
                // Обработка других уравнений, если необходимо
                break;
        }
    }

    private void computeMotionTrajectories(String dxEquation, String dyEquation) {
        double[] timePoints = new double[numPoints];

        double time = t;
        double dt = 0.1; // Предположим значение временного шага

        for (int i = 0; i < numPoints; i++) {
            timePoints[i] = time;
            // Уравнение для равномерного движения по x (примерное значение)
            xValues[i] = 0.5 * Math.pow(time, 2);
            // Константное значение для y
            yValues[i] = 10000;
            time += dt;
        }

        // Строим графики
        plotMotionGraphs(timePoints, xValues, yValues);
    }

    private void plotMotionGraphs(double[] timePoints, double[] xValues, double[] yValues) {
        DefaultCategoryDataset datasetX = new DefaultCategoryDataset();
        DefaultCategoryDataset datasetY = new DefaultCategoryDataset();

        for (int i = 0; i < timePoints.length; i++) {
            datasetX.addValue(xValues[i], "X", String.valueOf(timePoints[i]));
            datasetY.addValue(yValues[i], "Y", String.valueOf(timePoints[i]));
        }

        JFreeChart chartX = ChartFactory.createLineChart("Траектория движения по оси X", "t",
                "X", datasetX, PlotOrientation.VERTICAL, false, true, false);
        JFreeChart chartY = ChartFactory.createLineChart("Траектория движения по оси Y", "t",
                "Y", datasetY, PlotOrientation.VERTICAL, false, true, false);

        chartPanelX = new ChartPanel(chartX);
        chartPanelY = new ChartPanel(chartY);

        contentPane.add(chartPanelX);
        contentPane.add(chartPanelY);

        setContentPane(contentPane);
    }

    // Включение/отключение графика по оси X
    public void toggleChartXVisibility(boolean isVisible) {
        CategoryPlot plotX = (CategoryPlot) chartPanelX.getChart().getPlot();
        plotX.getRenderer().setSeriesVisible(0, !isVisible);
    }

    // Включение/отключение графика по оси Y
    public void toggleChartYVisibility(boolean isVisible) {
        CategoryPlot plotY = (CategoryPlot) chartPanelY.getChart().getPlot();
        plotY.getRenderer().setSeriesVisible(0, !isVisible);
    }

    public void toggleChartXTurbulentVisibility(boolean isVisible) {
        CategoryPlot plotX = (CategoryPlot) chartPanelX.getChart().getPlot();
        plotX.getRenderer().setSeriesVisible(1, !isVisible);
    }

    // Включение/отключение графика по оси Y
    public void toggleChartYTurbulentVisibility(boolean isVisible) {
        CategoryPlot plotY = (CategoryPlot) chartPanelY.getChart().getPlot();
        plotY.getRenderer().setSeriesVisible(1, !isVisible);
    }

    private static double[] generateTurbulentValues(double[] originalValues) {
        double[] turbulentValues = new double[originalValues.length];
        for (int i = 0; i < turbulentValues.length; i++) {
            turbulentValues[i] = originalValues[i] + (Math.random() - 0.5) * 10;
        }
        return turbulentValues;
    }

    public static void addTurbulentMotionTrajectories() {
        // Генерация шумовых значений для графика X
        double[] turbulentXValues = generateTurbulentValues(xValues);
        for (int i = 0; i < 1000; i++) {
            ((DefaultCategoryDataset) chartPanelX.getChart().getCategoryPlot().getDataset()).addValue(turbulentXValues[i], "TurbulentX", String.valueOf(i));
        }

        // Генерация шумовых значений для графика Y
        double[] turbulentYValues = generateTurbulentValues(yValues);
        for (int i = 0; i < 1000; i++) {
            ((DefaultCategoryDataset) chartPanelY.getChart().getCategoryPlot().getDataset()).addValue(turbulentYValues[i], "TurbulentY", String.valueOf(i));
        }

        // Обновление отображения графиков
        chartPanelX.repaint();
        chartPanelY.repaint();
    }

}
