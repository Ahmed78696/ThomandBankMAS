package bank;

public class DepositAccount extends Account {
    public static double AIR = 0.02;  // Annual Interest Rate for all Deposit Accounts

    public DepositAccount(int id, int custNo, double balance) {
        super(id, custNo, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal.");
        }
        setBalance(getBalance() - amount);
    }
}



