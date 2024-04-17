package com.luckyirchucky.ui.main;

import com.luckyirchucky.model.ResultSolution;

import java.io.File;

/**
 * Интерфейс основного окна (главной формы)
 * Определяет основные функциональные методы
 */
public interface IMainWindow {

    /**
     * Получить результат решения
     *
     * @return - результат решения (по всем задачам)
     */
    ResultSolution getResultSolution();

    /**
     * Запомнить файл с исследуемым параметром
     *
     * @param file
     */
    void setFile(File file);


    /**
     * Управление доступностью подпунктов меню "Обработка"
     *
     * @param isEnabled
     */
    void setVisibleTaskMenuItems(boolean isEnabled);


}
