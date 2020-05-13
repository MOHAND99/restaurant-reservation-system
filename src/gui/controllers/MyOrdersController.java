package gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import restaurant.Restaurant;
import restaurant.order.OrderItem;
import restaurant.user.Client;

import java.time.format.DateTimeFormatter;

public class MyOrdersController {
    @FXML
    private ScrollPane root;

    private final Restaurant restaurant;
    private final Client client;

    public MyOrdersController(Restaurant restaurant, Client client) {
        this.restaurant = restaurant;
        this.client = client;
    }

    public void initialize() {
        Object[] orders =
                restaurant.getOrders().stream()
                        .filter(order -> order.getClient().equals(client))
                        .map(order -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append(String.format("Table: %d, Date and time: %s, Total Price: %.2f LE\n", order.getTable().getNum(), order.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")), order.getTotalPrice()));
                            for (OrderItem item : order.getOrderItems()) {
                                builder.append(String.format("\tDish: %s, Quantity: %d, Total price with tax: %.2f LE\n", item.getDish().getName(), item.getQuantity(), item.getPriceWithTax()));
                            }
                            return builder.toString();
                        })
                        .toArray();

        VBox vbox = new VBox();
        for (Object order : orders) {
            Label label = new Label((String) order);
            label.setPadding(new Insets(10));
            vbox.getChildren().add(label);
        }
        if (orders.length == 0) {
            Label label = new Label("You don't have any orders.");
            label.setPadding(new Insets(20));
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 18px;");
            vbox.getChildren().add(label);
        }
        root.setContent(vbox);
    }
}
