package gui.custom.controls;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import restaurant.dish.Dish;
import restaurant.order.OrderItem;

public class ProductQuantityView extends HBox {
    private final Label labelName;
    private final JFXButton buttonIncrease;
    private final Label labelQuantity;
    private final JFXButton buttonDecrease;
    private final Dish dish;
    private int quantity;

    public ProductQuantityView(Dish dish) {
        super();
        setSpacing(10);
        setPadding(new Insets(5));
        labelName = new Label(dish.getName());
        labelName.setStyle("-fx-font-size: 20px;");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        buttonIncrease = new JFXButton("+");
        buttonIncrease.setDisableVisualFocus(true);
        buttonIncrease.setPadding(new Insets(0, 10, 0, 10));
        buttonIncrease.setStyle("-fx-font-size: 20px; -fx-background-color: #24A0ED; -fx-text-fill: white");

        labelQuantity = new Label("0");
        labelQuantity.setStyle("-fx-font-size: 20px;");
        labelQuantity.setMinWidth(40);
        labelQuantity.setMaxWidth(40);
        labelQuantity.setAlignment(Pos.CENTER);
        buttonDecrease = new JFXButton("-");
        buttonDecrease.setDisableVisualFocus(true);
        buttonDecrease.setPadding(new Insets(0, 10, 0, 10));
        buttonDecrease.setStyle("-fx-font-size: 20px; -fx-background-color: #24A0ED; -fx-text-fill: white");
        this.dish = dish;
        this.getChildren().add(labelName);
        this.getChildren().add(region);
        this.getChildren().add(buttonIncrease);
        this.getChildren().add(labelQuantity);
        this.getChildren().add(buttonDecrease);

        buttonIncrease.setOnAction(event -> increase(1));
        buttonDecrease.setOnAction(event -> increase(-1));
    }

    public OrderItem getOrderItem() {
        return new OrderItem(dish, quantity);
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) return;
        this.quantity = quantity;
        labelQuantity.setText(String.valueOf(quantity));
    }

    private void increase(int value) {
        if (quantity + value < 0) return;
        quantity += value;
        labelQuantity.setText(String.valueOf(quantity));
    }
}
