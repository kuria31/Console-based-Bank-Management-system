import java.util.Scanner;

public class App {
    static Scanner scan = new Scanner(System.in);
    static UsersAndBankAcc usersAndBankAcc = new UsersAndBankAcc();
        public static void main(String[] args) throws Exception {
            createUser();
        
        }

        public static void createUser(){
            clearScreen();
            System.out.println("-----CREATE ACCOUNT----\n");
            int option;
            do {
                System.out.println("1. Create Account\n2. Login \n3. View all users \n4. Exit");
                System.out.print("Enter your choice: ");
                if (!scan.hasNextInt()){
                    scan.nextLine();
                    continue;
                }
                option = scan.nextInt();
                scan.nextLine(); //clear new Line character
                switch (option) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        loginAccount(null);
                        break;
                    case 3:
                        clearScreen();
                        usersAndBankAcc.viewAllUsers();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        break;
                    default:
                        break;
                }
            } while (true);
        }

        public static void createAccount(){
            clearScreen();
            System.out.println("\n-----CREATE ACCOUNT----\n");
            System.out.print("Enter username: ");
            String userName = scan.nextLine();

            System.out.print("Enter a unique Id: ");
            Integer id;

            while (true){
                try {
                    id = Integer.parseInt(scan.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a numeric value");
                }
            }


            System.out.print("Enter password: ");
            String password = scan.nextLine();

            User newUser = new User(userName, password, id);
            User exixstUser = usersAndBankAcc.addUser(id, newUser);
            if (exixstUser == null){
                System.out.println("Account created sucessfully");
                loginAccount(userName);
            }else{
                System.out.println("Accoount with this ID exists. Try again");
            }
        }
        public static void loginAccount(String username){
            System.out.println("\n-------LOGIN-------");
            System.out.print("Enter a unique Id: ");
            Integer id;
            try {
                id = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Returning to the main menu");
                return;
            }
            System.out.print("Enter password: ");
            String password = scan.nextLine();
            
            if (username == null){
                username = usersAndBankAcc.login(id);
            }
            if(usersAndBankAcc.isUserPresent(id, username, password)){
                System.out.println("logged in sucessfully!");
                if (usersAndBankAcc.usersBankMapping.containsKey(id)){
                    BankAccount bankAccount = usersAndBankAcc.getBankAccount(id);
                    Integer accNum = usersAndBankAcc.bankAccounts.get(id).accountNumber;
                    SavingsAccount savingsAccount = usersAndBankAcc.getSavingsAccount(accNum);
                    displayBank(bankAccount, savingsAccount);
                }else{
                    createBankAccount(id, username);
                }
            }else{
                System.out.println("Account doesn't exixt");
                return;
            }
        }

        public static void createBankAccount(Integer id, String username){
            clearScreen();
            System.out.println("\n--------CREATE BANK ACCOUNT------\n");
            System.out.print("Enter account Number: ");
            Integer accNum;
            try {
                accNum = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Returning to the main menu");
                return;
            }

            if (usersAndBankAcc.bankAccounts.containsKey(accNum)){
                System.out.println("Account number already in use, Please try again");
                return;
            }

            // creating bankk account and savings account
            BankAccount newBankAccount = new BankAccount(accNum, username, 0);
            SavingsAccount savingsAccount = new SavingsAccount(accNum, username, 0);

            if (usersAndBankAcc.addAccount(id, newBankAccount, savingsAccount)){
                System.out.println("Bank Account created Successsfully!");
                displayBank(newBankAccount, savingsAccount);
            }else{
                System.out.println("Error creating bank account. Try again");
            }

        }
        public static void displayBank(BankAccount bankAccount, SavingsAccount savingsAccount){
            clearScreen();
            System.out.println("\n--------BANK ACCOUNT MANAGEMENT--------\n");
            int option;

            do {
                String username = bankAccount.getAccountHolder().toUpperCase();
                int bankBalance = bankAccount.getBalance();
                int savingsBalance = savingsAccount.getBalance();
                System.out.println("\t\tWelcome!! "+username+"\n\nAccount Balance: Ksh "+bankBalance+"\tSavings Account Balance: Ksh "+savingsBalance+"\n");
                System.out.println("1. Deposit\n2. Widthdraw\n3. View Bank Balance\n4. Savings Account\n5. View all trasactions\n6. Exit");
                System.out.print("Enter your choice: ");
                if(!scan.hasNextInt()){
                    scan.nextLine();
                    continue;
                }
            option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1:
                    System.out.print("Enter the amount to deposit: ");
                    int deposit = scan.nextInt();
                    clearScreen();
                    bankAccount.deposit(deposit);
                    break;
                case 2:
                    System.out.print("Enter the amount to withdraw: ");
                    int withdraw = scan.nextInt();
                    clearScreen();
                    bankAccount.withdraw(withdraw);
                    break;
                case 3:
                    int bal = bankAccount.getBalance();
                    clearScreen();
                    System.out.println("Your account balance is:"+bal+"\n");
                    break;
                case 4:
                    SavingsAccountDisplay(bankAccount, savingsAccount);
                    return;
                case 5:
                    clearScreen();
                    System.out.println("\n--------TRANSACTIONS--------");
                    bankAccount.viewTransactions();
                    System.out.println();
                    break;
                case 6:
                    clearScreen();
                    System.out.println("Exiting bank account management");
                    createUser();
                    break;
                default:
                    System.out.print("Invalid option. Please choose a number between 1 and 6.");
            }
        } while (true);
    }
    public static void SavingsAccountDisplay(BankAccount bankAccount, SavingsAccount savingsAccount){
        clearScreen();
        System.out.println("\n--------SAVINGS ACCOUNT--------\n");
        int option;
        do {
            System.out.println("1. Add Savings\t2. Withdraw\t3. View Savings Balance\t4. View transactions\t5. Back");
            System.out.print("Enter your choice: ");
            if (!scan.hasNextInt()){
                scan.nextLine();
                continue;
            }
            option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1:
                    System.out.print("Enter the amount to add to savings: ");
                    int addSavings = scan.nextInt();
                    if(bankAccount.withdraw(addSavings)){
                        clearScreen();
                        savingsAccount.deposit(addSavings);
                    }else{
                        System.out.println("Transaction Failed. Insufficient amount in bank account");
                    }
                    break;
                case 2:
                    System.out.print("Enter the amount to withdraw from savings: ");
                    int withSavings = scan.nextInt();
                    clearScreen();
                    if(savingsAccount.withdraw(withSavings)){
                        bankAccount.deposit(withSavings);
                    }
                    break;
                case 3:
                    clearScreen();
                    int balance = savingsAccount.getBalance();
                    System.out.println("Your savings account balance is:"+balance+"\n");
                    break;
                case 4:
                    clearScreen();
                    System.out.println("\n--------TRANSACTIONS--------\n");
                    savingsAccount.viewTransactions();
                    System.out.println("\n");
                    break;
                case 5:
                    displayBank(bankAccount, savingsAccount);
                    break;
                default:
                    System.out.println("Enter valid options 1, 2 or 3: ");
                    // break;
            }
        } while (true);
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
}
