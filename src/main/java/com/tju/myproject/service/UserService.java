package com.tju.myproject.service;

import com.tju.myproject.entity.Domain;
import com.tju.myproject.entity.User;

import java.util.ArrayList;

public interface UserService
{
    int addUser(User user);
    User getUserByUsername(String username);
}
