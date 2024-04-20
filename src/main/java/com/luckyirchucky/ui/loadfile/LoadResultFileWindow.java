package com.luckyirchucky.ui.loadfile;

import com.luckyirchucky.service.loader.DataLoader;
import com.luckyirchucky.service.loader.TXTDataLoader;
import com.luckyirchucky.service.localization.SingleRootFileSystemView;
import com.luckyirchucky.service.watchservice.DirectoryPreferences;
import com.luckyirchucky.ui.main.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.List;

import static com.luckyirchucky.service.localization.LocalizeUIJFileChooser.setUpdateUIJFileChooser;
import static com.luckyirchucky.ui.main.MainWindow.*;

/**
 * Окно отображения загруженных данных
 */
public class LoadResultFileWindow extends JDialog {
    private final DataLoader txtDataLoader = new TXTDataLoader();
    private final MainWindow mainWindow;
    private File selectedFile;
    private List<String> dataFromFile;
    private Font font = new Font("System", Font.PLAIN, 12);
    JTextField filePath = new JTextField(30);
    JTextArea textAreaFileContent = new JTextArea();
    Boolean isFileOpened = false;
    File rootPath = new File(System.getProperty("user.home"));
    FileSystemView fileSystemView = new SingleRootFileSystemView(rootPath);
    JFileChooser fileChooser = new JFileChooser(fileSystemView) {
        @Override
        public JDialog createDialog(Component parent) {
            JDialog dialog = super.createDialog(parent);
            dialog.setMinimumSize(new Dimension(600, 400));
            return dialog;
        }
    };
    JFileChooser mainJFileChooser;

    public LoadResultFileWindow(MainWindow mainWindow, JFileChooser mainJFileChooser) {
        this.mainJFileChooser = mainJFileChooser;

        fileChooser.addPropertyChangeListener(JFileChooser.DIRECTORY_CHANGED_PROPERTY, evt -> {
            if (evt.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                mainJFileChooser.setCurrentDirectory(new File(fileChooser.getCurrentDirectory().getAbsolutePath()));
            }
        });

        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new File(location));

        fileChooser.setAcceptAllFileFilterUsed(false);

        // Русификация
        setUpdateUIJFileChooser(false, fileChooser);

        this.mainWindow = mainWindow;
        this.setTitle("Загрузка данных");
        this.setSize(800, 800);
        openFileWindow();
        if (isFileOpened) {
            initialize();
            this.setModal(true);
            this.setVisible(true);
        }
    }

    /**
     * Инициализация интерфейса
     */
    public void initialize() {
        this.setMinimumSize(new Dimension(400, 500));

        JPanel loadFilePanel = new JPanel(new MigLayout("", "[][grow][]", ""));

        JLabel fileLabel = new JLabel("Файл");
        fileLabel.setFont(font);
        loadFilePanel.add(fileLabel, "width 100::");

        filePath.setEditable(false);
        loadFilePanel.add(filePath, "growx");

        JButton chooseFileButton = new JButton("Выбрать");
        chooseFileButton.setFont(font);
        loadFilePanel.add(chooseFileButton, "width 100::");

        JPanel dataPanel = new JPanel(new MigLayout("", "[grow]", "[grow]"));
        dataPanel.setBorder(BorderFactory.createTitledBorder(null, "Содержимое",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, font));

        JScrollPane scrollPane = new JScrollPane(textAreaFileContent);
        dataPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        dataPanel.add(scrollPane, "grow, width 150:500:, height 150:300:");

        JPanel buttonsPanel = new JPanel(new MigLayout("wrap, align right"));
        JButton uploadButton = new JButton("Загрузить");
        uploadButton.setFont(font);
        JButton cancelButton = new JButton("Отменить");
        cancelButton.setFont(font);
        buttonsPanel.add(uploadButton, "cell 0 0");
        buttonsPanel.add(cancelButton, "cell 1 0");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("wrap 1", "[grow]", "[][][grow][][]"));

        mainPanel.add(loadFilePanel, "growx");
        mainPanel.add(dataPanel, "grow");
        mainPanel.add(buttonsPanel, "growx");

        this.setLayout(new MigLayout("", "[grow]", "[grow]"));
        this.add(mainPanel, "grow, width :600:");
        this.pack();

        chooseFileButton.addActionListener(e -> {
            openFileWindow();
        });

        uploadButton.addActionListener(e -> {
            mainWindow.setFile(selectedFile);
            mainWindow.setVisibleTaskMenuItems(true);
            DirectoryPreferences.FileLocation.put(fileChooser.getCurrentDirectory().getAbsolutePath());
            this.dispose();
        });

        cancelButton.addActionListener(e -> {
            DirectoryPreferences.FileLocation.put(fileChooser.getCurrentDirectory().getAbsolutePath());
            this.dispose();
        });

        //по нажатию на Enter нажимается кнопка ок
        JRootPane rootPane = SwingUtilities.getRootPane(uploadButton);
        rootPane.setDefaultButton(uploadButton);
    }

    private void openFileWindow() {
        // Восстановление последнего пути к файлу
        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
        MainWindow.getOpenSavedFileChooser().setCurrentDirectory(new File(location));

        MainWindow.getOpenSavedFileChooser().setVisible(true);
        int response = MainWindow.getOpenSavedFileChooser().showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            isFileOpened = true;
            selectedFile = MainWindow.getOpenSavedFileChooser().getSelectedFile();
            String selectedFileName = selectedFile.getName();
            if (!selectedFileName.toLowerCase().endsWith(".json")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
            }
            MainWindow.setFilePath(selectedFile.getParent());
            filePath.setText(selectedFile.getAbsolutePath());
            dataFromFile = txtDataLoader.loadData(selectedFile.getAbsoluteFile());
            textAreaFileContent.setEditable(false);
            textAreaFileContent.setText(String.join("\n", dataFromFile));
            textAreaFileContent.setCaretPosition(0);
            DirectoryPreferences.FileLocation.put(MainWindow.getOpenSavedFileChooser().getCurrentDirectory().getAbsolutePath());
            setResultSolution(getJSON_DATA_READER().read(selectedFile));

        } else {
            isFileOpened = false;
            DirectoryPreferences.FileLocation.put(MainWindow.getOpenSavedFileChooser().getCurrentDirectory().getAbsolutePath());
        }
    }
}

