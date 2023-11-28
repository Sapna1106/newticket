package com.example.TicketModule.Service;

import com.example.TicketModule.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.TicketModule.Entity.User;
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
}
