package gui.controllers;

import gui.custom.controls.ProductQuantityView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.dish.Dish;
import restaurant.order.OrderItem;

import java.util.ArrayList;

public class OrderController {
    private final ArrayList<Dish> dishes;
    @FXML
    private ScrollPane scrollPane;
    private ArrayList<OrderItem> orderItems;

    public OrderController(ArrayList<Dish> dishes, ArrayList<OrderItem> oldSelectedItems) {
        this.dishes = dishes;
        this.orderItems = oldSelectedItems;
    }

    public void initialize() {
        VBox vbox = new VBox();
        for (Dish dish : dishes) {
            ProductQuantityView productQuantityView = new ProductQuantityView(dish);
            OrderItem tempItem = orderItems.stream().filter(item -> item.getDish().equals(dish)).findFirst().orElse(null);
            if (tempItem != null) {
                productQuantityView.setQuantity(tempItem.getQuantity());
            }
            vbox.getChildren().add(productQuantityView);
        }
        scrollPane.setContent(vbox);
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void buttonCancelClicked() {
        orderItems = null;
        ((Stage) scrollPane.getScene().getWindow()).close();
    }

    public void buttonOkClicked() {

        orderItems = new ArrayList<>();

        for (Node view : ((VBox) scrollPane.getContent()).getChildren()) {
            OrderItem item = ((ProductQuantityView) view).getOrderItem();
            if (item.getQuantity() > 0) orderItems.add(item);
        }
        ((Stage) scrollPane.getScene().getWindow()).close();
    }
}
