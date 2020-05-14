package com.example.quikrassign;

public class productlist {
    private String Advkey,Category, State, City, Mobile, Userid, Title, Description, Value;
    private String link1, username;
    public productlist(){

    }

    public productlist(String advKey, String category, String state, String city, String mobile, String userid, String title, String description, String value, String link1, String username) {
        Advkey = advKey;
        Category = category;
        State = state;
        City = city;
        Mobile = mobile;
        Userid = userid;
        Title = title;
        Description = description;
        Value = value;
        username = username;
        this.link1 = link1;
    }

    public String getAdvKey() {
        return Advkey;
    }

    public void setAdvKey(String advKey) {
        Advkey = advKey;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
