package com.leili.pojo;

import lombok.Data;

@Data
public class DatabaseConnectionInfo {
    private String url;
    private String username;
    private String password;
    private String table;
}
