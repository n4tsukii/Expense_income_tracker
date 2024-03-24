package expense_income_tracker;

import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.*;


public class Screen extends JFrame {
    private final Entry_Table Model = new Entry_Table();
    private final TableRowSorter<Entry_Table> sorter = new TableRowSorter<Entry_Table>(Model);
    private final JTable table;
    boolean editting = false;
    boolean searching = false;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton setButton;
    private final JButton searchButton;
    private final JTextField searchField;
    private final JLabel balanceLabel;
    private final JButton staticButton;
    private final JComboBox<String> statictype;
    private final JPopupMenu popupMenu;
    private final JButton refreshButton;
    ArrayList<String> type = new ArrayList<String>();
    private double balance;
    private String currentSearch;

    private static String date_format = "yyyy-MM-dd";

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

        table = new JTable(Model);
        table.setRowSorter(sorter);
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
        //inputPanel.add(searchButton);
        //JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

         refreshButton = new JButton("\uD83D\uDD04");
         refreshButton.setBackground(Color.RED);
         refreshButton.setPreferredSize(new Dimension(25,25));
         refreshButton.addActionListener(e->reload());
         inputPanel.add(refreshButton);


        JPanel bottomPanel = new JPanel();


        JPanel bot1= new JPanel(new FlowLayout(FlowLayout.LEFT)) ;
        JPanel bot2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        BoxLayout boxLayout = new BoxLayout(bottomPanel,BoxLayout.X_AXIS) ;
        bottomPanel.setLayout(boxLayout);

        bot1.add(staticButton,BorderLayout.WEST);
        bot1.add(statictype,BorderLayout.WEST);
        bot1.add(balanceLabel, BorderLayout.WEST);


        searchField = new JTextField(30);
        bot2.add(searchField);
        bot2.add(searchButton);

        bottomPanel.add(bot1);
        bottomPanel.add(bot2);
        
        add(inputPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        
        setTitle("Personal Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
     }



       
    private void setEntry() {
        String date = dateField.getText();
        String description = descriptionField.getText();
        String amountStr = amountField.getText();
        String type = (String)typeCombobox.getSelectedItem();
        double amount;

        if(!isDateValid(date)) {
            return;
        }

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
        if(amount <0) {
            type = "Expense";
        }

        if(type.equals("Expense") && amount >0)
        {
            amount *= -1;
        }



        if ((balance + amount) <0) {
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure your want to expense more !? Your balance is low", "Warning!!", JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.CANCEL_OPTION)
                return ;
        }

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //date = dateFormat.format(date);
        Entry entry = new Entry(amount, date, type, description);
        if (!editting) {
            db.insertToDatabase(entry);
            Model.addEntry(entry);

        } else {
            int index = table.getSelectedRow();
            table.clearSelection();
            db.editEntry(Model.getEntry(index).getID(),entry);
            Model.EditRow(index, entry);
            if (searching) {
                displaceThis(db.search(currentSearch));
            }
            editting = false;
        }
        balanceUpdate();

        //reload();
        clearInputFields();
    }


    private void clearInputFields() {
        updateDateField(dateField);
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }

    public static boolean isDateValid(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(date_format);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null,"incorrect date or date formate","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void displaceThis(Entry_Table new_table) {
        Model.updateEntryTable(new_table.returnAllEntries());
        Model.fireTableDataChanged();

    }

    public void balanceUpdate() {
        balance = db.balanceCheck();
        balanceLabel.setText("Balance: "+ formatDouble(balance) +" VND");
    }

    public void reload() {
         //database db = new database();
         displaceThis(db.returnAll());
         balanceUpdate();
         clearInputFields();
         editting = false;
         searching = false;
         currentSearch = "";
         searchField.setText("");

    }

    private static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(value);
    }
    private static void updateDateField(JTextField dateField) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_format);
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
            displaceThis(db.returnAll());
            if (searching) {
                String text = searchField.getText();
                if (!text.isEmpty()) {
                    searching = true;
                    displaceThis(db.search(text));
                } else {
                    searching = false;
                    displaceThis(db.returnAll());
                }
            }
            balanceUpdate();
        }
    }

    public void search() {
        if (editting) {
            JOptionPane.showMessageDialog(this, "Complete editting first", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            currentSearch = searchField.getText();
            if (!currentSearch.isEmpty()) {
                searching = true;
                displaceThis(db.search(currentSearch));
            } else {
                reload();
            }
        }
    }

    public void thongke() {


    }

    private void staticpopup() {
        int i=0;
        String stype = (String) statictype.getSelectedItem(); //"Month", "Week", "Day"
        if(stype.equals("Month")) {
            i = 30;
        }
        if(stype.equals("Week")) {
            i = 6;
        }
        double income  =  db.thongke("income",i);
        double expense =  db.thongke("expense",i);
        //JOptionPane.showMessageDialog(null, "Your income and expense and expenses last"+stype+"is: "+income+ "and "+expense);

        JOptionPane.showMessageDialog(null, "Income: "+income+ " VND\nExpense: "+expense+" VND\nTotal: " + (income-expense) + " VND", "Your income and expense last "+stype, JOptionPane.INFORMATION_MESSAGE);

    }
}
