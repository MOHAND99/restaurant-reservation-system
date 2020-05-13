package restaurant.order;

import adapters.OrderItemAdapter;
import restaurant.dish.Dish;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Objects;

@XmlJavaTypeAdapter(OrderItemAdapter.class)
public class OrderItem {
    private final Dish dish;
    private final int quantity;

    public OrderItem(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return dish.getPrice() * quantity;
    }

    public double getPriceWithTax() {
        return dish.getPriceWithTax() * quantity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (quantity != orderItem.quantity) return false;
        return Objects.equals(dish, orderItem.dish);
    }

    @Override
    public int hashCode() {
        int result = dish != null ? dish.hashCode() : 0;
        result = 31 * result + quantity;
        return result;
    }
}
