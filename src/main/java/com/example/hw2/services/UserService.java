package com.example.hw2.services;

import com.example.hw2.models.User;
import com.example.hw2.helpers.ValidatorHelper;
import com.example.hw2.helpers.TokenHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    private Map<String, User> users = new HashMap<>();
    private Map<String, String> blacklist = new HashMap<>();

    public int saveUser(User user){
        if(!ValidatorHelper.validate(user.getEmail())){
            return 2;
        }
        if(users.containsKey(user.getEmail())){
            return 0;
        }
        users.put(user.getEmail(), user);
        return 1;
    }

    public User getUserById(Integer id){
        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if(userEntry.getValue().getId() == (id)){
                return users.get(userEntry.getKey());
            }
        }
        return users.get(id);
    }

    public void deleteById(Integer id){
        for (Map.Entry<String, User> userEntry : users.entrySet()) {
            if(userEntry.getValue().getId() == (id)){
                users.remove(userEntry.getKey());
            }
        }
    }

    public boolean checkUser(User user){
        if(users.get(user.getEmail()).getPassword().equals(user.getPassword())){
            return true;
        }
        return false;
    }

    public boolean validate(String token){
        if(blacklist.containsValue(token)){
            return false;
        }
        if(users.get(TokenHelper.getEmailByToken(token))==null){
            return false;
        }
        return true;
    }

        public void blocktoken(String email, String token){
            blacklist.put(email, token);
        }

    public User getUserByToken(String token){
        return users.get(TokenHelper.getEmailByToken(token));
    }

    public void updateUser(User user_old, User user_new){
        user_old.setEmail(user_new.getEmail());
        user_old.setPassword(user_new.getPassword());
    }

}
