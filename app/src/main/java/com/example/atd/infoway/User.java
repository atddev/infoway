package com.example.atd.infoway;

/**
 * Created by atd on 9/19/2016.
 */

public class User {

    private static User instance;

    private User() {}

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    private  int userID;
    private String username;
    private String password;
    private String fname;
    private String lname;
    private String gender;
    private int age;

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userId) {
        this.userID = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return fname;
    }
    public void setFirstName(String fname) {
        this.fname = fname;
    }

    public String getLastName() {
        return lname;
    }
    public void setLastName(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

}
