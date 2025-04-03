package bank;

public class CurrentAccount extends Account {
    public static double AIR = 0.005;  // Annual Interest Rate for all Current Accounts
    private double overdraftLimit;

    public CurrentAccount(int id, int custNo, double balance, double overdraftLimit) {
        super(id, custNo, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > getBalance() + overdraftLimit) {
            throw new IllegalArgumentException("Insufficient funds or overdraft limit exceeded.");
        }
        setBalance(getBalance() - amount);  // Directly set the balance after withdrawal
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}




