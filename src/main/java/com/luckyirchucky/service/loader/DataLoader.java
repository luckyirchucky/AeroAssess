package com.luckyirchucky.service.loader;

import java.io.File;
import java.util.List;

/**
 * Загрузчик данных. Интерфейс
 */
public interface DataLoader {
    /**
     * Получение списка точек из файла
     *
     * @param file
     * @return - список точек
     */
    List<String> loadInputData(File file);

    /**
     * Получение списка точек из списка строк
     *
     * @param data
     * @return - список точек
     */
    List<String> loadInputData(List<String> data);

    /**
     * Получение списка строк из файла
     *
     * @param file
     * @return - список строк
     */
    List<String> loadData(File file);
}
