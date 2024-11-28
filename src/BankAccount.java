class BankAccount extends Abstract_Accounts{
    BankAccount(int accountNumber, String accountHolder, int balance){
        super(accountNumber, accountHolder, balance);
    }
    public boolean deposit(int amount){
        if(super.deposit(amount)){
            System.out.println("You have sucessfully deposited "+amount+" Balance: "+balance+"\n");
            String transaction = "You deposited "+amount+". Balance: "+ balance;
            super.addTransaction(transaction);
            return true;
        }
        return false;
    }
    public boolean withdraw(int amount){
        if (super.withdraw(amount)){
            System.out.println("You have sucessfully withdrawn "+amount+" Balance: "+balance+"\n");
            String transaction = "You withdrew "+amount+". Balance: "+ balance;
            super.addTransaction(transaction);
            return true;
        }
        return false;
    }
    public void viewTransactions(){
        super.viewTransactions();
    }
    @Override
    public int getBalance() {
        return balance;
    }
    
}
class SavingsAccount extends Abstract_Accounts{
    SavingsAccount(int accountNumber, String accountHolder, int balance){
        super(accountNumber, accountHolder, balance);
    }
    public boolean deposit(int amount){
        if(super.deposit(amount)){
            System.out.println("You have sucessfully deposited "+amount+" to your savings.\nBalance: "+balance+"\n");
            String transaction = "You deposited "+amount+" to your savings. Balance: "+ balance;
            super.addTransaction(transaction);
            return true;
        }
        return false;
    }
    public boolean withdraw(int amount){
        if (super.withdraw(amount)){
            System.out.println("You have sucessfully withdrawn "+amount+" from savings.\nBalance: "+balance+"\n");
            String transaction = "You withdrew "+amount+" from savings. Balance: "+ balance;
            super.addTransaction(transaction);
            return true;
        }
        return false;
    }
    public void viewTransactions(){
        super.viewTransactions();
    }
    @Override
    public int getBalance() {
        return balance;
    }
}
