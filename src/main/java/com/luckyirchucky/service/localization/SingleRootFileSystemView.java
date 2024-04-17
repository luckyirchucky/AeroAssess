package com.luckyirchucky.service.localization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.filechooser.FileSystemView;

/**
 * Класс, который переопределяет корневой каталог пользователя
 */
public class SingleRootFileSystemView extends FileSystemView {
    private File root;
    private File[] roots = new File[1];
    private static int nextFolderNumber;

    public SingleRootFileSystemView(File path) {
        super();
        try {
            root = path.getCanonicalFile();
            roots[0] = root;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        if (!root.isDirectory()) {
            String message = root + " не является директорией";
            throw new IllegalArgumentException(message);
        }

        nextFolderNumber = findLastFolderNumber(root) + 1;
    }

    @Override
    public File createNewFolder(File containingDir) {
        String folderName = "Новая папка";
        File folder = new File(containingDir, folderName);
        if (!folder.exists() && folder.mkdir()) {
            if (nextFolderNumber != 1) {
                nextFolderNumber++;
            }
        } else {
            int number = nextFolderNumber;
            if (number > 0) {
                folderName = String.format("%s (%d)", folderName, number);
            }
            while (true) {
                folder = new File(containingDir, folderName);
                if (!folder.exists() && folder.mkdir()) {
                    nextFolderNumber = number + 1;
                    break;
                }
                number++;
                folderName = String.format("%s (%d)", "Новая папка", number);
            }
        }
        saveNextFolderNumber();
        return folder;
    }

    @Override
    public File[] getFiles(File dir, boolean useFileHiding) {
        File[] files = super.getFiles(dir, useFileHiding);
        List<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            if (!isShortcut(String.valueOf(file))) {
                filteredFiles.add(file);
            }
        }
        return filteredFiles.toArray(new File[0]);
    }

    @Override
    public Boolean isTraversable(File file) {
        if (isShortcut(String.valueOf(file))) {
            return false;
        }
        return super.isTraversable(file);
    }

    /**
     * Проверка, является ли ярлыком
     *
     * @param filePath
     */
    private static boolean isShortcut(String filePath) {
        if(filePath == null || filePath.isEmpty()) {
            return false;
        }
        File file = new File(filePath);
        if(file == null || !file.exists()) {
            return false;
        }
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return canonicalPath.endsWith(".lnk");
    }

    private void saveNextFolderNumber() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.putInt("nextFolderNumber", nextFolderNumber);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private int findLastFolderNumber(File root) {
        File[] files = root.listFiles();
        int lastNumber = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String fileName = file.getName();
                    if (fileName.matches("Новая папка( \\(\\d+\\))?")) {
                        int number = extractFolderNumber(fileName);
                        if (number > lastNumber) {
                            lastNumber = number;
                        }
                    }
                }
            }
        }
        return lastNumber;
    }

    private int extractFolderNumber(String folder) {
        int number = 0;
        int startIndex = folder.lastIndexOf('(');
        int endIndex = folder.lastIndexOf(')');
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            try {
                number = Integer.parseInt(folder.substring(startIndex + 1, endIndex));
            } catch (NumberFormatException ignored) {
                System.out.println("Ошибка при преобразовании строки в число: " + ignored.getMessage());
            }
        }
        return number;
    }
}
