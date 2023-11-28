package com.example.TicketModule.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.TicketModule.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
