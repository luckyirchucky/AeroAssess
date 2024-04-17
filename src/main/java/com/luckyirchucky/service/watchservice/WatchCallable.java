package com.luckyirchucky.service.watchservice;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Класс для вызова просмотра изменений в именах файлов
 */
public class WatchCallable implements Callable<Void> {
    JFileChooser fileChooser;
    public WatchCallable(JFileChooser fileChooser){
        this.fileChooser = fileChooser;
    }

    @Override
    public Void call() throws IOException, InterruptedException {
        WatchFile wf = new WatchFile(fileChooser);
        wf.watchFile();

        return null;
    }

}
