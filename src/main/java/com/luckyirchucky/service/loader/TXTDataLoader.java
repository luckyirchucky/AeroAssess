package com.luckyirchucky.service.loader;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Загрузчик данных. Реализация. Загрузка из txt-файла
 */
public class TXTDataLoader implements DataLoader {

    private final Font font = new Font("System", Font.PLAIN, 12);

    @Override
    public List<String> loadInputData(File file) {
        return null;
    }

    @Override
    public List<String> loadInputData(List<String> data) {
        return null;
    }

    @Override
    public List<String> loadData(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> data = reader.lines()
                    .collect(Collectors.toList());
            return data;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
