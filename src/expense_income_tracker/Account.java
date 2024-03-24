package expense_income_tracker;

public class Account {
    private double balance;

    public Account(double balance) {
        this.balance = balance;
        
    }
    public Account() {
        this.balance = 0;
    }
    
    private void setBalance(double amount) {
        this.balance = amount;
    }
    
    private double getBalance() {
        return this.balance;
    }
    
//    public void addAmount(double amount) {
//        setBalance(this.balance + amount);
//    }
    
//    void addBalance(Entry ee) {
//        this.addAmount(ee.getAmount());
//    }
}
