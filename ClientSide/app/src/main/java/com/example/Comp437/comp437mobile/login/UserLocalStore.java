package com.example.kwoksinman.comp437mobile.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kwoksinman.comp437mobile.SystemConstant;

/**
 * Created by KwokSinMan on 22/3/2016.
 */
public class UserLocalStore {
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SystemConstant.SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    //save user data to the local SharePreferences
    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("userId", user.getUserId());
        userLocalDatabaseEditor.putString("userName", user.getUserName());
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }
    public User getLoggedInUser() {
        String userId = userLocalDatabase.getString("userId", "");
        String userName = userLocalDatabase.getString("userName", "");
        User storedUser = new User(userId,userName,"");
        return storedUser;
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == true){
            return true;
        }
        else {
            return false;
        }
    }

}
