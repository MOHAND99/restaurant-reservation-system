package information;

public class ManagerInformation extends Information {
    private final String price;
    private final String name;
    private final String orderItems;

    public ManagerInformation(String price, String name, String orderItems, String schedule, String tableNum) {
        super(schedule, tableNum);
        this.price = price;
        this.name = name;
        this.orderItems = orderItems;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getOrderItems() {
        return orderItems;
    }
    
}
