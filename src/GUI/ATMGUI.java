package GUI;

import bank.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ATMGUI {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane;
    private JTextField AtmtextField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton checkBalanceButton;
    private JButton createNewAccountButton;
    private JButton changeAIRButton;
    private JButton changeOverDraftLimitButton;
    private JButton logOutButton;
    private JRadioButton depositAccountRadioButton;
    private JRadioButton currentAccountRadioButton;
    private JButton transactionButton;

    public static ArrayList<Account> thomondAccounts = new ArrayList<>();
    private List<Transaction> transactionHistory = new ArrayList<>();

    public ATMGUI() {
        populateMyAccounts();

        // Ensure only one radio button is selected at a time
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(depositAccountRadioButton);
        accountTypeGroup.add(currentAccountRadioButton);

        // Deposit
        depositButton.addActionListener(e -> handleDeposit());

        // Withdraw
        withdrawButton.addActionListener(e -> handleWithdraw());

        // Check balance
        checkBalanceButton.addActionListener(e -> handleCheckBalance());

        // Create new account
        createNewAccountButton.addActionListener(e -> handleCreateNewAccount());

        // Change AIR
        changeAIRButton.addActionListener(e -> handleChangeAIR());

        // Change overdraft
        changeOverDraftLimitButton.addActionListener(e -> handleChangeOverdraftLimit());

        // Transaction history or new transaction
        transactionButton.addActionListener(e -> handleTransaction());

        // Logout
        logOutButton.addActionListener(e -> JOptionPane.showMessageDialog(rootPanel, "Logging out..."));
    }

    private void handleDeposit() {
        try {
            int accountId = Integer.parseInt(AtmtextField.getText());
            Account account = findAccount(accountId);

            if (account != null) {
                String amountStr = JOptionPane.showInputDialog("Enter deposit amount:");
                double amount = Double.parseDouble(amountStr);
                account.deposit(amount);

                Transaction transaction = new Transaction("Deposit", amount, "Deposited to account " + accountId);
                transactionHistory.add(transaction);

                JOptionPane.showMessageDialog(rootPanel, "Deposit successful! New balance: " + account.getBalance());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid amount entered.");
        }
    }

    private void handleWithdraw() {
        try {
            int accountId = Integer.parseInt(AtmtextField.getText());
            Account account = findAccount(accountId);

            if (account != null) {
                String amountStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
                double amount = Double.parseDouble(amountStr);
                account.withdraw(amount);

                Transaction transaction = new Transaction("Withdraw", amount, "Withdrawn from account " + accountId);
                transactionHistory.add(transaction);

                JOptionPane.showMessageDialog(rootPanel, "Withdrawal successful! New balance: " + account.getBalance());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid amount entered.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(rootPanel, ex.getMessage());
        }
    }

    private void handleCheckBalance() {
        int accountId = Integer.parseInt(AtmtextField.getText());
        Account account = findAccount(accountId);

        if (account != null) {
            JOptionPane.showMessageDialog(rootPanel, "Current balance: " + account.getBalance());
        }
    }

    private void handleCreateNewAccount() {
        String[] options = {"Deposit", "Current"};
        int choice = JOptionPane.showOptionDialog(
                rootPanel,
                "Select Account Type:",
                "New Account",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) return; // User cancelled

        try {
            String accountIdStr = JOptionPane.showInputDialog("Enter Account ID:");
            if (accountIdStr == null) return;
            int accountId = Integer.parseInt(accountIdStr);

            String custNoStr = JOptionPane.showInputDialog("Enter Customer Number:");
            if (custNoStr == null) return;
            int custNo = Integer.parseInt(custNoStr);

            double overdraftLimit = 0;
            if (choice == 1) { // Current Account
                String overdraftStr = JOptionPane.showInputDialog("Enter Overdraft Limit:");
                if (overdraftStr == null) return;
                overdraftLimit = Double.parseDouble(overdraftStr);
            }

            Account newAccount;
            if (choice == 0) {
                newAccount = new DepositAccount(accountId, custNo, 0);
            } else {
                newAccount = new CurrentAccount(accountId, custNo, 0, overdraftLimit);
            }

            thomondAccounts.add(newAccount);
            JOptionPane.showMessageDialog(rootPanel, "New Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid input. Please enter valid numbers only.");
        }
    }

    private void handleChangeAIR() {
        try {
            if (depositAccountRadioButton.isSelected()) {
                double newAIR = Double.parseDouble(JOptionPane.showInputDialog("Enter new AIR for Deposit Accounts:"));
                DepositAccount.AIR = newAIR;
                JOptionPane.showMessageDialog(rootPanel, "Deposit Account AIR updated to: " + newAIR);
            } else if (currentAccountRadioButton.isSelected()) {
                double newAIR = Double.parseDouble(JOptionPane.showInputDialog("Enter new AIR for Current Accounts:"));
                CurrentAccount.AIR = newAIR;
                JOptionPane.showMessageDialog(rootPanel, "Current Account AIR updated to: " + newAIR);
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Please select an account type to change AIR!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid input. Please enter a valid number.");
        }
    }

    private void handleChangeOverdraftLimit() {
        try {
            int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account ID to modify overdraft:"));
            Account account = findAccount(accountId);

            if (account instanceof CurrentAccount) {
                double newOverdraftLimit = Double.parseDouble(JOptionPane.showInputDialog("Enter new overdraft limit:"));
                ((CurrentAccount) account).setOverdraftLimit(newOverdraftLimit);
                JOptionPane.showMessageDialog(rootPanel, "Overdraft limit updated successfully!");
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Error: Account not found or not a Current Account.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid input. Please enter numbers only.");
        }
    }


    private void handleTransaction() {
        int accountId = Integer.parseInt(AtmtextField.getText());
        Account account = findAccount(accountId);

        if (account != null) {
            StringBuilder history = new StringBuilder("Transaction History for Account " + accountId + ":\n");

            for (Transaction transaction : transactionHistory) {
                history.append(transaction.toString()).append("\n");
            }

            JOptionPane.showMessageDialog(rootPanel, history.toString());
        } else {
            JOptionPane.showMessageDialog(rootPanel, "Account not found!");
        }
    }

    private void populateMyAccounts() {
        thomondAccounts.add(new DepositAccount(1, 1, 100));
        thomondAccounts.add(new DepositAccount(2, 2, 500));
        thomondAccounts.add(new DepositAccount(3, 3, 300));
        thomondAccounts.add(new DepositAccount(4, 4, 300));

        thomondAccounts.add(new CurrentAccount(5, 1, 100, 100));
        thomondAccounts.add(new CurrentAccount(6, 2, 1000, 500));
        thomondAccounts.add(new CurrentAccount(7, 4, 200, 200));
    }

    private Account findAccount(int accountId) {
        for (Account account : thomondAccounts) {
            if (account.getId() == accountId) {
                return account;
            }
        }
        JOptionPane.showMessageDialog(rootPanel, "Account not found!");
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Thomond Bank ATM");
        frame.setContentPane(new ATMGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
