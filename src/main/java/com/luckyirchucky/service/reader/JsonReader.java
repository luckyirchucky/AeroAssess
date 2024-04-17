package com.luckyirchucky.service.reader;

import java.io.File;

/**
 * Интерфейс чтения из файла
 *
 * @param <T>
 */
public interface JsonReader<T> {
    /**
     * Прочитать файл
     *
     * @param file
     * @return - считанный объект
     */
    T read(File file);

    /**
     * Записать объект в файл
     *
     * @param t
     * @param file
     * @return - записанный файл
     */
    File write(T t, File file);
}
