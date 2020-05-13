package adapters;

import adapted.AdaptedOrderItem;
import restaurant.order.OrderItem;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class OrderItemAdapter extends XmlAdapter<AdaptedOrderItem, OrderItem> {
    @Override
    public OrderItem unmarshal(AdaptedOrderItem v) throws Exception {
        return new OrderItem(v.getDish(), v.getQuantity());
    }

    @Override
    public AdaptedOrderItem marshal(OrderItem v) throws Exception {
        AdaptedOrderItem adaptedOrderItem = new AdaptedOrderItem();
        adaptedOrderItem.setDish(v.getDish());
        adaptedOrderItem.setQuantity(v.getQuantity());
        return adaptedOrderItem;
    }
}
