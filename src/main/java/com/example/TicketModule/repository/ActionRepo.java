package com.example.TicketModule.repository;

import com.example.TicketModule.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepo extends JpaRepository<Action,Long> {

}
