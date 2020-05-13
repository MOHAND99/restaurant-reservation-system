package information;

public abstract class Information {
    private final String schedule;
    private final String tableNum;

    protected Information(String schedule, String tableNum) {
        this.schedule = schedule;
        this.tableNum = tableNum;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getTableNum() {
        return tableNum;
    }
    
   
}
