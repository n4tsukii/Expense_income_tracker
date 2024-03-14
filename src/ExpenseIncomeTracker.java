import expense_income_tracker.*;

public class ExpenseIncomeTracker {
    public void Process(Account account, Entry ee) {
        account.addAmount(ee.getAmount());
    }

    public static void main(String[] args) {
        Screen screen = new Screen();

        screen.setLocationRelativeTo(null);

        database db = new database();
        Entry_Table SQLdata = db.returnAll();

        screen.displaceThis(SQLdata);

    }
}

