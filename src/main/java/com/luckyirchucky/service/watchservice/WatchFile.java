package com.luckyirchucky.service.watchservice;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.JFileChooser;

import org.apache.commons.io.FilenameUtils;

/**
 * Класс для просмотра изменений в именах файлов
 */
public class WatchFile {
    JFileChooser fileChooser;

    /**
     * Просмотр файлов, созданных без расширения для их удаления
     *
     * @param fileChooser объект JFileChooser, который будет использоваться для просмотра файлов
     * @throws NullPointerException если параметр fileChooser равен null
     */
    public WatchFile(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Конструктор без параметров
     *
     * @throws IOException исключение
     * @throws InterruptedException исключение
     */
    public void watchFile() throws IOException, InterruptedException {

        // Создаем службу просмотра
        WatchService watchService = FileSystems.getDefault().newWatchService();

        // Опрос по событиям
        while (true) {

            // Получить путь к каталогу, который хотим отслеживать
            Path directory = fileChooser.getCurrentDirectory().toPath();

            // Регистрация каталога в службе просмотра
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            for (WatchEvent<?> event : watchKey.pollEvents()) {

                // Получить имя файла из контекста
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                Path fileName = pathEvent.context();

                // Проверить тип события
                WatchEvent.Kind<?> kind = event.kind();

                // Выполнить действие с событием
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {

                    String fileExtension = getExtensionByApacheCommonLib(String.valueOf(fileName.toFile()));

                    if (!fileExtension.equals("txt") && !fileExtension.equals("png") && !fileExtension.equals("json")) {

                        Path filePath = directory.resolve(fileName.toString());

                        if (Files.exists(filePath)) {
                            Thread.sleep(500);
                            try {
                                Files.delete(filePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

            }

            // Сбросить клавишу просмотра каждый раз,
            // чтобы продолжать использовать ее для дальнейшего опроса событий
            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }

        }

    }

    /**
     * Возвращает расширение файла
     *
     * @param filename - имя файла
     */
    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
