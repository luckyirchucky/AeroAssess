package com.luckyirchucky.service.reader;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Работа с файлом JSON-формата
 *
 * @param <T>
 */
public class JsonDataReader<T> implements JsonReader<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> type;

    public JsonDataReader(Class<T> type) {
        this.type = type;
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public T read(File file) {
        try {
            return objectMapper.readValue(file, type);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    null,
                    "Ошибка в файле",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Не удалось прочитать данные из файла");
        }
    }

    @Override
    public File write(T t, File file) {
        try {
            objectMapper.writeValue(file, t);
        } catch (IOException e) {
            if (!(file.getName().contains("<") || file.getName().contains(">") ||
                    file.getName().contains("|") || file.getName().contains("\"") ||
                    file.getName().contains(":"))){

                JOptionPane.showMessageDialog(null, "Невозможно создать файл.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

            throw new RuntimeException("Возникла ошибка при записи в файл");
        }
        return file;
    }
}
