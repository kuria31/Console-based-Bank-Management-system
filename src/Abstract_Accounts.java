import java.util.ArrayList;
import java.util.List;

public abstract class Abstract_Accounts implements Interface_Accounts{
    int accountNumber;
    String accountHolder;
    int balance;
    List<String> transactionHistory = new ArrayList<>();

    Abstract_Accounts(int accountNumber, String accountHolder, int balance){
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public int getAccountNumber(){
        return accountNumber;
    }
    public String getAccountHolder(){
        return accountHolder;
    }
    public boolean deposit(int amount){
        if (amount <= 0){
            System.out.println("Please enter a valid amount\n");
            return false;
        }else{
            balance+=amount;
            return true;
        }
    }
    public boolean withdraw(int amount){
        if (balance < amount){
            System.out.println("Insufficient amount in your bank account. Balance: " +balance+"\n");
            return false;
        }else if (amount<= 0){
            System.out.println("Please enter a valid amount\n");
            return false;
        }
        else{
            balance-=amount;
            return true;
        }
    }
    public abstract int getBalance();
    public void addTransaction(String transaction){
        transactionHistory.add(transaction);
    }
    public void viewTransactions(){
        if (transactionHistory.isEmpty()){
            System.out.println("No traansactions found");
        }else{
            transactionHistory.forEach(System.out::println);
        }
    }
}
