import java.util.HashMap;
import java.util.Map;

public class UsersAndBankAcc {
    // Store all users
    Map<Integer, User> users = new HashMap<>();
    // Integer userId = user.getId();

    // Store all bank account information
    Map<Integer, BankAccount> bankAccounts = new HashMap<>();

    // Store bankAccount details
    Map<Integer, Integer> usersBankMapping = new HashMap<>();

    // Link Bank account to Savings account
    Map<Integer, SavingsAccount> bankSavingMapping = new HashMap<>();

    // Add new User
    public User addUser(Integer id, User newUser){
        // Integer id = newUser.getId();
        return users.put(id, newUser);
    }

    // Remove user by their object;
    public boolean removeUser(User user){
        if (user == null || user.getId() == null){
            return false;
        }
        return users.remove(user.getId()) != null;
    }

    // Add a bank account
    public boolean addAccount(Integer userId, BankAccount bankAccount, SavingsAccount savingsAccount){
        Integer accNum = bankAccount.getAccountNumber();
        if (userId == null || bankAccount == null){
            return false;
        }
        bankAccounts.put(accNum, bankAccount);
        usersBankMapping.put(userId, accNum);
        bankSavingMapping.put(accNum, savingsAccount);
        return true;
    }

    // Remove bank account by userId
    public void removeAccount(Integer userId){
        if (userId != null){
            Integer accNum = usersBankMapping.get(userId);
            if (accNum != null){
                bankAccounts.remove(accNum); //remove bank account
                usersBankMapping.remove(userId); //remove user-bank link
                bankSavingMapping.remove(accNum);
            } 
        }
    }

    // Check if user is present
    public boolean isUserPresent(Integer id, String username, String password){
        if (id == null || username == null || password == null){
            System.out.println("Invalid login credentials");
            return false;
        }
        User user = users.get(id);
        if (user != null && user.getUserName().equals(username) && user.getPassword().equals(password)){
            return true;
        }
        else{
            System.out.println("Invalid credentials");
            return false;
        }
    }

    // View all users
    public void viewAllUsers(){
        if (users.isEmpty()){
            System.out.println("No users present");
        }else{
            for (Map.Entry<Integer, User> entry : users.entrySet()) {
                System.out.println("ID: " +entry.getKey()+", Username: "+ entry.getValue().getUserName());
            }
        }
    }
    public BankAccount getBankAccount(Integer userId){
        Integer accNum = usersBankMapping.get(userId);
        if (accNum != null ){
            return bankAccounts.get(accNum);
        }
        return null;
    }
    public SavingsAccount getSavingsAccount(Integer accNum){
        if (accNum != null){
            return bankSavingMapping.get(accNum);
        }
        return null;
    }
    public String login(Integer id){
        User user = users.get(id);
        if (user != null){
            System.out.println(user.getUserName());
            return user.getUserName();
        }
        else{
            return null;
        }
    }
    // public String getBankDetails(){}
}
