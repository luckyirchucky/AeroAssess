package com.luckyirchucky.service.localization;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, позволяющий русифицировать интерфейс для работы с файлами
 */
public final class LocalizeUIJFileChooser {
    @Setter
    @Getter
    private static boolean isOnline = true;

    private LocalizeUIJFileChooser() {
    }

    /**
     * Руссификатор окна работы с файловой системой
     *
     * @param choose
     */
    public static void setUpdateUIJFileChooser(boolean isJson, JFileChooser choose) {
        choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
        setFileType(isJson, choose);
        choose.setAcceptAllFileFilterUsed(false);
        //DesktopIconUI
        UIManager.put("FileChooser.lookInLabelMnemonic", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'></span></html>");

        UIManager.put("FileChooser.cancelButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Отмена" + "</span></html>");
        UIManager.put("FileChooser.openButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Открыть" + "</span></html>");
        UIManager.put("FileChooser.directoryOpenButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Открыть" + "</span></html>");
        UIManager.put("FileChooser.lookInLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Смотреть в" + "</span></html>");
        UIManager.put("FileChooser.fileNameLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Имя файла" + "</span></html>");
        UIManager.put("FileChooser.filesOfTypeLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Тип файла" + "</span></html>");
        UIManager.put("FileChooser.saveButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Сохранить" + "</span></html>");
        UIManager.put("FileChooser.saveButtonToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Сохранить" + "</span></html>");
        UIManager.put("FileChooser.cancelButtonToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Отмена" + "</span></html>");
        UIManager.put("FileChooser.openButtonToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Открыть" + "</span></html>");
        UIManager.put("FileChooser.lookInLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Папка" + "</span></html>");
        UIManager.put("FileChooser.saveInLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Папка" + "</span></html>");
        UIManager.put("FileChooser.filesOfTypeLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Тип файлов" + "</span></html>");
        UIManager.put("FileChooser.upFolderToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "На один уровень вверх" + "</span></html>");
        UIManager.put("FileChooser.homeFolderToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "В домашнюю директорию" + "</span></html>");
        UIManager.put("FileChooser.newFolderToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Создание новой папки" + "</span></html>");
        UIManager.put("FileChooser.newFolderActionLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Новая папка" + "</span></html>");
        UIManager.put("FileChooser.listViewButtonToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Список" + "</span></html>");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Таблица" + "</span></html>");
        UIManager.put("FileChooser.fileNameHeaderText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Имя" + "</span></html>");
        UIManager.put("FileChooser.fileSizeHeaderText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Размер" + "</span></html>");
        UIManager.put("FileChooser.fileTypeHeaderText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Тип" + "</span></html>");
        UIManager.put("FileChooser.fileDateHeaderText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Изменен" + "</span></html>");
        UIManager.put("FileChooser.fileAttrHeaderText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Атрибуты" + "</span></html>");
        UIManager.put("FileChooser.acceptAllFileFilterText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Все файлы" + "</span></html>");
        UIManager.put("FileChooser.viewMenuLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Вид" + "</span></html>");
        UIManager.put("FileChooser.listViewActionLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Список" + "</span></html>");
        UIManager.put("FileChooser.detailsViewActionLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Детали" + "</span></html>");
        UIManager.put("FileChooser.refreshActionLabelText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Обновить" + "</span></html>");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить файл");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть файл");
        UIManager.put("OptionPane.yesButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Да" + "</span></html>");
        UIManager.put("OptionPane.noButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Нет" + "</span></html>");
        UIManager.put("OptionPane.okButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "ОК" + "</span></html>");
        UIManager.put("OptionPane.cancelButtonText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Отмена" + "</span></html>");
        UIManager.put("FileChooser.renameErrorTitleText", "Ошибка");
        UIManager.put("FileChooser.renameErrorText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Невозможно переименовать." + "</span></html>");
        UIManager.put("FileChooser.renameErrorFileExistsText", "<html><span style='font-family: System; font-size: 12pt; "
                + "font-weight: normal;'>" + "Невозможно переименовать." + "</span></html>");

        choose.updateUI();
    }

    private static void setFileType(boolean isJson, JFileChooser chooser) {
        if (isJson) {
            chooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".json") || file.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".json файлы";
                }
            });
        } else {
            chooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".txt") || file.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Текстовый файл";
                }
            });
        }
    }

}
