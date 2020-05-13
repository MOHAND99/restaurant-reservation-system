package adapters;

import adapted.AdaptedTable;
import restaurant.Table;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// This adapter is necessary because we can't work directly with Table as it doesn't have no-arg constructor.
public class TableAdapter extends XmlAdapter<AdaptedTable, Table> {
    @Override
    public Table unmarshal(AdaptedTable v) throws Exception {
        return new Table(v.getNumberOfSeats(), v.getNumber(), v.isSmoking());
    }

    @Override
    public AdaptedTable marshal(Table v) throws Exception {
        AdaptedTable adaptedTable = new AdaptedTable();
        adaptedTable.setNumber(v.getNum());
        adaptedTable.setNumberOfSeats(v.getNumOfSeats());
        adaptedTable.setSmoking(v.isSmoking());
        return adaptedTable;
    }
}
