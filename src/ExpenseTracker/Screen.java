import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class Screen extends JFrame {
    private final Entry_Table Model;
    private final JTable table;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton addButton;
    private final JLabel balanceLabel;
    
    private final String dateFormat = "30/12/1991";
    private final String amountFormat = "29.69";
    
    private double balance;
    
     public Screen() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        }
        catch(Exception ex) {
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
        balanceLabel = new JLabel("Balance: "+ balance +" VND");
        
        addButton.addActionListener(e -> addEntry());
        
        
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);
        
        inputPanel.add(new JLabel("Type"));
        inputPanel.add(typeCombobox);
        
        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);
        
        inputPanel.add(new JLabel("Description"));
        inputPanel.add(descriptionField);

        HintInput(dateField,dateFormat);
        HintInput(amountField, amountFormat);
        
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
        // Get input values from input fields.
        String date = dateField.getText();
        String description = descriptionField.getText();
        String amountStr = amountField.getText();
        String type = (String)typeCombobox.getSelectedItem();
        double amount;
        
        // Validate input values.
        // you can the description and date to the validation
        if(amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try
        {
            amount = Double.parseDouble(amountStr);
        }
        catch(NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Convert expenses to negative values.
        if(type.equals("Expense"))
        {
            amount *= -1;
        }
        
        // Create a new entry and add it to the table.
        Entry entry = new Entry(amount, date, type, description);
        Model.addEntry(entry);
        
        // Update the balance and display the new balance.
        balance += amount;
        balanceLabel.setText("Balance: "+balance+" VND");
        
        // Clear input fields for the next entry.
        clearInputFields();
    }
    
    
    // Method to clear input fields.
    private void clearInputFields()
    {
        dateField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }
    
    public static void HintInput(JTextField textField, String str) {
        
        textField.setText(str);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // When the text field gains focus, clear the hint text
                if (textField.getText().equals(str)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK); // Set text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // When the text field loses focus and is empty, display the hint text
                if (textField.getText().isEmpty()) {
                    textField.setText(str);
                    textField.setForeground(Color.GRAY); // Set text color to gray
                }
            }
        } );
    }
    
}



