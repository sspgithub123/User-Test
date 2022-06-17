package com.bridgelabz.usertest.service;



import com.bridgelabz.usertest.dto.LoginDTO;
import com.bridgelabz.usertest.dto.UserDTO;
import com.bridgelabz.usertest.model.User;

import java.util.List;

public interface IUserService {

    String registerUser(UserDTO dto);

    List<User> getUser();

    User loginUser(LoginDTO dto);

    User getById(String token);

    Object forgotPassword(String email);

    String resetPassword(String token, String password);

    Object deleteById(String token);

}
