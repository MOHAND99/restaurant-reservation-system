package adapted;

import adapters.LocalDateTimeAdapter;
import adapters.UserAdapter;
import restaurant.Table;
import restaurant.order.OrderItem;
import restaurant.user.Client;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedOrder {
    @XmlElement(name = "user")
    @XmlJavaTypeAdapter(UserAdapter.class)
    private Client client;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dateTime;
    private Table table;
    private Double totalPrice;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private ArrayList<OrderItem> items;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

}
