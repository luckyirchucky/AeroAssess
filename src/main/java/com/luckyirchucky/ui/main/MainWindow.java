package com.luckyirchucky.ui.main;

import com.luckyirchucky.model.ResultSolution;
import com.luckyirchucky.service.localization.SingleRootFileSystemView;
import com.luckyirchucky.service.reader.JsonDataReader;
import com.luckyirchucky.service.reader.JsonReader;
import com.luckyirchucky.service.watchservice.DirectoryPreferences;
import com.luckyirchucky.service.watchservice.WatchCallable;
import com.luckyirchucky.ui.inputdata.EKF.EKFInputDataWindow;
import com.luckyirchucky.ui.loadfile.LoadFileWindow;
import com.luckyirchucky.ui.outputdata.reference.ReferenceOutputDataWindow;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private static final JsonReader<ResultSolution> JSON_DATA_READER = new JsonDataReader<>(ResultSolution.class);

    @Getter
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

    @Getter
    private static ResultSolution bufferResult = new ResultSolution();
    private Font font = new Font("System", Font.PLAIN, 12);
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

    public MainWindow() {
        super("");
        ToolTipManager.sharedInstance().setDismissDelay(60000);
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
        initializeMainWindow();
        MainWindow window = this;
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
        });
        menuFile.add(menuFileItemOpen);

        menuWorkItemEKF = new JMenuItem("Расширенный фильтр Калмана...");
        menuWorkItemEKF.setFont(font);
        menuWork.add(menuWorkItemEKF);
        menuWorkItemEKF.addActionListener(e -> new EKFInputDataWindow());

        menuWorkItemParticleFilter = new JMenuItem("Фильтр частиц...");
        menuWorkItemParticleFilter.setFont(font);
        menuWork.add(menuWorkItemParticleFilter);
        setVisibleTaskMenuItems(false);

        JMenuItem menuUploadResultSolution = new JMenuItem("Загрузить из файла");
        menuUploadResultSolution.setFont(font);
        menuResult.add(menuUploadResultSolution);
        menuUploadResultSolution.addActionListener(e -> {
            uploadResultSolutionFromFile();
        });

        JMenuItem menuResultItemToFile = new JMenuItem("Сохранить");
        menuResultItemToFile.setFont(font);
        menuResult.add(menuResultItemToFile);
        //menuResultItemToFile.addActionListener(e -> FileJSON.saveResultSolutionToFileAsJson());

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

    public ResultSolution getResultSolution() {
        return bufferResult;
    }

    private void initializeMainWindow() {
        this.setSize(1200, 600);
        this.setVisible(true);
        this.setLayout(new GridLayout(1, 1));

        JLabel resultLabel = new JLabel("Результаты расчетов");
        resultLabel.setFont(font);

        EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
        resultLabel.setBorder(emptyBorder);

        JPanel panelWithPaddingForEditButtonsPanel = new JPanel(new BorderLayout());
        panelWithPaddingForEditButtonsPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    }

    private void uploadResultSolutionFromFile() {
        // Восстановление последнего пути к файлу
        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
        openSavedFileChooser.setCurrentDirectory(new File(location));

        openSavedFileChooser.setVisible(true);
        int response = openSavedFileChooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = openSavedFileChooser.getSelectedFile();
            String selectedFileName = selectedFile.getName();
            if (!selectedFileName.toLowerCase().endsWith(".json")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
            }
            MainWindow.setFilePath(selectedFile.getParent());
            resultSolution = JSON_DATA_READER.read(selectedFile);
            file = selectedFile;
            DirectoryPreferences.FileLocation.put(openSavedFileChooser.getCurrentDirectory().getAbsolutePath());
        }
    }
}
