package com.example.TicketModule.repository;

import com.example.TicketModule.entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {

    List<CustomField> findByProjectId(Long projectId);

    CustomField findByFieldName(String name);

    List<CustomField> findByDataType(String datatype);
}
