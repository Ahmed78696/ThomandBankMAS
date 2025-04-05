package GUI;
import bank.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    public static ArrayList<Account> thomondAccounts = new ArrayList<>();
    public static ArrayList<BankStaff> thomondStaff = new ArrayList<>();

    public ATMGUI() {
        populateMyAccounts();

        // Ensure only one radio button is selected at a time
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(depositAccountRadioButton);
        accountTypeGroup.add(currentAccountRadioButton);

        // Deposit button logic
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeposit();
            }
        });

        // Withdraw button logic
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleWithdraw();
            }
        });

        // Check balance button logic
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckBalance();
            }
        });

        // Create new account button logic
        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateNewAccount();
            }
        });

        // Change AIR button logic
        changeAIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangeAIR();
            }
        });

        // Change overdraft limit button logic
        changeOverDraftLimitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangeOverdraftLimit();
            }
        });

        // Logout button logic
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPanel, "Logging out...");
            }
        });
    }

    // Deposit
    private void handleDeposit() {
        try {
            int accountId = Integer.parseInt(AtmtextField.getText());
            Account account = findAccount(accountId);

            if (account != null) {
                String amountStr = JOptionPane.showInputDialog("Enter deposit amount:");
                double amount = Double.parseDouble(amountStr);
                account.deposit(amount);

                JOptionPane.showMessageDialog(rootPanel, "Deposit successful! New balance: " + account.getBalance());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid amount entered.");
        }
    }

    // Withdraw
    private void handleWithdraw() {
        try {
            int accountId = Integer.parseInt(AtmtextField.getText());
            Account account = findAccount(accountId);

            if (account != null) {
                String amountStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
                double amount = Double.parseDouble(amountStr);
                account.withdraw(amount);

                JOptionPane.showMessageDialog(rootPanel, "Withdrawal successful! New balance: " + account.getBalance());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid amount entered.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(rootPanel, ex.getMessage());
        }
    }

    // Check balance
    private void handleCheckBalance() {
        int accountId = Integer.parseInt(AtmtextField.getText());
        Account account = findAccount(accountId);

        if (account != null) {
            JOptionPane.showMessageDialog(rootPanel, "Current balance: " + account.getBalance());
        }
    }

    // Create new account
    private void handleCreateNewAccount() {
        try {
            int custNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Customer Number:"));
            double initialBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance:"));

            if (depositAccountRadioButton.isSelected()) {
                DepositAccount newAccount = new DepositAccount(thomondAccounts.size() + 1, custNo, initialBalance);
                thomondAccounts.add(newAccount);
                JOptionPane.showMessageDialog(rootPanel, "Deposit Account created successfully!\nAccount ID: " + newAccount.getId());
            } else if (currentAccountRadioButton.isSelected()) {
                double overdraftLimit = Double.parseDouble(JOptionPane.showInputDialog("Enter overdraft limit:"));
                CurrentAccount newAccount = new CurrentAccount(thomondAccounts.size() + 1, custNo, initialBalance, overdraftLimit);
                thomondAccounts.add(newAccount);
                JOptionPane.showMessageDialog(rootPanel, "Current Account created successfully!\nAccount ID: " + newAccount.getId());
            } else {
                JOptionPane.showMessageDialog(rootPanel, "Please select an account type!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Invalid input. Please enter numbers only.");
        }
    }

    // Change AIR
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

    // Change Overdraft Limit
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

    // Preload sample data
    private void populateMyAccounts() {
        thomondAccounts.add(new DepositAccount(1, 1, 100));
        thomondAccounts.add(new DepositAccount(2, 2, 500));
        thomondAccounts.add(new DepositAccount(3, 3, 300));
        thomondAccounts.add(new DepositAccount(4, 4, 300));

        thomondAccounts.add(new CurrentAccount(5, 1, 100, 100));
        thomondAccounts.add(new CurrentAccount(6, 2, 1000, 500));
        thomondAccounts.add(new CurrentAccount(7, 4, 200, 200));
    }

    // Helper to find account
    private Account findAccount(int accountId) {
        for (Account account : thomondAccounts) {
            if (account.getId() == accountId) {
                return account;
            }
        }
        JOptionPane.showMessageDialog(rootPanel, "Account not found!");
        return null;
    }

    // Main
    public static void main(String[] args) {
        JFrame frame = new JFrame("Thomond Bank ATM");
        frame.setContentPane(new ATMGUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

