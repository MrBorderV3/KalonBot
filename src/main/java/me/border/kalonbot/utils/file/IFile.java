package me.border.kalonbot.utils.file;

import java.io.File;

public interface IFile {

    void setup();

    void save();

    String getFileName();

    File getFile();
}
