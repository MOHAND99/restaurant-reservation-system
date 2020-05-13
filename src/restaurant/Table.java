package restaurant;

import adapters.TableAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(TableAdapter.class)
public class Table {

    private final short numOfSeats;
    private final short num;
    private final boolean smoking;

    public Table(short numOfSeats, short num, boolean smoking) {
        this.numOfSeats = numOfSeats;
        this.num = num;
        this.smoking = smoking;
    }

    public short getNum() {
        return num;
    }

    public short getNumOfSeats() {
        return numOfSeats;
    }

    public boolean isSmoking() {
        return smoking;
    }

    @Override
    public String toString() {
        return "table number: " + num +
                ", number of seats: " + numOfSeats +
                ", is smoking: " + smoking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (numOfSeats != table.numOfSeats) return false;
        if (num != table.num) return false;
        return smoking == table.smoking;
    }

    @Override
    public int hashCode() {
        int result = (int) numOfSeats;
        result = 31 * result + (int) num;
        result = 31 * result + (smoking ? 1 : 0);
        return result;
    }
}
