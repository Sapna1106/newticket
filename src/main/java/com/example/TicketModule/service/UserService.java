package com.example.TicketModule.service;

import com.example.TicketModule.exception.UserNotFoundException;
import com.example.TicketModule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.TicketModule.entity.User;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not exist  With the Id userId:"+userId));
    }
}
