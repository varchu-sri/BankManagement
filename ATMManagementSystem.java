import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import com.toedter.calendar.JDateChooser; 
class BankDetails {
    private String accno;
    private String name;
    private String acc_type;
    private long balance;
    private String dateOfBirth;
    private Stack<String> transactionHistory;
    public BankDetails() {
        transactionHistory = new Stack<>();
    }
    public void openAccount(String accno, String acc_type, String name, long balance, String dateOfBirth) {
        this.accno = accno;
        this.acc_type = acc_type;
        this.name = name;
        this.balance = balance;
        this.dateOfBirth = dateOfBirth;
        transactionHistory.push("Account opened with balance: " + balance);
    }
    public boolean search(String ac_no) {
        return accno.equals(ac_no);
    }
    public String getDetails() {
        return "Account No: " + accno + "\nName: " + name + "\nAccount Type: " + acc_type + "\nBalance: " + balance + "\nDate of Birth: " + dateOfBirth;
    }
    public void deposit(long amt) {
        balance += amt;
        transactionHistory.push("Deposited: " + amt);
    }
    public boolean withdrawal(long amt) {
        if (balance >= amt) {
            balance -= amt;
            transactionHistory.push("Withdrew: " + amt);
            return true;
        }
        return false;
    }
    public boolean transfer(BankDetails targetAccount, long amount) {
        if (balance >= amount) {
            balance -= amount;
            targetAccount.balance += amount;
            transactionHistory.push("Transferred: " + amount + " to " + targetAccount.accno);
            return true;
        }
        return false;
    }
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    public String getAccno() {
        return accno;
    }
}
public class ATMManagementSystem extends JFrame implements ActionListener {
    private static List<BankDetails> customers = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField accNoField, nameField, balanceField, amountField, targetAccNoField;
    private JPanel mainPanel, currentPanel;
    private JRadioButton savingsButton, currentButton;
    private JDateChooser dateOfBirthChooser; 
    public ATMManagementSystem() {
        setTitle("ATM Management System");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel, BorderLayout.WEST);
        createMainButtons();
        currentPanel = new JPanel();
        add(currentPanel, BorderLayout.NORTH);
        setVisible(true);
    }
    private void createMainButtons() {
        String[] buttonLabels = {"Open Account", "Deposit", "Withdraw", "Transfer", "Show History", "View Account", "Delete Account", "Exit"};
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = i;
            mainPanel.add(button, gbc);
        }
    }
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        clearCurrentPanel();

        switch (command) {
            case "Open Account":
                showOpenAccountPanel();
                break;
            case "Deposit":
                showDepositPanel();
                break;
            case "Withdraw":
                showWithdrawPanel();
                break;
            case "Transfer":
                showTransferPanel();
                break;
            case "Show History":
                showHistoryPanel();
                break;
            case "View Account":
                showViewAccountPanel();
                break;
            case "Delete Account":
                showDeleteAccountPanel();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }
    private void clearCurrentPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new JPanel(); 
    }
    private void showOpenAccountPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        nameField = createTextField(currentPanel, gbc, "Name:");
        balanceField = createTextField(currentPanel, gbc, "Initial Balance:");
        JLabel accountTypeLabel = new JLabel("Account Type:");
        gbc.gridx = 0;
        gbc.gridy++;
        currentPanel.add(accountTypeLabel, gbc);
        savingsButton = new JRadioButton("Savings");
        currentButton = new JRadioButton("Current");
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(savingsButton);
        accountTypeGroup.add(currentButton);
        gbc.gridx = 0;
        gbc.gridy++;
        currentPanel.add(savingsButton, gbc);
        gbc.gridx = 1;
        currentPanel.add(currentButton, gbc);
        JLabel dobLabel = new JLabel("Date of Birth:");
        gbc.gridx = 0;
        gbc.gridy++;
        currentPanel.add(dobLabel, gbc);
        dateOfBirthChooser = new JDateChooser(); 
        dateOfBirthChooser.setDate(new Date()); 
        dateOfBirthChooser.setDateFormatString("yyyy-MM-dd"); 
        gbc.gridx = 1;
        currentPanel.add(dateOfBirthChooser, gbc);
        JButton createButton = new JButton("Create Account");
        createButton.addActionListener(e -> createAccount());
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(createButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showDepositPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        amountField = createTextField(currentPanel, gbc, "Amount:");
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> deposit());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(depositButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showWithdrawPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        amountField = createTextField(currentPanel, gbc, "Amount:");
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> withdraw());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(withdrawButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showTransferPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "From Account No:");
        targetAccNoField = createTextField(currentPanel, gbc, "To Account No:");
        amountField = createTextField(currentPanel, gbc, "Amount:");
        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(e -> transfer());
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(transferButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showHistoryPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        JButton showHistoryButton = new JButton("Show History");
        showHistoryButton.addActionListener(e -> showHistory());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(showHistoryButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showViewAccountPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewAccountDetails());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(viewButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private void showDeleteAccountPanel() {
        currentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        accNoField = createTextField(currentPanel, gbc, "Account No:");
        JButton deleteButton = new JButton("Delete Account");
        deleteButton.addActionListener(e -> deleteAccount());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        currentPanel.add(deleteButton, gbc);
        add(currentPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }
    private JTextField createTextField(JPanel panel, GridBagConstraints gbc, String labelText) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);
        JTextField textField = new JTextField(15);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField, gbc);
        return textField;
    }
    private void createAccount() {
        String accno = accNoField.getText();
        String name = nameField.getText();
        long balance;
        try {
            balance = Long.parseLong(balanceField.getText());
        } catch (NumberFormatException e) {
            displayArea.append("Please enter a valid balance.\n");
            return;
        }
        String accType = savingsButton.isSelected() ? "Savings" : "Current";
        String dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirthChooser.getDate());
        BankDetails newAccount = new BankDetails();
        newAccount.openAccount(accno, accType, name, balance, dateOfBirth);
        customers.add(newAccount);
        displayArea.append("Account created successfully: " + accno + "\n");
    }
    private void deposit() {
        String accno = accNoField.getText();
        long amount;
        try {
            amount = Long.parseLong(amountField.getText());
        } catch (NumberFormatException e) {
            displayArea.append("Please enter a valid amount.\n");
            return;
        }
        for (BankDetails customer : customers) {
            if (customer.search(accno)) {
                customer.deposit(amount);
                displayArea.append("Deposited " + amount + " to account " + accno + "\n");
                return;
            }
        }
        displayArea.append("Account not found: " + accno + "\n");
    }
    private void withdraw() {
        String accno = accNoField.getText();
        long amount;
        try {
            amount = Long.parseLong(amountField.getText());
        } catch (NumberFormatException e) {
            displayArea.append("Please enter a valid amount.\n");
            return;
        }
        for (BankDetails customer : customers) {
            if (customer.search(accno)) {
                if (customer.withdrawal(amount)) {
                    displayArea.append("Withdrew successfully: " + amount + " from account " + accno + "\n");
                } else {
                    displayArea.append("Insufficient balance in account: " + accno + "\n");
                }
                return;
            }
        }
        displayArea.append("Account not found: " + accno + "\n");
    }
    private void transfer() {
        String fromAccno = accNoField.getText();
        String toAccno = targetAccNoField.getText();
        long amount;
        try {
            amount = Long.parseLong(amountField.getText());
        } catch (NumberFormatException e) {
            displayArea.append("Please enter a valid amount.\n");
            return;
        }
        BankDetails fromAccount = null;
        BankDetails toAccount = null;
        for (BankDetails customer : customers) {
            if (customer.search(fromAccno)) {
                fromAccount = customer;
            }
            if (customer.search(toAccno)) {
                toAccount = customer;
            }
        }
        if (fromAccount != null && toAccount != null) {
            if (fromAccount.transfer(toAccount, amount)) {
                displayArea.append("Transferred " + amount + " from account " + fromAccno + " to " + toAccno + "\n");
            } else {
                displayArea.append("Insufficient balance in account: " + fromAccno + "\n");
            }
        } else {
            displayArea.append("One or both accounts not found.\n");
        }
    }
    private void showHistory() {
        String accno = accNoField.getText();
        for (BankDetails customer : customers) {
            if (customer.search(accno)) {
                displayArea.append("Transaction History for " + accno + ":\n");

                // Get the history and display in reverse order
                List<String> history = customer.getTransactionHistory();
                for (int i = history.size() - 1; i >= 0; i--) {
                    displayArea.append(history.get(i) + "\n");
                }
                return;
            }
        }
        displayArea.append("Account not found: " + accno + "\n");
    }
    private void viewAccountDetails() {
        String accno = accNoField.getText();
        for (BankDetails customer : customers) {
            if (customer.search(accno)) {
                displayArea.append("Account Details:\n");
                displayArea.append(customer.getDetails() + "\n");
                return;
            }
        }
        displayArea.append("Account not found: " + accno + "\n");
    }
    private void deleteAccount() {
        String accno = accNoField.getText();
        BankDetails accountToRemove = null;
        for (BankDetails customer : customers) {
            if (customer.search(accno)) {
                accountToRemove = customer;
                break;
            }
        }
        if (accountToRemove != null) {
            customers.remove(accountToRemove);
            displayArea.append("Account deleted successfully: " + accno + "\n");
        } else {
            displayArea.append("Account not found: " + accno + "\n");
        }
    }
    public static void main(String[] args) {
        new ATMManagementSystem();
    }
}