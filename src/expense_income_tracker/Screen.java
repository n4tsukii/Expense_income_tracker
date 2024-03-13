package expense_income_tracker;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;

public class Screen extends JFrame {
    private static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(value);
    }
    private static void updateDateField(JTextField dateField) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        dateField.setText(formattedDate);
    }

    private static String reformatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    private final Entry_Table Model;
    private final JTable table;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton addButton;
    private final JLabel balanceLabel;
    
    private double balance;
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

        balance = 0.0;
        
        Model = new Entry_Table();
        table = new JTable(Model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        table.setFillsViewportHeight(true);
        
        dateField = new JTextField(10);
        descriptionField = new JTextField(10);
        amountField = new JTextField(10);
        typeCombobox = new JComboBox<>(new String[] {"Expense","Income"});
        
        addButton = new JButton("Add");
        balanceLabel = new JLabel("Balance: "+ formatDouble(balance) +" VND");
        
        addButton.addActionListener(e -> addEntry());
        
        
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

        inputPanel.add(addButton);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(balanceLabel);
        
        add(inputPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        
        setTitle("Personal Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setVisible(true);
     }
       
    private void addEntry()
    {
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
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure your want to expense more !?", "Warning, Balance is low!!", JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.CANCEL_OPTION)
                return ;
        }

        date = reformatDate(date);
        Entry entry = new Entry(amount, date, type, description);

        //insert entry to database
        database db = new database();
        db.insertToDatabase(entry);


        Model.addEntry(entry);
        balance += amount;

        balanceLabel.setText("Balance: "+ formatDouble(balance) +" VND");

        clearInputFields();
    }

    private void clearInputFields() {
        updateDateField(dateField);
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }
    
}



