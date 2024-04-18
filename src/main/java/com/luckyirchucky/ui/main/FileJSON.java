package com.luckyirchucky.ui.main;

import com.luckyirchucky.model.ResultSolution;
import com.luckyirchucky.service.watchservice.DirectoryPreferences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.io.File;

@Data
@AllArgsConstructor
@Builder
public class FileJSON {
    String fullFileName;
    String fileType;

    public static void saveResultSolutionToFileAsJson() {
        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
        MainWindow.getOpenSavedFileChooser().setCurrentDirectory(new File(location));
        String fileName = MainWindow.getFileName().replace(".txt", "");
        if (!fileName.contains("_result")) {
            fileName += "_result";
        }
        File defaultFile = new File(fileName);
        MainWindow.getOpenSavedFileChooser().setSelectedFile(defaultFile);

        if (!MainWindow.getResultSolution().equals(new ResultSolution())) {

            //Окно сохранения в файл
            int response = MainWindow.getOpenSavedFileChooser().showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {

                File file = MainWindow.getOpenSavedFileChooser().getSelectedFile();

                //Дописывать расширение, если пользователь не написал
                if (!file.getName().toLowerCase().endsWith(".json")) {
                    String filePath = file.getAbsolutePath().replace(".txt", "");
                    file = new File(filePath + ".json");
                }

                //Проверка, если файл существует
                if (file.exists()) {
                    int overwriteResponse = JOptionPane.showConfirmDialog(null,
                            "В данном файле уже существует решение. Хотите его перезаписать?",
                            "Решение существует", JOptionPane.YES_NO_OPTION);
                    if (overwriteResponse == JOptionPane.YES_OPTION) {
                        MainWindow.getJSON_DATA_READER().write(MainWindow.getResultSolution(), file);
                    } else {
                        return;
                    }
                } else {
                    MainWindow.getJSON_DATA_READER().write(MainWindow.getResultSolution(), file);
                }
            }
            DirectoryPreferences.FileLocation.put(MainWindow.getOpenSavedFileChooser().getCurrentDirectory().getAbsolutePath());

        } else {
            JOptionPane.showMessageDialog(null,
                    "<html><span style='font-family: System; font-size: 12pt; "
                            + "font-weight: normal;'>" + "Пока нечего сохранять" + "</span></html>", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
