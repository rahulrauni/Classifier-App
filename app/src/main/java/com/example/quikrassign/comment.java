package com.example.quikrassign;

public class comment {
    String Comment,advkey, userid;
    public comment(){
        
    }

    public comment(String comment, String advkey, String userid) {
        Comment = comment;
        this.advkey = advkey;
        this.userid = userid;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getAdvkey() {
        return advkey;
    }

    public void setAdvkey(String advkey) {
        this.advkey = advkey;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
