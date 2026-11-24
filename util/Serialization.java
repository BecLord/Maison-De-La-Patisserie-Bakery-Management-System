package com.example.oopproject.util;

import com.example.oopproject.model.CustomerRecords;
import com.example.oopproject.model.Order;
import java.io.*;
import javafx.scene.control.Alert;

public class Serialization {
    private static Serialization instance;
    private static final String FILENAME = "store_data.ser";

    private Serialization() {}

    public static Serialization getInstance() {
        if (instance == null) {
            instance = new Serialization();
        }
        return instance;
    }

    public void saveStore(CustomerRecords records, Order currentOrder) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(records);
            oos.writeObject(currentOrder);
            showAlert("Success", "Store data saved successfully!");
        } catch (IOException e) {
            showAlert("Error", "Failed to save store data: " + e.getMessage());
        }
    }

    public Object[] loadStore() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            CustomerRecords records = (CustomerRecords) ois.readObject();
            Order order = (Order) ois.readObject();
            showAlert("Success", "Store data loaded successfully!");
            return new Object[]{records, order};
        } catch (FileNotFoundException e) {
            showAlert("Error", "Save file not found.");
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Error", "Failed to load store data: " + e.getMessage());
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
