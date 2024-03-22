package expense_income_tracker;

import java.util.Objects;

public class Entry {
    public int id;
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

    public Entry( double amount, String date, String type, String description,int id) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.description = description;
        this.id = id;
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
    public int getID() {return id;}

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean contain(Entry e) {
        if (this.date.contains(e.date)) {
            return true;
        }
        if (Objects.equals(this.type, e.type)) {
            return true;
        }
        if (this.amount == e.amount) {
            return true;
        }
        if (this.description.contains(e.description)) {
            return true;
        }

        return false;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
