package adapters;

import adapted.AdaptedOrder;
import restaurant.order.Order;
import restaurant.order.OrderItem;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OrderAdapter extends XmlAdapter<AdaptedOrder, Order> {
    @Override
    public Order unmarshal(AdaptedOrder v) throws Exception {
        Order result = new Order(v.getClient(), v.getDateTime(), v.getTable());
        for (OrderItem item : v.getItems()) {
            result.addOrderItem(item);
        }
        return result;
    }

    @Override
    public AdaptedOrder marshal(Order v) throws Exception {
        AdaptedOrder result = new AdaptedOrder();
        result.setClient(v.getClient());
        result.setDateTime(v.getDateTime());
        result.setTable(v.getTable());
        result.setItems(v.getOrderItems());
        result.setTotalPrice(v.getTotalPrice());
        return result;
    }
}
