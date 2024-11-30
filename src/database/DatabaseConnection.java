package database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    public static Connection createConnection(){
        // Load properties from .env file
        Properties properties = new Properties();
        try{
            properties.load(new FileReader(".env"));
        }catch (IOException e){
            System.out.println("Error reading .env file"+e.getMessage());
        }

        // Retrieve database redetials form properties
        String dbHost = properties.getProperty("DB_HOST");
        String dbPort = properties.getProperty("DB_PORT");
        String dbUser = properties.getProperty("DB_USER");
        String dbPassword = properties.getProperty("DB_PASSWORD");
        String dbName = properties.getProperty("DB_NAME");

        // database URL
        String dbUrl = "jdbc:mysql://"+dbHost+": " +dbPort+"/" +dbName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connection secured sucessfully");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }catch(ClassNotFoundException e){
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    // CREATING USER
    public boolean createUser(Connection con, String username, String email, String password){
        String InsertSQL = " INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (? , ?, ?)";

        // checking if user aleady exist
        if (getUserId(con, email) != 0){
            return false;
        }else{
            try (PreparedStatement statement = con.prepareStatement(InsertSQL)){
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password);
    
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted >0){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
         return false;
    }

    // Validating user
    public boolean validateUser(Connection con, String email, String password){
        String validateUser = " SELECT PASSWORD FROM USERS WHERE EMAIL = ? ";
        try (PreparedStatement statement = con.prepareStatement(validateUser)){
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){
                String storedPassword = rs.getString(password);
                if (storedPassword.equals(password)){
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                return false;
    }

    // CREATING BANK ACCOUNT
    public boolean createBankAccount (Connection con, int userId){
        String insertSQL = " INSERT INTO BANK_ACCOUNT (USER_FK) VALUES (?)";

        try (PreparedStatement statement = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setLong(1, userId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0){
                try(ResultSet rs = statement.getGeneratedKeys()){
                    if (rs.next()){
                        int generatedId = rs.getInt(1);
                        System.out.println("Account sucessfully created! Account Number "+generatedId);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }return false;
    }

    // CREATING SAVINGS ACCOUNT
    public boolean creatingSavingsAcc(Connection con, int userId){
        String creatAccQuery = " INSERT INTO SAVINGS_ACCOUNT (USER_ID_FK) VALUE (?)";
        try (PreparedStatement statement = con.prepareStatement(creatAccQuery)){
            statement.setInt(1, userId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted >0 ){
                return true;
            }
            return false;
        }catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    // GETTING USER ID
    public int getUserId(Connection con, String email){
        String selectId = " SELECT ID FROM USERS WHERE EMAIL = ?";

        try (PreparedStatement statement = con.prepareStatement(selectId)){
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // CHECKING IF USER IS PRESENT IN BANK_ACCOUNT
    public int isUserPresentInBank(Connection con, int userId){
        String selectId = " SELECT USER_FK FROM BANK_ACCOUNT WHERE USER_FK = ?";

        try (PreparedStatement statement = con.prepareStatement(selectId)){
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // CHECKING IF USER IS PRESENT IN SAVINGS ACCOUNT
    public int isUserPresentInSavings(Connection con, int userId){
        String selectId = " SELECT USER_ID_FK FROM savings_Account WHERE USER_ID_FK = ?";

        try (PreparedStatement statement = con.prepareStatement(selectId)){
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // GETTING SAVINGS ACCOUNT ID
    public int getSavingsAccID(Connection con, int userID){
        String selectId = " SELECT ACCOUNT_ID FROM SAVINGS_ACCOUNT WHERE USER_ID_FK = ?";

        try (PreparedStatement statement = con.prepareStatement(selectId)){
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    // GETTING Bank Account account Number
    public int getBankAccNumber(Connection con, int userID){
        String selectId = " SELECT ACCOUNT_NUMBER FROM BANK_ACCOUNT WHERE USER_FK = ?";

        try (PreparedStatement statement = con.prepareStatement(selectId)){
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    // GETTING BANK ACCOUNT BALANCE
    public int getBankAccountbalance(Connection con, int userID){
        String accBalQuery = " SELECT ACC_BALANCE FROM BANK_ACCOUNT WHERE USER_FK = ? ";

        try (PreparedStatement statement = con.prepareStatement(accBalQuery)) {
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    // GETTING SAVINGS ACCOUNT BALANCE
    public int getSavingsAccountbalance(Connection con, int userID){
        String accBalQuery = " SELECT SAVINGS_ACC_BAL FROM SAVINGS_ACCOUNT WHERE USER_ID_FK = ? ";

        try (PreparedStatement statement = con.prepareStatement(accBalQuery)) {
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }

    // DEPOSITING MONEY TO BANK ACCOUNT
    public void depositBank(Connection con, int amount, int userID){
        String updateQuery = " UPDATE BANK_ACCOUNT SET ACC_BALANCE = ? WHERE USER_FK = ?";

        int accountBal = getBankAccountbalance(con, userID);
        accountBal += amount;
        try (PreparedStatement statement = con.prepareStatement(updateQuery)){
            statement.setInt(1, accountBal);
            statement.setInt(2, userID);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted >0){
                int accNumber = getBankAccNumber(con, userID);
                String trans = "You have successfully deposited "+ amount+ " to bank account. Bank account balance: "+accountBal;
                transactions(con, trans, userID, accNumber, 0);
                System.out.println(trans);
            }
            else{
                System.out.println("Transaction unsucessful. Try again");
            }
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // WITHDRAWING MONEY FROM BANK ACCOUNT
    public void withdrawBank(Connection con, int amount, int userID){
        String updateQuery = " UPDATE BANK_ACCOUNT SET ACC_BALANCE = ? WHERE USER_FK = ?";

        int accountBal = getBankAccountbalance(con, userID);

        if (accountBal > amount){
            accountBal -= amount;
        }else{
            System.out.println("Insufficient amount in your account");
        }
        try (PreparedStatement statement = con.prepareStatement(updateQuery)){
            statement.setInt(1, accountBal);
            statement.setInt(2, userID);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0){
                int accNumber = getBankAccNumber(con, userID);
                String trans = "You have successfully withdrawn "+ amount+ " from bank account. Bank account balance: "+accountBal;
                transactions(con, trans, userID, accNumber, 0);
                System.out.println(trans);
            }
            else{
                System.out.println("Transaction unsucessful. Try again");
            }
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // DEPOSTIT TO SAVINGS ACCOUNT
    public void depositSavings(Connection con, int amount, int userID){
        String updateQuery = " UPDATE SAVINGS_ACCOUNT SET SAVINGS_ACC_BAL = ? WHERE USER_ID_FK = ?";

        int bankAccountBal = getBankAccountbalance(con, userID);
        int savingsAccBal = getSavingsAccountbalance(con, userID);
        if (bankAccountBal > amount){
            withdrawBank(con, amount, userID);
            savingsAccBal +=amount;
        }else{
            System.out.println("Insuffiient amount in bank account");
            return;
        }
        // accountBal += amount;
        try (PreparedStatement statement = con.prepareStatement(updateQuery)){
            statement.setInt(1, savingsAccBal);
            statement.setInt(2, userID);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted >0){
                String trans = "You have successfully deposited "+ amount+ " to savings account. Savings account balance: "+savingsAccBal;
                int accId = getSavingsAccID(con, userID);
                transactions(con, trans, userID, 0, accId);
                System.out.println(trans);
            }
            else{
                System.out.println("Transaction unsucessful. Try again");
            }
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void withdrawSavings(Connection con, int amount, int userId){
        String updateQuery = " UPDATE SAVINGS_ACCOUNT SET SAVINGS_ACC_BAL = ? WHERE USER_ID_FK = ?";

        int savingsAccBal = getSavingsAccountbalance(con, userId);
        if (savingsAccBal > amount){
            depositBank(con, amount, userId);
            savingsAccBal -=amount;
        }else{
            System.out.println("Insuffiient amount in Savings account");
            return;
        }

        try (PreparedStatement statement = con.prepareStatement(updateQuery)) {
            statement.setInt(1, savingsAccBal);
            statement.setInt(2, userId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0){
                String trans = "You have successfully withdrawn "+ amount+ " from savings account. Savings account balance: "+savingsAccBal;
                int accId = getSavingsAccID(con, userId);
                transactions(con, trans, userId, 0, accId);
                System.out.println(trans);
            }
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // INSERTING TRANSACTION INFROMATION TO DATABASE
    public boolean transactions(Connection con, String transaction, int userId, int bankAcc, int savingsId){
        String insertTransQuery = " INSERT INTO TRANSACTIONS (TRANS_DESCRIPTION, USER_ID_FK, BANK_ACC_ID, SAVINGS_ACC_ID) VALUES (? , ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(insertTransQuery)){
            statement.setString(1, transaction);
            statement.setInt(2, userId);
            statement.setInt(3, bankAcc);
            statement.setInt(4, savingsId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0){
                System.out.println("Transaction saved");
                return true;
            }
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return false;
    }
    public List<String> getBankAccTransactions(Connection con, int userId, int bank_acc){
        String bankTrasanctions = " SELECT TRANS_DESCRIPTION FROM TRANSACTIONS WHERE USER_ID_FK = ? AND BANK_ACC_ID = ?";

        try (PreparedStatement statement = con.prepareStatement(bankTrasanctions)){
            statement.setInt(1, userId);
            statement.setInt(2, bank_acc);
            ResultSet rs = statement.executeQuery();

            List<String> allTrans = new ArrayList<>();
            while (rs.next()){
                allTrans.add(rs.getString(1));
            }
            return allTrans;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    // get saving bank account transactions
    public List<String> getSavingsTransactions (Connection con, int userId, int savingsId){
        String savingTrasQuery = " SELECT TRANS_DESCRIPTION FROM TRANSACTIONS WHERE USER_ID_FK = ? AND SAVINGS_ACC_ID = ?";

        try (PreparedStatement statement = con.prepareStatement(savingTrasQuery)){
            statement.setInt(1, userId);
            statement.setInt(2, savingsId);
            ResultSet rs = statement.executeQuery();

            List<String> allTransactions = new ArrayList<>();
            while (rs.next()){
                allTransactions.add(rs.getString(1));
            }
            return allTransactions;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    // get all users
    public List<String> getAllUsers(Connection con){
        String getQuery = " SELECT EMAIL, USERNAME FROM USERS";

        try (PreparedStatement statement = con.prepareStatement(getQuery)) {
            ResultSet rs = statement.executeQuery();
            
            List<String> getUsers = new ArrayList<>();
            System.out.println("EMAIL\t\t\t\tUSERNAME");
            while(rs.next()){
                getUsers.add(rs.getString(1).concat("\t\t"+rs.getString(2)));
            }
            return getUsers;
        } catch (Exception e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
}
