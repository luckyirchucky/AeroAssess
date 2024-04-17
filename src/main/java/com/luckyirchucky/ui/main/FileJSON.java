//package com.luckyirchucky.ui.main;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.file.Paths;
//
//import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.luckyirchucky.model.ResultSolution;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
//@Builder
//public class FileJSON {
//    String fullFileName;
//    String fileType;
//
//    public static void saveResultSolutionToFileAsJson() {
//        String location = DirectoryPreferences.FileLocation.get(System.getProperty("user.home"));
//        MainWindow.getOpenSavedFileChooser().setCurrentDirectory(new File(location));
//
//        File defaultFile = new File(fileName);
//        MainWindow.getOpenSavedFileChooser().setSelectedFile(defaultFile);
//
//        if (!MainWindow.getBufferResult().equals(new ResultSolution())) {
//
//            //Окно сохранения в файл
//            int response = MainWindow.getOpenSavedFileChooser().showSaveDialog(null);
//
//            if (response == JFileChooser.APPROVE_OPTION) {
//
//                File file = MainWindow.getOpenSavedFileChooser().getSelectedFile();
//
//                //Дописывать расширение, если пользователь не написал
//                if (!file.getName().toLowerCase().endsWith(".json")) {
//                    String filePath = file.getAbsolutePath().replace(".txt", "");
//                    file = new File(filePath + ".json");
//                }
//
//                //Проверка, если файл существует
//                if (file.exists()) {
//                    ResultSolution bufferFullResult = MainWindow.getJSON_DATA_READER().read(file);
//                    if (MainWindow.getBufferResult().getPointsList().isCircleType()) {
//                        if (bufferFullResult.getAzimuthResultSolutions().getPointsList() != null) {
//                            int overwriteResponse = JOptionPane.showConfirmDialog(null,
//                                    "В данном файле уже существует решение азимута. Хотите его перезаписать?",
//                                    "Решение существует", JOptionPane.YES_NO_OPTION);
//                            if (overwriteResponse == JOptionPane.YES_OPTION) {
//                                MainWindow.getFullResultSolution().setElevationResultSolutions(bufferFullResult.getElevationResultSolutions());
//                                MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                            } else {
//                                return;
//                            }
//                        } else {
//                            MainWindow.getFullResultSolution().setElevationResultSolutions(bufferFullResult.getElevationResultSolutions());
//                            MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                        }
//                    }
//
//                    if (!MainWindow.getBufferResult().getPointsList().isCircleType()) {
//                        if (bufferFullResult.getElevationResultSolutions().getPointsList() != null) {
//                            int overwriteResponse = JOptionPane.showConfirmDialog(null,
//                                    "В данном файле уже существует решение угла места. Хотите его перезаписать?",
//                                    "Решение существует", JOptionPane.YES_NO_OPTION);
//                            if (overwriteResponse == JOptionPane.YES_OPTION) {
//                                MainWindow.getFullResultSolution().setAzimuthResultSolutions(bufferFullResult.getAzimuthResultSolutions());
//                                MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                            } else {
//                                return;
//                            }
//                        } else {
//                            MainWindow.getFullResultSolution().setAzimuthResultSolutions(bufferFullResult.getAzimuthResultSolutions());
//                            MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                        }
//                    }
//                } else {
//                    if(MainWindow.getBufferResult().getPointsList().isCircleType()){
//                        MainWindow.getFullResultSolution().setAzimuthResultSolutions(MainWindow.getBufferResult());
//                        MainWindow.getFullResultSolution().setElevationResultSolutions(new ResultSolution());
//                        MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                    } else {
//                        MainWindow.getFullResultSolution().setElevationResultSolutions(MainWindow.getBufferResult());
//                        MainWindow.getFullResultSolution().setAzimuthResultSolutions(new ResultSolution());
//                        MainWindow.getJSON_DATA_READER().write(MainWindow.getFullResultSolution(), file);
//                    }
//                }
//                if (LocalizeUIJFileChooser.isOnline()) {
//                    try {
//                        String coordProcessingFile = Paths.get(MainWindow.getFilePath() + "/" + MainWindow.getFileName() + ".txt").toString();
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        FileJSON fileJSON = new FileJSON(file.getAbsolutePath()
//                                .replace("_Az", "")
//                                .replace("_El", ""), "DDTP_PROCESSING_RESULTS_JSON_FILE");
//                        StringDataJson stringDataJson = new StringDataJson(coordProcessingFile, new FileJSON[]{ fileJSON });
//                        String postData = objectMapper.writeValueAsString(stringDataJson);
//                        postRequest(postData);
//                    } catch (JsonProcessingException e) {
//                        System.out.println("Не получилось преобразовать в json для отправки в бд" + e.toString());
//                    }
//                }
//            }
//            DirectoryPreferences.FileLocation.put(MainWindow.getOpenSavedFileChooser().getCurrentDirectory().getAbsolutePath());
//
//        } else {
//            JOptionPane.showMessageDialog(null,
//                    "<html><span style='font-family: System; font-size: 12pt; "
//                            + "font-weight: normal;'>" + "Пока нечего сохранять" + "</span></html>", "Ошибка", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}
