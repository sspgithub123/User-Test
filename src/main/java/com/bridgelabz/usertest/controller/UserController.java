package com.bridgelabz.usertest.controller;


import com.bridgelabz.usertest.dto.LoginDTO;
import com.bridgelabz.usertest.dto.ResponseDTO;
import com.bridgelabz.usertest.dto.UserDTO;
import com.bridgelabz.usertest.model.User;
import com.bridgelabz.usertest.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService service;


    @PostMapping({"/register"})
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserDTO dto){
        String user = service.registerUser(dto);
        ResponseDTO response = new ResponseDTO("User Registered", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<String> getUser(){
        List<User> user = service.getUser();
        ResponseDTO response = new ResponseDTO("Users", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@Valid @RequestBody LoginDTO dto){
        ResponseDTO response = new ResponseDTO("User Login successefully: ", service.loginUser(dto));
        return new ResponseEntity<ResponseDTO>(response,HttpStatus.OK);
    }


    @GetMapping({"/findById/{token}"})
    public ResponseEntity<ResponseDTO> getById(@PathVariable String token) {
        User user = this.service.getById(token);
        ResponseDTO response = new ResponseDTO("Requested User : ", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email){
        Object user = service.forgotPassword(email);
        ResponseDTO response = new ResponseDTO("Check your email to reset the password", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestParam String token, @RequestParam String password){

        String user = service.resetPassword(token, password);
        ResponseDTO response = new ResponseDTO("Reset Password", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //to delete specific user using token provided
    @DeleteMapping({"/delete/{token}"})
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String token) {
        ResponseDTO response = new ResponseDTO("User deleted successfully", service.deleteById(token));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
