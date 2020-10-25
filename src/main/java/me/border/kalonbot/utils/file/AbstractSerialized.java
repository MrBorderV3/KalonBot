package me.border.kalonbot.utils.file;

import java.io.*;

public abstract class AbstractSerialized<I> implements IFile {

    private String fileName;
    private File file;
    public I item;

    public AbstractSerialized(String fileName, I item){
        this.fileName = fileName;
        this.item = item;
        setup();
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    @Override
    public void setup() {
        this.file = new File(fileName + ".dat");
        if (this.file.exists()) {
            this.item = load();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(item);
            oos.flush();
            oos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public I load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object result = ois.readObject();
            ois.close();
            return (I)result;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public String getFileName(){
        return fileName;
    }

    @Override
    public File getFile(){
        return file;
    }
}