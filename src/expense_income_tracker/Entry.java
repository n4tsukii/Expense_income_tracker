package expense_income_tracker;

public class Entry {
    public double amount;
    public String date;
    public String type;
    public String description;
    
    public Entry( double amount, String date, String type, String description) {
        
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.description = description;
        
    }
    public Entry() {}

    public double getAmount(){
        return amount;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    
}
