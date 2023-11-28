package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.CustomizedField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomizedRepository extends JpaRepository<CustomizedField, Long> {
}
