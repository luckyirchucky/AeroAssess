package com.luckyirchucky.service.watchservice;

import java.util.prefs.Preferences;

/**
 * Перечень директорий
 */
public enum DirectoryPreferences {
    FileLocation;

    private static Preferences preferences = Preferences.userRoot()
            .node(DirectoryPreferences.class.getName());

    /**
     * Получить последнюю открытую директорию
     *
     * @param defaultValue
     */
    public String get(String defaultValue) {
        return preferences.get(this.name(), defaultValue);
    }

    /**
     * Добавить последнюю открытую директорию
     *
     * @param value
     */
    public void put(String value) {
        preferences.put(this.name(), value);
    }
}
