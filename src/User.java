class User {
    String userName;
    String password;
    Integer id;
    

    User(String username, String password, Integer id){
        this.userName = username;
      
        this.password = password;
        this.id = id;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public Integer getId(){
        return id;
    }
}
