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

    // Static lists to hold accounts and staff members
    public static ArrayList<Account> thomondAccounts = new ArrayList<>();
    public static ArrayList<BankStaff> thomondStaff = new ArrayList<>();

    public ATMGUI() {
        // Populate accounts for testing purposes
        populateMyAccounts();

        // Button event listeners
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deposit Money
                int accountId = Integer.parseInt(AtmtextField.getText());
                Account account = findAccount(accountId);

                if (account != null) {
                    String amountStr = JOptionPane.showInputDialog("Enter deposit amount:");
                    double amount = Double.parseDouble(amountStr);
                    account.deposit(amount);
                    JOptionPane.showMessageDialog(rootPanel, "Deposit successful! New balance: " + account.getBalance());
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Withdraw Money
                int accountId = Integer.parseInt(AtmtextField.getText());
                Account account = findAccount(accountId);

                if (account != null) {
                    String amountStr = JOptionPane.showInputDialog("Enter withdrawal amount:");
                    double amount = Double.parseDouble(amountStr);
                    account.withdraw(amount);
                    JOptionPane.showMessageDialog(rootPanel, "Withdrawal successful! New balance: " + account.getBalance());
                }
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check Account Balance
                int accountId = Integer.parseInt(AtmtextField.getText());
                Account account = findAccount(accountId);

                if (account != null) {
                    JOptionPane.showMessageDialog(rootPanel, "Current balance: " + account.getBalance());
                }
            }
        });

        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new Account based on radio button selection
                int custNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Customer Number:"));
                double initialBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance:"));

                if (depositAccountRadioButton.isSelected()) {
                    DepositAccount newAccount = new DepositAccount(thomondAccounts.size() + 1, custNo, initialBalance);
                    thomondAccounts.add(newAccount);
                    JOptionPane.showMessageDialog(rootPanel, "Deposit Account created successfully!");
                } else if (currentAccountRadioButton.isSelected()) {
                    double overdraftLimit = Double.parseDouble(JOptionPane.showInputDialog("Enter overdraft limit:"));
                    CurrentAccount newAccount = new CurrentAccount(thomondAccounts.size() + 1, custNo, initialBalance, overdraftLimit);
                    thomondAccounts.add(newAccount);
                    JOptionPane.showMessageDialog(rootPanel, "Current Account created successfully!");
                }
            }
        });

        changeAIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change AIR for selected account type
                if (depositAccountRadioButton.isSelected()) {
                    String newAIR = JOptionPane.showInputDialog("Enter new AIR for Deposit Account:");
                    DepositAccount.AIR = Double.parseDouble(newAIR);
                    JOptionPane.showMessageDialog(rootPanel, "AIR for Deposit Accounts updated!");
                } else if (currentAccountRadioButton.isSelected()) {
                    String newAIR = JOptionPane.showInputDialog("Enter new AIR for Current Account:");
                    CurrentAccount.AIR = Double.parseDouble(newAIR);
                    JOptionPane.showMessageDialog(rootPanel, "AIR for Current Accounts updated!");
                }
            }
        });

        changeOverDraftLimitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change overdraft limit for a current account
                int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account ID to modify overdraft:"));
                Account account = findAccount(accountId);

                if (account instanceof CurrentAccount) {
                    double newOverdraftLimit = Double.parseDouble(JOptionPane.showInputDialog("Enter new overdraft limit:"));
                    ((CurrentAccount) account).setOverdraftLimit(newOverdraftLimit);
                    JOptionPane.showMessageDialog(rootPanel, "Overdraft limit updated!");
                } else {
                    JOptionPane.showMessageDialog(rootPanel, "Account is not a Current Account!");
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Log out
                JOptionPane.showMessageDialog(rootPanel, "Logging out...");
                // Handle logout logic (e.g., closing or resetting the form)
            }
        });
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

    private void populateMyAccounts() {
        // Populate with some test data
        thomondAccounts.add(new DepositAccount(1, 1, 100));
        thomondAccounts.add(new DepositAccount(2, 2, 500));
        thomondAccounts.add(new DepositAccount(3, 3, 300));
        thomondAccounts.add(new DepositAccount(4, 4, 300));
        thomondAccounts.add(new CurrentAccount(4, 1, 100, 100));
        thomondAccounts.add(new CurrentAccount(5, 2, 1000, 500));
        thomondAccounts.add(new CurrentAccount(6, 3, 400, 200));
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
