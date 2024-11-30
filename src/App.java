import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import database.DatabaseConnection;

public class App {
    static Scanner scan = new Scanner(System.in);
    static DatabaseConnection database = new DatabaseConnection();
    static Connection con = DatabaseConnection.createConnection();
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
                    return;
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
                        System.out.println("ALL USERS");
                        List<String> users = database.getAllUsers(con);
                        int i = 0;
                        do {
                            System.out.println(users.get(i));
                            i+=1;
                        } while (i < users.size());
                        System.out.println();
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
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

                System.out.print("Enter email: ");
                String email = scan.nextLine();

                System.out.print("Enter password: ");
                String password = scan.nextLine();

                if (database.createUser(con, userName, email, password)){
                    System.out.println("Account created sucessfully");
                    loginAccount(userName);
                }else{
                    System.out.println("Account already exists\n");
                    createUser();
                }
        }
        public static void loginAccount(String username){
            System.out.println("\n-------LOGIN-------");

            System.out.print("Enter email: ");
            String email = scan.nextLine();
        
            System.out.print("Enter password: ");
            String password = scan.nextLine();
            
            if (database.validateUser(con, email, password)){
                System.out.println("Login sucessfull");
                createBankAccount(email, username);
            }
            else{
                clearScreen();
                System.out.println("Incorrect credentials");
                loginAccount(username);
            }
        }

        public static void createBankAccount(String email, String username){
            clearScreen();
            System.out.println("\n--------CREATE BANK ACCOUNT------\n");

            // Gettting userID
            int userId = database.getUserId(con, email);

            if (database.isUserPresentInBank(con, userId) != 0){
                clearScreen();
                displayBank(email, username, userId);
            }else{
                String option;
                do {
                    System.out.print("You do not have bank account. Would you like to create one? \nEnter option (yes or 1 )if you agree or (no or 0) if you disagree (press 5 to exit): ");
                    option = scan.nextLine().toLowerCase();
                    if (option.equals("yes") || option.equals("1")){

                        try {
                            System.out.println("Creating account...");
                            Thread.sleep(3000);
    
                        } catch (InterruptedException e) {
                            System.out.println("Sleep Error: "+e.getMessage());
                        }
                        // Creating bank account
                        if(database.createBankAccount(con, userId)){
                            displayBank(email, username, userId);
                            System.out.println("Account sucesssfully created");
                        }else{
                            System.out.println("Error creating bank account, try again");
                        }
                    }else if (option.equals("no") || option.equals("0")){
                        System.out.println("Returnig to login page");
                        loginAccount(username);
                    }else if (option.equals("5")){
                        return;
                    }else{
                        System.out.println("Invalid option");
                    }

                } while (true);
            }

        }
        public static void displayBank(String email, String username, int userId){
            clearScreen();

            System.out.println("\n--------BANK ACCOUNT MANAGEMENT--------\n");
            int option;

            do {
                int bankBalance = database.getBankAccountbalance(con, userId);
                int savingsBalance = database.getSavingsAccountbalance(con, userId);
                System.out.println("\t\tWelcome!! "+username+"\n\nAccount Balance: Ksh "+bankBalance+"\tSavings Account Balance: Ksh" +savingsBalance);
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
                    database.depositBank(con, deposit, userId);
                    break;
                case 2:
                    System.out.print("Enter the amount to withdraw: ");
                    int withdraw = scan.nextInt();
                    clearScreen();
                    database.withdrawBank(con, withdraw, userId);
                    break;
                case 3:
                    clearScreen();
                    System.out.println("Your account balance is:"+bankBalance+"\n");
                    break;
                case 4:
                    creatingSavingsAccountPrompt(email, username, userId);
                    break;
                case 5:
                    clearScreen();
                    System.out.println("\n--------TRANSACTIONS--------");
                    int bank_acc = database.getBankAccNumber(con, userId);
                    List<String> statemet = database.getBankAccTransactions(con, userId, bank_acc);
                    int i = 0;
                    // System.out.println(statemet.get(i));
                    do {System.out.println(statemet.get(i)); i+=1;} while(i < statemet.size());
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

    public static void creatingSavingsAccountPrompt(String email, String username, int userId){
        if (database.isUserPresentInSavings(con, userId) != 0){
            SavingsAccountDisplay(email, username, userId);
        }
        String option;
            do {
                System.out.print("You do not have bank account. Would you like to create one? \nEnter option (yes or 1 )if you agree or (no or 0) if you disagree (press 5 to exit): ");
                option = scan.nextLine().toLowerCase();
                if (option.equals("yes") || option.equals("1")){

                    try {
                        System.out.println("Creating account...");
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        System.out.println("Sleep Error: "+e.getMessage());
                    }

                    // Creating bank account
                    if(database.creatingSavingsAcc(con, userId)){
                        SavingsAccountDisplay(email, username, userId);
                        System.out.println("Account sucesssfully created");
                    }else{
                        System.out.println("Error creating bank account, try again");
                    }
                }else if (option.equals("no") || option.equals("0")){
                    System.out.println("Returnig to login page");
                    loginAccount(username);
                }else if (option.equals("5")){
                    return;
                }else{
                    System.out.println("Invalid option");
                }

            } while (true);
    }
    public static void SavingsAccountDisplay(String email, String username, int userId){
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
                    clearScreen();
                    database.depositSavings(con, addSavings, userId);
                    break;
                case 2:
                    System.out.print("Enter the amount to withdraw from savings: ");
                    int withSavings = scan.nextInt();
                    clearScreen();
                    database.withdrawSavings(con, withSavings, userId);
                    break;
                case 3:
                    clearScreen();
                    int balance = database.getSavingsAccountbalance(con, userId);
                    System.out.println("Your savings account balance is:"+balance+"\n");
                    break;
                case 4:
                    clearScreen();
                    System.out.println("\n--------TRANSACTIONS--------\n");
                    int savingsId = database.getSavingsAccID(con, userId);
                    List<String> statements = database.getSavingsTransactions(con, userId, savingsId);
                    int i = 0;
                    do {
                        System.out.println(statements.get(i));
                        i+=1;
                    } while (i < statements.size());
                    System.out.println("\n");
                    break;
                case 5:
                    displayBank(email, username, userId);
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
