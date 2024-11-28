package database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    // String accNumber, getAccNum;
    // String username, getUsername;
    // String password, getPassword;
    // try{
    //     Statement st = con.createStatement();
    //     String selectUsers = "SELECT * FROM USERS";
    //     ResultSet rs = st.executeQuery(selectUsers);

    //     while(rs.next()){
    //         accNumber = rs.getString("AccountNum");
    //         username = rs.getString("Usernames");
    //         password = rs.getString("Passwords");

    //         if (accNumber.equals(getAccNum) && username.equals(getUsername) && password.equals(getPassword)){
    //             System.out.println("Login Sucessfull");
    //         }else{
    //             System.out.println("Invalid credetials");
    //         }
    //     }
    // }
    public static Connection createConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connection secured sucessfully");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/BankMgmt", "root", "password");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }catch(ClassNotFoundException e){
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean createUser(Connection con, String username, String email, String password){
        String InsertSQL = " INSERT INTO USERS (USERNAME, EMAIL, PASSWORD) VALUES (? , ?, ?)";

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
                return false;
    }
    public boolean validateUser(Connection con, String email, String password){
        String validateUser = " SELECT PASSWORD FROM USERS WHERE EMAIL = ? ";
        try (PreparedStatement statement = con.prepareStatement(validateUser)){
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){
                String storedPassword = rs.getString(password);
                if (storedPassword.equals(password)){
                    System.out.println("Login sucessful");
                    return true;
                }
                else{
                    System.out.println("Invalid password. Try again");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                return false;
    }
    public void createBankAccount (Connection con, int userId){
        String insertSQL = " INSERT INTO BANK_ACCOUNT USER_ID VALUES (?)";

        try (PreparedStatement statement = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setLong(1, userId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0){
                try(ResultSet rs = statement.getGeneratedKeys()){
                    if (rs.next()){
                        int generatedId = rs.getInt(1);
                        System.out.println("Account sucessfully created! Account Number "+generatedId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
