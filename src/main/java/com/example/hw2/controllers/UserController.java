package com.example.hw2.controllers;

import com.example.hw2.models.User;
import com.example.hw2.services.UserService;
import com.example.hw2.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/helloWorld")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/register")
    public ResponseEntity Register(@RequestBody User user){
        int res = userService.saveUser(user);
        if(res==2){
            return new ResponseEntity("Invalid email", HttpStatus.BAD_REQUEST);
        }
        if(res==1){
            return ResponseEntity.ok(user);
        }
        return new ResponseEntity("User already exist", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody User user){
        if(userService.checkUser(user)){
            return ResponseEntity.ok(TokenHelper.getToken(user.getEmail()));
        }
        return new ResponseEntity("Login failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/read") public ResponseEntity readUserById(@RequestParam int id,@RequestHeader("Authorization") String token) {
        if(!userService.validate(token.split(" ")[1])){
            return new ResponseEntity("You don't have access to this method", HttpStatus.NOT_FOUND);
        }
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteById(@RequestParam int id,@RequestHeader("Authorization") String token){
        if(!userService.validate(token.split(" ")[1])){
            return new ResponseEntity("You don't have access to this method", HttpStatus.NOT_FOUND);
        }
        userService.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/update")
    public ResponseEntity updateById(
            @RequestParam int id,
            @RequestBody User user,
            @RequestHeader("Authorization") String token){
        if(!userService.validate(token.split(" ")[1])){
            return new ResponseEntity("You don't have access to this method", HttpStatus.NOT_FOUND);
        }
        User user_old = userService.getUserById(id);
        if (user_old == null) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        userService.updateUser(user_old, user);
        return ResponseEntity.ok(user_old);
    }

}
