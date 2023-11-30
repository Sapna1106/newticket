package com.example.TicketModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TicketModule.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
