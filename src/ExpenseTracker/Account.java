
import java.util.ArrayList;
import java.util.List;

public class Account {
    private double balance;
//    private List<Entry> entry_table;
    
    
    public Account(double balance) {
        this.balance = balance;
//        entry_table= new ArrayList<>();
        
    }
    public Account() {
        this.balance = 0;
//        entry_table= new ArrayList<>();
    }
    
    private void setBalance(double amount) {
        this.balance = amount;
    }
    
    private double getBalance() {
        return this.balance;
    }
    
    void addAmount(double amount) {
        setBalance(this.balance + amount);
    }
    
    void addBalance(Entry ee) {
        this.addAmount(ee.getAmount());
    }
        
    
//    public void addEntry_toList(Entry ee) {
//        entry_table.add(ee);
//    }
        
    
}
