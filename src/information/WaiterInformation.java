package information;

public class WaiterInformation extends Information {
    private final String price;
    private final String name;

    public WaiterInformation(String price, String name, String schedule, String tableNum) {
        super(schedule, tableNum);
        this.price = price;
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
