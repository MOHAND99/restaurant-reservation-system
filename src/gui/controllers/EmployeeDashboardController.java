package gui.controllers;

import com.jfoenix.controls.JFXButton;
import gui.HelperMethods;
import information.CookerInformation;
import information.ManagerInformation;
import information.WaiterInformation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.Restaurant;
import restaurant.order.Order;
import restaurant.user.Cooker;
import restaurant.user.Employee;
import restaurant.user.Manager;
import restaurant.user.Waiter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeDashboardController {

    private final Restaurant restaurant;
    private final Employee employee;
    private final ArrayList<Order> todaySortedOrders;

    @FXML
    private TableView ordersData;

    @FXML
    private VBox dashboard;

    public EmployeeDashboardController(Restaurant restaurant, Employee employee) {
        this.restaurant = restaurant;
        this.employee = employee;
        todaySortedOrders = new ArrayList<>();
        todaySortedOrders.addAll(restaurant.getOneDayOrders(LocalDate.now()));
    }

    public void initialize() {
        if (employee instanceof Manager) getManagerDashboard();
        else if (employee instanceof Waiter) getWaiterDashboard();
        else if (employee instanceof Cooker) getCookerDashboard();
        ordersData.setPlaceholder(new Label("No orders today to display"));
        JFXButton logout = new JFXButton("Logout");
        logout.setButtonType(JFXButton.ButtonType.RAISED);
        logout.setStyle("-fx-background-color:WHITE;-fx-font-size:14px;");
        logout.setMinWidth(150);
        logout.setPrefHeight(40);
        logout.setOnAction(event -> logoutClicked());
        dashboard.getChildren().add(logout);
    }

    public void logoutClicked() {
        try {
            HelperMethods.createStage(getClass().getResource("../views/login.fxml"),
                    "Login",
                    c -> new LoginController(restaurant)).show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error has occurred.\n" + e.getMessage()).showAndWait();
        }
        ((Stage) dashboard.getScene().getWindow()).close();
    }

    public void getManagerDashboard() {
        Label earned = new Label("today's earned = "+ restaurant.getEarn(LocalDate.now()) + "LE");/* */ 
        JFXButton statistics = new JFXButton("Statistics");
        statistics.setButtonType(JFXButton.ButtonType.RAISED);
        statistics.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
        statistics.setPrefHeight(40);
        statistics.setMinWidth(150);
        statistics.setOnAction(event -> statisticsClicked());

        JFXButton controlPanel = new JFXButton("Control Panel");
        controlPanel.setButtonType(JFXButton.ButtonType.RAISED);
        controlPanel.setStyle("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
        controlPanel.setMinWidth(150);
        controlPanel.setPrefHeight(40);
        controlPanel.setOnAction(event -> controlPanelClicked());

        ArrayList<String> schedules, names, tablesNum, ordersItems, prices;
        schedules = new ArrayList<>(todaySortedOrders.size());
        names = new ArrayList<>(todaySortedOrders.size());
        tablesNum = new ArrayList<>(todaySortedOrders.size());
        ordersItems = new ArrayList<>(todaySortedOrders.size());
        prices = new ArrayList<>(todaySortedOrders.size());

        schedules.addAll(employee.getSchedules(todaySortedOrders));
        names.addAll(employee.getClients(todaySortedOrders));
        tablesNum.addAll(employee.getReservedTables(todaySortedOrders));
        ordersItems.addAll(employee.getOrdersItems(todaySortedOrders));
        prices.addAll(employee.getPrices(todaySortedOrders));

        TableColumn<String, ManagerInformation> timeColumn = new TableColumn<>("Schedule");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<String, ManagerInformation> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, ManagerInformation> tableNumColumn = new TableColumn<>("Table no.");
        tableNumColumn.setCellValueFactory(new PropertyValueFactory<>("tableNum"));

        TableColumn<String, ManagerInformation> orderItemsColumn = new TableColumn<>("Dishes");
        orderItemsColumn.setCellValueFactory(new PropertyValueFactory<>("orderItems"));

        TableColumn<String, ManagerInformation> priceColumn = new TableColumn<>("Tot. Price (LE)");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        ordersData.getColumns().addAll(timeColumn, nameColumn, tableNumColumn, orderItemsColumn, priceColumn);

        for (int i = 0; i < todaySortedOrders.size(); i++)
            ordersData.getItems().add(new ManagerInformation(prices.get(i), names.get(i), ordersItems.get(i), schedules.get(i), tablesNum.get(i)));

        dashboard.getChildren().add(earned);
        HBox hbox = new HBox(statistics, controlPanel);
        hbox.setSpacing(10);
        dashboard.getChildren().add(hbox);
    }

    private void controlPanelClicked() {
        try {
            HelperMethods.createStage(getClass().getResource("../views/controlPanel.fxml"), "Control Panel", c -> new ControlPanelController(restaurant)).showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error has occurred.\n" + e.getMessage()).showAndWait();
        }
    }

    private void statisticsClicked() {
        try {
            HelperMethods.createStage(getClass().getResource("../views/statistics.fxml"), "Manager Statistics", c -> new StatisticsController(restaurant)).showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error has occurred.\n" + e.getMessage()).showAndWait();
        }
    }

    public void getCookerDashboard() {
        ArrayList<String> schedules, tablesNum, ordersItems;
        schedules = new ArrayList<>(todaySortedOrders.size());
        tablesNum = new ArrayList<>(todaySortedOrders.size());
        ordersItems = new ArrayList<>(todaySortedOrders.size());

        schedules.addAll(employee.getSchedules(todaySortedOrders));
        tablesNum.addAll(employee.getReservedTables(todaySortedOrders));
        ordersItems.addAll(employee.getOrdersItems(todaySortedOrders));

        TableColumn<String, CookerInformation> timeColumn = new TableColumn<>("Schedule");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<String, ManagerInformation> tableNumColumn = new TableColumn<>("Table no.");
        tableNumColumn.setCellValueFactory(new PropertyValueFactory<>("tableNum"));

        TableColumn<String, ManagerInformation> orderItemsColumn = new TableColumn<>("Dishes");
        orderItemsColumn.setCellValueFactory(new PropertyValueFactory<>("orderItems"));

        ordersData.getColumns().addAll(timeColumn, tableNumColumn, orderItemsColumn);

        for (int i = 0; i < todaySortedOrders.size(); i++)
            ordersData.getItems().add(new CookerInformation(ordersItems.get(i), schedules.get(i), tablesNum.get(i)));
    }

    public void getWaiterDashboard() {
        ArrayList<String> schedules, tablesNum, prices, names;
        schedules = new ArrayList<>(todaySortedOrders.size());
        tablesNum = new ArrayList<>(todaySortedOrders.size());
        prices = new ArrayList<>(todaySortedOrders.size());
        names = new ArrayList<>(todaySortedOrders.size());

        schedules.addAll(employee.getSchedules(todaySortedOrders));
        tablesNum.addAll(employee.getReservedTables(todaySortedOrders));
        prices.addAll(employee.getPrices(todaySortedOrders));
        names.addAll(employee.getClients(todaySortedOrders));

        TableColumn<String, CookerInformation> timeColumn = new TableColumn<>("Schedule");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));

        TableColumn<String, CookerInformation> tableNumColumn = new TableColumn<>("Table no.");
        tableNumColumn.setCellValueFactory(new PropertyValueFactory<>("tableNum"));

        TableColumn<String, CookerInformation> pricesColumn = new TableColumn<>("Tot. price (LE)");
        pricesColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<String, ManagerInformation> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        ordersData.getColumns().addAll(timeColumn, nameColumn, tableNumColumn, pricesColumn);

        for (int i = 0; i < todaySortedOrders.size(); i++)
            ordersData.getItems().add(new WaiterInformation(prices.get(i), names.get(i), schedules.get(i), tablesNum.get(i)));

    }
}
