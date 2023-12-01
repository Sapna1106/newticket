package com.example.TicketModule.repository;
import java.util.List;

import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepo extends JpaRepository<Trigger,Long> {
    public List<Trigger> findByTriggerField(CustomField triggerField);
}
