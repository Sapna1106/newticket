package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {

    List<CustomField> findByProjectId(Long projectId);
}
