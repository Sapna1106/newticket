package com.example.TicketModule.repository;
import java.util.List;
import java.util.Optional;

import com.example.TicketModule.entity.CustomField;
import com.example.TicketModule.entity.Project;
import com.example.TicketModule.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepo extends JpaRepository<Rule, Long> {

//    @Query("SELECT r FROM Rule r JOIN r.triggerList t WHERE t.triggerField = :triggerField AND r.projectId = :projectId")
//    List<Rule> findByTriggerFieldAndProjectId(@Param("triggerField") CustomField triggerField, @Param("projectId") Project project);

    List<Rule> findByTriggerList_TriggerFieldAndProjectId(CustomField triggerField, Long project);

    List<Rule> findByTriggerList_TriggerField(CustomField triggerField);

    List<Rule> findByProjectId(Long projectId);

    Optional<Rule> findByProjectIdAndRuleId(Long projectId, Long ruleId);

}