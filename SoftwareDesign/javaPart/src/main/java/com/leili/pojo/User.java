package com.leili.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String usr;
    private String pwd;
    private Integer isAdmin;

    public User(@JsonProperty("usr") String usr,@JsonProperty("pwd") String pwd,@JsonProperty("isAdmin") Integer isAdmin) {
        this.usr = usr;
        this.pwd = pwd;
        this.isAdmin = isAdmin;
    }

    public User(String usr,String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public User(String usr,Integer isAdmin) {
        this.usr = usr;
        this.isAdmin = isAdmin;
    }

    public User(Integer isAdmin){
        this.isAdmin = isAdmin;
    }
}
