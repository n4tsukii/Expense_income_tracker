
package expense_income_tracker;

import expense_income_tracker.Entry;

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author japan
 */
 public class Entry_Table extends AbstractTableModel  {
    
    private final List<Entry> entries;
    private final String[] columnNames = {"Date","Type", "Amount","Description"};
    
    public Entry_Table() {
        entries = new ArrayList<>();
        
    }
    
    public void addEntry(Entry ee) {
        entries.add(ee);
        
        fireTableRowsInserted(entries.size()-1,entries.size()-1);
    }

    @Override
    public int getRowCount() { return entries.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }
    
     @Override
    public String getColumnName(int column){ return columnNames[column]; }

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
