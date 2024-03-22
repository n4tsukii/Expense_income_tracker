package expense_income_tracker;

//import jdk.internal.jmod.JmodFile;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class Entry_Table  extends AbstractTableModel {
    private  List<Entry> entries;
    private final String[] columnNames = {"Date","Type","Amount","Description"};
    
    public Entry_Table() {
        entries = new ArrayList<>();
    }

    public Entry_Table searchby(Entry temp) {
        Entry_Table result = new Entry_Table();
        for(Entry entry : entries) {
           if (entry.contain(temp)) {
                result.adden(entry);
           }
        }
        return result;
    }

    public void addEntry(Entry ee) {
        entries.add(ee);
        fireTableRowsInserted(entries.size()-1,entries.size()-1);
    }

    public void adden(Entry ee) {
        entries.add(ee);
    }

    public void EditRow(int index, Entry entry) {
        entries.set(index, entry);
    }

    public void removeRow(int index) {
        entries.remove(index);
    }


    public void updateEntryTable(List<Entry> newList) {
        this.entries = newList;
        //fireTableDataChanged();
    }

    public List<Entry> returnAllEntries() {
        return entries;
    }

     public int getRowCount() {
         return entries.size();
     }
     public Entry getEntry(int index) {return entries.get(index);}

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
