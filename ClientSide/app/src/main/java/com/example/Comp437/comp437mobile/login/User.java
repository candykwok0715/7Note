package com.example.kwoksinman.comp437mobile.login;

/**
 * Created by KwokSinMan on 23/3/2016.
 */
public class User {

    private String userId;
    private String userName;
    private String password;

    public User(){

    }

    public User(String userId,String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return "user id="+userId+" userName="+userName +" password="+password;
    }
}
