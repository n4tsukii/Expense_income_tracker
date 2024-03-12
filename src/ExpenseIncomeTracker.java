import expense_income_tracker.*;

public class ExpenseIncomeTracker {

    public void Process(Account account, Entry ee) {
        account.addAmount(ee.getAmount());
    }

    public static void main(String[] args) {
         new Screen().setLocationRelativeTo(null);
    }
}