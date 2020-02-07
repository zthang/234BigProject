package com.tju.myproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User
{
    @JsonProperty(value = "userID")
    private Integer userID;
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "password")
    private String password;
}
