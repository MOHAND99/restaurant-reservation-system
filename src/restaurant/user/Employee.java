package restaurant.user;

import restaurant.order.Order;
import restaurant.order.OrderItem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class Employee extends User {

    protected Employee(String username, String password, String name) {
        super(username, password, name);
    }

    public ArrayList<String> getClients(ArrayList<Order> orders) {
        ArrayList<String> result;
        result = new ArrayList(orders.size());

        for (int i = 0; i < orders.size(); i++) {
            result.add(orders.get(i).getClient().getName());
        }

        return result;
    }

    public ArrayList<String> getSchedules(ArrayList<Order> orders) {
        ArrayList<String> result;
        result = new ArrayList(orders.size());

        for (int i = 0; i < orders.size(); i++) {
            result.add(orders.get(i).getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
        }

        return result;
    }

    public ArrayList<String> getReservedTables(ArrayList<Order> orders) {
        ArrayList<String> result;
        result = new ArrayList(orders.size());

        for (int i = 0; i < orders.size(); i++) {
            result.add("#" + orders.get(i).getTable().getNum());
        }

        return result;
    }

    public ArrayList<String> getOrdersItems(ArrayList<Order> orders) {
        ArrayList<String> result;
        result = new ArrayList(orders.size());

        for (int i = 0; i < orders.size(); i++) {
            StringBuilder builder = new StringBuilder();
            for (OrderItem item : orders.get(i).getOrderItems()) {
                builder.append(item.getDish().getName()).append(" x").append(item.getQuantity()).append('\n');
            }
            result.add(builder.toString());
        }

        return result;
    }

    public ArrayList<String> getPrices(ArrayList<Order> orders) {
        ArrayList<String> result;
        result = new ArrayList(orders.size());

        for (int i = 0; i < orders.size(); i++) {
            result.add(String.valueOf(orders.get(i).getTotalPrice()));
        }

        return result;
    }
 
}
