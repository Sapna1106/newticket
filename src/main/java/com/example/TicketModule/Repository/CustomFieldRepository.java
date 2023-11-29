package com.example.TicketModule.Repository;

import com.example.TicketModule.Entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {

    List<CustomField> findByProjectId(Long projectId);

    Optional<CustomField> findByFieldName(String name);
}
