package information;

public class CookerInformation extends Information {
    private final String orderItems;

    public CookerInformation(String orderItems, String schedule, String tableNum) {
        super(schedule, tableNum);
        this.orderItems = orderItems;
    }

    public String getOrderItems() {
        return orderItems;
    }
}
