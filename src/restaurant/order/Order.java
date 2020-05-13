package restaurant.order;

import adapters.OrderAdapter;
import restaurant.Table;
import restaurant.user.Client;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@XmlJavaTypeAdapter(OrderAdapter.class)
public class Order {
    private final Client client;
    private final LocalDateTime dateTime;
    private final Table table;
    private final ArrayList<OrderItem> orderItems;
    private double totalPrice;

    public Order(Client client, LocalDateTime dateTime, Table table) {
        this.client = client;
        this.dateTime = dateTime;
        this.table = table;
        this.orderItems = new ArrayList<>();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        totalPrice += orderItem.getPriceWithTax();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Table getTable() {
        return table;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (Double.compare(order.totalPrice, totalPrice) != 0) return false;
        if (!Objects.equals(client, order.client)) return false;
        if (!Objects.equals(dateTime, order.dateTime)) return false;
        if (!Objects.equals(table, order.table)) return false;
        return Objects.equals(orderItems, order.orderItems);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = client != null ? client.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

