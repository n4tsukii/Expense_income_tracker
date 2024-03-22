package expense_income_tracker;

import java.util.ArrayList;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableModel;


public class Screen extends JFrame {
    private final Entry_Table Model;
    private final JTable table;
    boolean editting = false;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton setButton;
    private final JButton searchButton;
    private final JLabel balanceLabel;
    private final JButton staticButton;
    private final JComboBox<String> statictype;
    private final JPopupMenu popupMenu;
    ArrayList<String> type = new ArrayList<String>();

    private double balance;

    private database db = new database();
     public Screen(){
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        }
        catch(Exception ex){
            System.err.println("Failed to Set FlatDarkLaf LookAndFeel");
        }
       
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.DARK_GRAY);
        UIManager.put("TextField.caretForeground", Color.RED);
        UIManager.put("ComboBox.foreground", Color.YELLOW);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", Color.BLACK);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", Color.ORANGE);
        UIManager.put("Label.foreground", Color.WHITE);

        balance = db.balanceCheck();
        Model = new Entry_Table();
        table = new JTable(Model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        dateField = new JTextField(10);
        descriptionField = new JTextField(10);
        amountField = new JTextField(10);
        typeCombobox = new JComboBox<>(new String[] {"Income", "Expense"});
        type.add("Income"); type.add("Expense");
        statictype = new JComboBox<>(new String[] {"Month", "Week", "Day"});
        popupMenu = new JPopupMenu();


        JMenuItem menuItemEdit = new JMenuItem("Edit");
        JMenuItem menuItemDel = new JMenuItem("Delete");
        staticButton = new JButton("Thong Ke");
        setButton = new JButton("Set");
        searchButton = new JButton("Search");
        balanceLabel = new JLabel("Balance: "+ formatDouble(balance) +" VND");
        popupMenu.add(menuItemEdit);
        popupMenu.add(menuItemDel);
        menuItemEdit.addActionListener(e -> EditOption());
        menuItemDel.addActionListener(e -> DelOption());
        table.setComponentPopupMenu(popupMenu);
        setButton.addActionListener(e -> setEntry());
        searchButton.addActionListener(e -> search());
        staticButton.addActionListener(e -> staticpopup());

        table.setComponentPopupMenu(popupMenu);
        table.setFillsViewportHeight(true);
        
        JPanel inputPanel = new JPanel();

        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);
        updateDateField(dateField);
        
        inputPanel.add(new JLabel("Type"));
        inputPanel.add(typeCombobox);
        
        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);
        
        inputPanel.add(new JLabel("Description"));
        inputPanel.add(descriptionField);

        inputPanel.add(setButton);
        inputPanel.add(searchButton);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(staticButton);
        bottomPanel.add(statictype);
        bottomPanel.add(balanceLabel, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        setTitle("Personal Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
     }


    private void staticpopup() {
        JOptionPane.showMessageDialog(null, "Balance " + formatDouble(balance) + " VND", "Thong Ke", JOptionPane.INFORMATION_MESSAGE);
    }
       
    private void setEntry() {
        String date = dateField.getText();
        String description = descriptionField.getText();
        String amountStr = amountField.getText();
        String type = (String)typeCombobox.getSelectedItem();
        double amount;

        if(amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            amount = Double.parseDouble(amountStr);
        }
        catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(type.equals("Expense"))
        {
            amount *= -1;
        }

        if (balance <= 0 && amount < 0) {
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure your want to expense more !? Your balance is low", "Warning!!", JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.CANCEL_OPTION)
                return ;
        }

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //date = dateFormat.format(date);
        Entry entry = new Entry(amount, date, type, description);
        if (!editting) {
            db.insertToDatabase(entry);
            //Model.addEntry(entry);

        } else {
            int index = table.getSelectedRow();
            table.clearSelection();
            Model.EditRow(index, entry);

            editting = false;
        }
        balance = db.balanceCheck();
        balanceLabel.setText("Balance: "+ formatDouble(balance) +" VND");
        reload();
        clearInputFields();
    }


    private void clearInputFields() {
        updateDateField(dateField);
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }

    public void displaceThis(Entry_Table new_table) {
        Model.updateEntryTable(new_table.returnAllEntries());
        Model.fireTableDataChanged();
        //System.out.println("WELLCOME !!");
    }

    public void reload() {
         //database db = new database();
         displaceThis(db.returnAll());
         balance = db.balanceCheck();


    }

    private static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(value);
    }
    private static void updateDateField(JTextField dateField) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(currentDate);
        dateField.setText(formattedDate);
    }

    private void EditOption() {
        int index = table.getSelectedRow();
        balance -= Double.parseDouble(Model.getValueAt(index, 2).toString());
        dateField.setText(Model.getValueAt(index,0).toString());
        typeCombobox.setSelectedItem(Model.getValueAt(index, 1).toString());
        amountField.setText(Model.getValueAt(index, 2).toString());
        descriptionField.setText(Model.getValueAt(index, 3).toString());
        editting = true;
    }

    private void DelOption() {
        if (editting) {
            JOptionPane.showMessageDialog(this, "Complete editting first", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int index = table.getSelectedRow();
            balance -= Double.parseDouble(Model.getValueAt(index, 2).toString());


            int id = Model.getEntry(index).getID();
            Model.removeRow(index);
            db.removeThis(id);
            balanceLabel.setText("Balance: " + formatDouble(balance) + " VND");

        }
    }

    public void search() {
        if (editting) {
            JOptionPane.showMessageDialog(this, "Complete editting first", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int index = table.getSelectedRow();
            String date = Model.getValueAt(index, 0).toString();
            String type = Model.getValueAt(index, 1).toString();
            double amount = Double.parseDouble(Model.getValueAt(index, 2).toString());
            String des = Model.getValueAt(index, 3).toString();
            JTable newtable = new JTable((TableModel) Model.searchby(date, type, amount, des));

        }
    }
}
