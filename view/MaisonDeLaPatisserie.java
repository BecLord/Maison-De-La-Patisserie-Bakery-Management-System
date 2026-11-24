package com.example.oopproject.view;

import com.example.oopproject.Bakery;
import com.example.oopproject.controller.Admin;
import com.example.oopproject.model.Customer;
import com.example.oopproject.model.Product;
import com.example.oopproject.model.Order;
import com.example.oopproject.model.Cake;
import com.example.oopproject.model.Bread;
import com.example.oopproject.model.Pastry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class MaisonDeLaPatisserie extends Application {

    private Bakery bakery;
    private Admin admin;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private boolean dataChanged = false;

    private static int customerCounter = 1;

    private static final String CUSTOMER_FILE = "customers.ser";
    private static final String PRODUCT_FILE = "products.ser";
    private static final String ORDER_FILE = "orders.ser";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        bakery = new Bakery();
        admin = bakery.getAdmin();

        loadCustomers();
        updateCustomerCounter();
        loadProducts();
        loadOrders();

        TabPane tabPane = new TabPane(
                createCustomerTab(),
                createOrderTab(),
                createProductTab()
        );
        tabPane.setStyle("-fx-background-color: #f8efe6;");

        Scene scene = new Scene(tabPane, 1100, 650);
        primaryStage.setTitle("Maison De La Patisserie");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            askSaveBeforeExit(primaryStage);
        });
        primaryStage.show();
    }

    //Customer Tab
    private Tab createCustomerTab() {
        Tab tab = new Tab("Customers");
        tab.setClosable(false);

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        styleField(nameField); styleField(emailField); styleField(phoneField); styleField(addressField);

        ListView<Customer> customerListView = new ListView<>(customerList);
        customerListView.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        Button removeSelectedBtn = new Button("Remove Selected");
        styleButton(removeSelectedBtn);
        removeSelectedBtn.setOnAction(e -> {
            Customer selected = customerListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                customerList.remove(selected);
                admin.getAllCustomers().remove(selected);
                showAlert(Alert.AlertType.INFORMATION, "Removed", "Customer removed.");
            } else {
                showAlert(Alert.AlertType.WARNING, "No selection", "Select a customer to remove.");
            }
        });

        Button addBtn = new Button("Add");
        styleButton(addBtn);
        addBtn.setOnAction(e -> handleAddCustomer(nameField, emailField, phoneField, addressField));

        Button saveBtn = new Button("Save");
        styleButton(saveBtn);
        saveBtn.setOnAction(e -> saveCustomers());

        Button loadBtn = new Button("Load");
        styleButton(loadBtn);
        loadBtn.setOnAction(e -> {
            loadCustomers();
            updateCustomerCounter();
        });

        Label addLabel = new Label("Add Customer");
        Label removeLabel = new Label("Remove by Name");
        Label listLabel = new Label("Customer List:");
        styleLabel(addLabel); styleLabel(removeLabel); styleLabel(listLabel);

        VBox form = new VBox(8,
                addLabel,
                new Label("Name"), nameField,
                new Label("Email"), emailField,
                new Label("Phone"), phoneField,
                new Label("Address"), addressField,
                addBtn
        );
        form.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        VBox left = new VBox(12, form, removeSelectedBtn, saveBtn, loadBtn);
        left.setPadding(new Insets(16));
        left.setPrefWidth(320);
        left.setStyle("-fx-background-color: #f8efe6;");

        VBox right = new VBox(10, listLabel, customerListView);
        right.setPadding(new Insets(16));
        right.setPrefWidth(600);
        right.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        HBox main = new HBox(20, left, right);
        main.setPadding(new Insets(10));
        main.setStyle("-fx-background-color: #f8efe6;");

        tab.setContent(main);
        return tab;
    }

    //Order Tab
    private Tab createOrderTab() {
        Tab tab = new Tab("Orders");
        tab.setClosable(false);

        ComboBox<Customer> customerCombo = new ComboBox<>(customerList);
        ComboBox<Product> productCombo = new ComboBox<>(productList);
        customerCombo.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");
        productCombo.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, 1);
        quantitySpinner.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        Button addBtn = new Button("Add to Cart");
        Button finalizeBtn = new Button("Finalize Purchase");
        styleButton(addBtn);
        styleButton(finalizeBtn);

        TextArea cartArea = new TextArea();
        cartArea.setEditable(false);
        cartArea.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        Label totalLabel = new Label("Total: €0.00");
        styleLabel(totalLabel);

        Map<Product, Integer> cartMap = new LinkedHashMap<>();

        addBtn.setOnAction(e -> {
            Customer customer = customerCombo.getValue();
            Product product = productCombo.getValue();
            int qty = quantitySpinner.getValue();
            if (customer == null || product == null) {
                showAlert(Alert.AlertType.WARNING, "Select Both", "Select a customer and a product.");
                return;
            }
            if (qty > product.getStockQuantity()) {
                showAlert(Alert.AlertType.ERROR, "Stock Error", "Not enough stock available.");
                return;
            }
            cartMap.put(product, cartMap.getOrDefault(product, 0) + qty);
            StringBuilder sb = new StringBuilder();
            double total = 0.0;
            for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                sb.append(entry.getKey().getName()).append(" x").append(entry.getValue())
                        .append(" (€").append(entry.getKey().getPrice()).append(" each)\n");
                total += entry.getKey().getPrice() * entry.getValue();
            }
            cartArea.setText(sb.toString());
            totalLabel.setText(String.format("Total: €%.2f", total));
        });

        finalizeBtn.setOnAction(e -> {
            Customer customer = customerCombo.getValue();
            if (customer == null || cartMap.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "No Purchase", "Select customer and add products.");
                return;
            }
            Order order = new Order(customer);
            for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    order.addProduct(entry.getKey());
                }
                entry.getKey().setStockQuantity(entry.getKey().getStockQuantity() - entry.getValue());
            }
            orderList.add(order);
            admin.getCurrentOrders().add(order);
            cartMap.clear();
            cartArea.clear();
            totalLabel.setText("Total: €0.00");
            showAlert(Alert.AlertType.INFORMATION, "Order Complete", "Purchase finalized!");
            dataChanged = true;
        });

        Label selectCustomerLabel = new Label("Select Customer:");
        Label selectProductLabel = new Label("Select Product:");
        Label quantityLabel = new Label("Quantity:");
        Label cartLabel = new Label("Cart:");
        styleLabel(selectCustomerLabel);
        styleLabel(selectProductLabel);
        styleLabel(quantityLabel);
        styleLabel(cartLabel);

        VBox left = new VBox(10,
                selectCustomerLabel, customerCombo,
                selectProductLabel, productCombo,
                quantityLabel, quantitySpinner,
                addBtn, finalizeBtn, totalLabel);
        left.setPadding(new Insets(16));
        left.setPrefWidth(320);
        left.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        VBox right = new VBox(10, cartLabel, cartArea);
        right.setPadding(new Insets(16));
        right.setPrefWidth(600);
        right.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        HBox main = new HBox(20, left, right);
        main.setPadding(new Insets(10));
        main.setStyle("-fx-background-color: #f8efe6;");

        tab.setContent(main);
        return tab;
    }

    //Product Tab
    private Tab createProductTab() {
        Tab tab = new Tab("Products");
        tab.setClosable(false);

        productList = FXCollections.observableArrayList(bakery.listAvailableProducts());
        TableView<Product> productTable = new TableView<>(productList);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productTable.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getStockQuantity()).asObject());
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        TableColumn<Product, String> allergensCol = new TableColumn<>("Allergens");
        allergensCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAllergens()));

        productTable.getColumns().addAll(nameCol, priceCol, stockCol, categoryCol, allergensCol);

        ComboBox<String> typeCombo = new ComboBox<>(FXCollections.observableArrayList("Cake", "Bread", "Pastry"));
        typeCombo.setValue("Cake");
        typeCombo.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField categoryField = new TextField();
        TextField allergensField = new TextField();
        styleField(nameField); styleField(priceField); styleField(categoryField); styleField(allergensField);

        Spinner<Integer> stockSpinner = new Spinner<>(0, 1000, 10);
        stockSpinner.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");

        CheckBox availableBox = new CheckBox("Available");
        availableBox.setStyle("-fx-text-fill: #6c4327;");

        TextField flavourField = new TextField();
        TextField sizeField = new TextField();
        CheckBox customisableBox = new CheckBox("Customisable");
        styleField(flavourField); styleField(sizeField);
        customisableBox.setStyle("-fx-text-fill: #6c4327;");

        TextField breadTypeField = new TextField();
        styleField(breadTypeField);

        TextField pastryTypeField = new TextField();
        TextField fillingField = new TextField();
        styleField(pastryTypeField); styleField(fillingField);

        VBox dynamicFields = new VBox(10);
        Runnable updateDynamicFields = () -> {
            dynamicFields.getChildren().clear();
            String type = typeCombo.getValue();
            if ("Cake".equals(type)) {
                Label flavourLabel = new Label("Flavour:");
                Label sizeLabel = new Label("Size:");
                styleLabel(flavourLabel); styleLabel(sizeLabel);
                dynamicFields.getChildren().addAll(
                        flavourLabel, flavourField,
                        sizeLabel, sizeField,
                        customisableBox
                );
            } else if ("Bread".equals(type)) {
                Label typeLabel = new Label("Type:");
                styleLabel(typeLabel);
                dynamicFields.getChildren().addAll(
                        typeLabel, breadTypeField
                );
            } else {
                Label typeLabel = new Label("Type:");
                Label fillingLabel = new Label("Filling:");
                styleLabel(typeLabel); styleLabel(fillingLabel);
                dynamicFields.getChildren().addAll(
                        typeLabel, pastryTypeField,
                        fillingLabel, fillingField
                );
            }
        };
        typeCombo.setOnAction(e -> updateDynamicFields.run());
        updateDynamicFields.run();

        Button addBtn = new Button("Add/Update");
        Button removeBtn = new Button("Remove");
        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Load");
        styleButton(addBtn); styleButton(removeBtn); styleButton(saveBtn); styleButton(loadBtn);

        addBtn.setOnAction(e -> {
            String type = typeCombo.getValue();
            String name = nameField.getText().trim();
            double price;
            try {
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid price.");
                return;
            }
            String category = categoryField.getText().trim();
            String allergens = allergensField.getText().trim();
            int stock = stockSpinner.getValue();
            boolean isAvailable = availableBox.isSelected();

            Optional<Product> existing = productList.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
            if (existing.isPresent()) {
                Product p = existing.get();
                p.setPrice(price);
                p.setStockQuantity(stock);
            } else {
                Product product;
                String productId = UUID.randomUUID().toString();
                if ("Cake".equals(type)) {
                    String flavour = flavourField.getText().trim();
                    int size;
                    try {
                        size = Integer.parseInt(sizeField.getText().trim());
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid size.");
                        return;
                    }
                    boolean customisable = customisableBox.isSelected();
                    product = new Cake(productId, name, price, category, allergens, stock, isAvailable, flavour, size, customisable);
                } else if ("Bread".equals(type)) {
                    String breadType = breadTypeField.getText().trim();
                    product = new Bread(productId, name, price, category, allergens, stock, isAvailable, breadType);
                } else { // Pastry
                    String pastryType = pastryTypeField.getText().trim();
                    String filling = fillingField.getText().trim();
                    product = new Pastry(productId, name, price, category, allergens, stock, isAvailable, pastryType, filling);
                }
                productList.add(product);
                admin.addProduct(product);
            }
            nameField.clear(); priceField.clear(); categoryField.clear(); allergensField.clear();
            flavourField.clear(); sizeField.clear(); breadTypeField.clear(); pastryTypeField.clear(); fillingField.clear();
            stockSpinner.getValueFactory().setValue(10);
            availableBox.setSelected(false); customisableBox.setSelected(false);
            dataChanged = true;
        });

        removeBtn.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                productList.remove(selected);
                admin.removeProduct(selected.getProductId());
                dataChanged = true;
            }
        });

        saveBtn.setOnAction(e -> saveProducts());
        loadBtn.setOnAction(e -> loadProducts());

        Label typeLabel = new Label("Type:");
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label categoryLabel = new Label("Category:");
        Label allergensLabel = new Label("Allergens:");
        Label stockLabel = new Label("Stock:");
        Label productsLabel = new Label("Products:");
        styleLabel(typeLabel); styleLabel(nameLabel); styleLabel(priceLabel);
        styleLabel(categoryLabel); styleLabel(allergensLabel); styleLabel(stockLabel);
        styleLabel(productsLabel);

        VBox form = new VBox(10,
                typeLabel, typeCombo,
                nameLabel, nameField,
                priceLabel, priceField,
                categoryLabel, categoryField,
                allergensLabel, allergensField,
                stockLabel, stockSpinner,
                availableBox,
                dynamicFields,
                addBtn, removeBtn, saveBtn, loadBtn
        );
        form.setPadding(new Insets(16));
        form.setPrefWidth(320);
        form.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        ScrollPane leftScroll = new ScrollPane(form);
        leftScroll.setFitToWidth(true);
        leftScroll.setPrefHeight(650);
        leftScroll.setStyle("-fx-background-color: #f8efe6;");

        VBox right = new VBox(10, productsLabel, productTable);
        right.setPadding(new Insets(16));
        right.setPrefWidth(600);
        right.setStyle("-fx-background-color: #fff8f0; -fx-border-radius: 8; -fx-background-radius: 8;");

        HBox main = new HBox(20, leftScroll, right);
        main.setPadding(new Insets(10));
        main.setStyle("-fx-background-color: #f8efe6;");

        tab.setContent(main);
        return tab;
    }

    private void styleButton(Button btn) {
        btn.setStyle("-fx-background-color: #e3bfa0; -fx-text-fill: #6c4327; -fx-font-weight: bold; -fx-background-radius: 8;");
    }
    private void styleField(TextField tf) {
        tf.setStyle("-fx-background-color: #fff8f0; -fx-border-color: #e3bfa0; -fx-border-radius: 6;");
    }
    private void styleLabel(Label label) {
        label.setStyle("-fx-text-fill: #6c4327; -fx-font-size: 15px; -fx-font-family: Georgia, serif;");
    }

    private void handleAddCustomer(TextField nameField, TextField emailField, TextField phoneField, TextField addressField) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Name and Email are required.");
            return;
        }

        String customerId = "C" + customerCounter++;
        Customer customer = new Customer(customerId, name, email, phone, address);
        admin.getAllCustomers().add(customer);
        customerList.add(customer);
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Added", "Customer added.");
    }

    private void updateCustomerCounter() {
        int max = 0;
        for (Customer c : customerList) {
            String id = c.getCustomerId();
            if (id != null && id.startsWith("C")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        customerCounter = max + 1;
    }

    private void saveCustomers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {
            out.writeObject(new ArrayList<>(customerList));
            showAlert(Alert.AlertType.INFORMATION, "Saved", "Customers saved successfully.");
            dataChanged = false;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save customers.");
        }
    }

    private void loadCustomers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CUSTOMER_FILE))) {
            ArrayList<Customer> loaded = (ArrayList<Customer>) in.readObject();
            customerList.setAll(loaded);
            if (admin != null) {
                admin.getAllCustomers().clear();
                admin.getAllCustomers().addAll(loaded);
            }
        } catch (Exception e) {
        }
    }

    private void saveProducts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            out.writeObject(new ArrayList<>(productList));
            showAlert(Alert.AlertType.INFORMATION, "Saved", "Products saved successfully.");
            dataChanged = false;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save products.");
        }
    }

    private void loadProducts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PRODUCT_FILE))) {
            ArrayList<Product> loaded = (ArrayList<Product>) in.readObject();
            productList.setAll(loaded);
        } catch (Exception e) {
        }
    }

    private void loadOrders() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            ArrayList<Order> loaded = (ArrayList<Order>) in.readObject();
            orderList.setAll(loaded);
            if (admin != null) {
                admin.getCurrentOrders().clear();
                admin.getCurrentOrders().addAll(loaded);
            }
        } catch (Exception e) {
        }
    }

    private void askSaveBeforeExit(Stage stage) {
        if (dataChanged) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Save changes before exit?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Exit Confirmation");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    saveCustomers();
                    saveProducts();
                    Platform.exit();
                } else if (result.get() == ButtonType.NO) {
                    Platform.exit();
                }
            }
        } else {
            Platform.exit();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
