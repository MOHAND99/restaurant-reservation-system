package restaurant;

import restaurant.dish.Dish;
import restaurant.order.Order;
import restaurant.user.Client;
import restaurant.user.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement
public class Restaurant {
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private final ArrayList<User> users = new ArrayList<>();

    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    private final ArrayList<Table> tables = new ArrayList<>();


    @XmlElementWrapper(name = "dishes")
    @XmlElement(name = "dish")
    private final ArrayList<Dish> dishes = new ArrayList<>();

    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "order")
    private final TreeSet<Order> orders = new TreeSet<>((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime())); // log(n) for adding elements, but prevents from too many low-performance sorting.


    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public SortedSet<Order> getOrders() {
        return orders;
    }

    public List<Table> getUnreservedTablesAt(LocalDate localDate) {
        List<Table> reserved = orders.stream().filter(o -> o.getDateTime().toLocalDate().equals(localDate)).map(o -> o.getTable()).collect(Collectors.toList());
        return tables.stream().filter(t -> !reserved.contains(t)).collect(Collectors.toList());
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getOneDayOrders(LocalDate date) {
        ArrayList<Order> dayOrders = new ArrayList<>(orders.size());

        for (Order order : orders) {
            if (order.getDateTime().toLocalDate().equals(date)) {
                dayOrders.add(order);
            } else if (order.getDateTime().toLocalDate().isAfter(date)) {
                // Because orders are always sorted by date.
                break;
            }
        }
        return dayOrders;
    }
    
    public double getEarn(LocalDate date) {
        double earned = 0;
        for (Order o : orders)
            if (o.getDateTime().toLocalDate().equals(date)) {
                earned += o.getTotalPrice();
            }
        return earned;
    }

    public ClientPayments getBestCustomer() {
        if (orders.isEmpty()) return null;
        Object[] clients = users.stream().filter(u -> u instanceof Client).toArray();
        Client bestClient = null;
        double bestInvoice = 0;
        for (Object client : clients) {
            double currentInvoice = orders.stream()
                    .filter(o -> o.getClient().equals(client))
                    .mapToDouble(o -> o.getTotalPrice()).sum();

            if (currentInvoice > bestInvoice) {
                bestClient = (Client) client;
                bestInvoice = currentInvoice;
            }
        }
        return new ClientPayments(bestClient, bestInvoice);
    }
    
    public DayIncome getMaxDayIncome() {
        if (orders.isEmpty()) return null;
        ArrayList<Order> ordersArr = new ArrayList<>(orders.size());
        ordersArr.addAll(orders);
        
        ArrayList<LocalDate> dates = new ArrayList<>(orders.size());
        dates.addAll(getOrdersDates());
        
        double maxEarn = 0;
        LocalDate maxDate = null;
        for (LocalDate ld : dates){ 
            double income = getEarn(ld);
            if(income > maxEarn) {
                maxEarn = income; 
                maxDate = ld;
            }
        }
        return new DayIncome(maxEarn, maxDate);

    }
    
    private HashSet<LocalDate> getOrdersDates(){
        HashSet<LocalDate> dates = new HashSet<>(orders.size());  
        for (Order o : orders) dates.add(o.getDateTime().toLocalDate());
        return dates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        if (!Objects.equals(users, that.users)) return false;
        if (!Objects.equals(tables, that.tables)) return false;
        if (!Objects.equals(dishes, that.dishes)) return false;
        return Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        int result = users != null ? users.hashCode() : 0;
        result = 31 * result + (tables != null ? tables.hashCode() : 0);
        result = 31 * result + (dishes != null ? dishes.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }
}
