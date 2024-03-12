import expense_income_tracker.*;

public class ExpenseIncomeTracker {

    public void Process(Account account, Entry ee) {
        account.addAmount(ee.getAmount());
//        account.addEntry_toList(ee);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        
         new Screen().setLocationRelativeTo(null);
    }
    
}

