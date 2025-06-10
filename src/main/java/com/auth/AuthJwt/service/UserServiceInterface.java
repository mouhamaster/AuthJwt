package com.auth.AuthJwt.service;

import com.auth.AuthJwt.entities.User;

import java.util.List;

public interface UserServiceInterface {
     List<User> getAllUsers();
     User  Register(User user);
     User Login(User user);
}
