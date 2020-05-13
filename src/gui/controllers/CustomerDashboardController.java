package gui.controllers;

import com.jfoenix.controls.JFXTimePicker;
import gui.HelperMethods;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import restaurant.Restaurant;
import restaurant.Table;
import restaurant.dish.Dish;
import restaurant.order.Order;
import restaurant.order.OrderItem;
import restaurant.user.Client;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CustomerDashboardController {
    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private TableView tableItems;

    @FXML
    private ComboBox comboTables;

    @FXML
    private ComboBox comboType;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelPrice;

    private Restaurant restaurant;
    private Client client;
    private HashSet<Class> dishTypes;
    private HashMap<Class, ArrayList<OrderItem>> orderItems;


    public CustomerDashboardController(Restaurant restaurant, Client client) {
        this.restaurant = restaurant;

        this.client = client;
        dishTypes = new HashSet<>();
        orderItems = new HashMap<>();
        for (Dish dish : restaurant.getDishes()) {
            if (dishTypes.add(dish.getClass())) orderItems.put(dish.getClass(), new ArrayList<>());
        }
    }

    public void initialize() {
        for (Class c : dishTypes) {
            comboType.getItems().add(c.getSimpleName());
        }

        timePicker.setValue(LocalTime.now());

        TableColumn dishColumn = new TableColumn("Dish");
        dishColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((OrderItem) ((TableColumn.CellDataFeatures) cellData).getValue()).getDish().getName()));

        TableColumn quantityColumn = new TableColumn("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((OrderItem) ((TableColumn.CellDataFeatures) cellData).getValue()).getQuantity())));

        TableColumn unitPriceColumn = new TableColumn("Unit price");
        unitPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((OrderItem) ((TableColumn.CellDataFeatures) cellData).getValue()).getDish().getPrice())));

        TableColumn totalPriceWithTaxColumn = new TableColumn("Total price with tax");
        totalPriceWithTaxColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((OrderItem) ((TableColumn.CellDataFeatures) cellData).getValue()).getPriceWithTax())));

        tableItems.getColumns().addAll(dishColumn, quantityColumn, unitPriceColumn, totalPriceWithTaxColumn);
    }

    private void fillTablesAvailableAt(LocalDate localDate) {
        comboTables.getItems().clear();
        comboTables.getItems().addAll(restaurant.getUnreservedTablesAt(localDate));
    }

    public void buttonTypeClicked() throws IOException {
        ArrayList<Dish> dishes = new ArrayList<>();
        if (comboType.getValue() == null) return;
        for (Dish dish : restaurant.getDishes()) {
            if (dish.getClass().getSimpleName().equals(comboType.getValue())) {
                dishes.add(dish);
            }
        }
        OrderController controller = new OrderController(dishes, orderItems.get(dishes.get(0).getClass()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/order.fxml"));
        loader.setControllerFactory(c -> controller);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(dishes.get(0).getClass().getSimpleName() + " Menu");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        ArrayList<OrderItem> selectedItems = controller.getOrderItems();
        if (selectedItems == null) return;
        orderItems.put(dishes.get(0).getClass(), controller.getOrderItems());
        tableItems.getItems().clear();
        double price = 0;
        for (ArrayList<OrderItem> items : orderItems.values()) {
            for (OrderItem item : items) {
                tableItems.getItems().add(item);
                price += item.getPriceWithTax();
            }
        }
        labelPrice.setText(String.valueOf(price));

    }

    public void buttonReserveClicked() throws IOException {
        if (datePicker.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "You must select the reservation date.").showAndWait();
            datePicker.requestFocus();
            return;
        }

        if (comboTables.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "You must select a table.").showAndWait();
            comboTables.requestFocus();
            return;
        }

        if (Double.valueOf(labelPrice.getText()) == 0) {
            new Alert(Alert.AlertType.ERROR, "You must add at least one dish.").showAndWait();
            comboType.requestFocus();
            return;
        }

        if (datePicker.getValue().atTime(timePicker.getValue()).isBefore(LocalDateTime.now())) {
            new Alert(Alert.AlertType.ERROR, "You must select at minimum " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"))).showAndWait();
            datePicker.requestFocus();
            return;
        }


        if (!isAccepted()) return;

        Order order = new Order(client, datePicker.getValue().atTime(timePicker.getValue()), (Table) comboTables.getValue());

        for (ArrayList<OrderItem> items : orderItems.values()) {
            for (OrderItem item : items) {
                order.addOrderItem(item);
            }
        }
        restaurant.addOrder(order);
        try {
            HelperMethods.saveRestaurantToXml("restaurant.xml", restaurant);
            ((Stage) timePicker.getScene().getWindow()).close();
            HelperMethods.createStage(getClass().getResource("../views/customerDashboard.fxml"),
                    "Customer Dashboard | " + client.getName(),
                    c -> new CustomerDashboardController(restaurant, client)).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "The following error has occurred while saving:\n" + e.getMessage());
        }
    }

    private boolean isAccepted() throws IOException {
        SaveController saveController = new SaveController();
        HelperMethods.createStage(getClass().getResource("../views/save.fxml"), "Save", c -> saveController).showAndWait();
        return saveController.getIsAccepted();
    }

    public void buttonLogout() throws IOException {
        HelperMethods.createStage(getClass().getResource("../views/login.fxml"),
                "Login",
                c -> new LoginController(restaurant)).show();
        ((Stage) timePicker.getScene().getWindow()).close();

    }

    public void valueChanged() {
        fillTablesAvailableAt(datePicker.getValue());
    }

    public void buttonMyOrdersClicked() throws IOException {
        HelperMethods.createStage(getClass().getResource("../views/myOrders.fxml"),
                client.getName() + "'s orders",
                c -> new MyOrdersController(restaurant, client)).showAndWait();
    }
}
    
  
