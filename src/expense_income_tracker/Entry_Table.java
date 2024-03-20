package expense_income_tracker;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class Entry_Table  extends AbstractTableModel {
    private  List<Entry> entries;
    private final String[] columnNames = {"Date","Type","Amount","Description"};
    
    public Entry_Table() {
        entries = new ArrayList<>();
    }
    //@Override
    public void addEntry(Entry ee) {
        entries.add(ee);
        fireTableRowsInserted(entries.size()-1,entries.size()-1);
    }
    //@Override
    public void adden(Entry ee) {
        entries.add(ee);
    }

    public void EditRow(int index, Entry entry) {
        entries.set(index, entry);
    }

    public void removeRow(int index) {
        entries.remove(index);
    }

    //@Override
    public void updateEntryTable(List<Entry> newList) {
        this.entries = newList;
        //fireTableDataChanged();
    }
    //@Override
    public List<Entry> returnAllEntries() {
        return entries;
    }

     //@Override
     public int getRowCount() {
         return entries.size();
     }

     @Override
     public int getColumnCount() {
         return columnNames.length;
     }

     @Override
     public String getColumnName(int column) {
         return columnNames[column];
     }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Entry ee = entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> ee.getDate();
            case 1 -> ee.getType();
            case 2 -> ee.getAmount();
            case 3 -> ee.getDescription();
            default -> null;
        };
    }

    
    
    
    

}
