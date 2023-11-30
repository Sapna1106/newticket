package com.example.TicketModule.repository;

import com.example.TicketModule.entity.Worklogs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorklogRepository extends JpaRepository<Worklogs ,Long> {


    List<Worklogs> findByTicketId(Long ticketId);

}
