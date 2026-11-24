package com.example.oopproject.util;

import java.io.*;
import java.util.ArrayList;

public class BakeryFileManager {
    public static BakeryFileManager instance;
    private static String FILENAME = "bakery_data.ser";

    private BakeryFileManager() {}

    public static synchronized BakeryFileManager getInstance() {
        if (instance == null) {
            instance = new BakeryFileManager();
        }
        return instance;
    }

    public void saveData(ArrayList<Object> bakery) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(bakery);
        }
    }

    public ArrayList<Object> loadData() throws IOException, ClassNotFoundException {
        File file = new File(FILENAME);
        if (!file.exists()) {
            throw new FileNotFoundException("Bakery Data File not found");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (ArrayList<Object>) ois.readObject();
        }
    }
}
