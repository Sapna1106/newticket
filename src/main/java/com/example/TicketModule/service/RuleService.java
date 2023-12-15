package com.example.TicketModule.service;

import com.example.TicketModule.dto.rule.RuleRequestDTO;
import com.example.TicketModule.entity.*;
import com.example.TicketModule.entity.actionConditionType.*;
import com.example.TicketModule.entity.triggerConditionTypes.*;
import com.example.TicketModule.enums.ConditionOnAction;
import com.example.TicketModule.enums.ConditionOnTrigger;
import com.example.TicketModule.exception.RuleNotFoundException;
import com.example.TicketModule.repository.RuleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
  @Autowired private RuleRepo ruleRepo;

  @Autowired private ProjectService projectService;


  private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

  public List<Rule> getAllRule(long projectId) {
    try {
      List<Rule> ruleList = ruleRepo.findByProjectId(projectId);
      return ruleList;
    } catch (Exception e) {
      logger.error(
          "Error occurred while fetching rules for project with ID {}: {}",
          projectId,
          e.getMessage());
      throw e;
    }
  }

  public Rule getRuleById(long projectID, long ruleID) {
    try {
        Optional<Rule> ruleOptional = ruleRepo.findByProjectIdAndRuleId(projectID,ruleID);
        if (ruleOptional.isPresent()) {
          logger.info("Fetched rule with ID {} for project with ID: {}", ruleID, projectID);
          return ruleOptional.get();
        } else {
          logger.error("Rule not found with ID {} for project with ID: {}", ruleID, projectID);
          throw new RuleNotFoundException("Rule with ID " + ruleID + " not found");
        }
    } catch (Exception e) {
      logger.error(
          "Error occurred while fetching rule with ID {} for project with ID {}: {}",
          ruleID,
          projectID,
          e.getMessage());
      throw e;
    }
  }

  public Rule addRules(long projectId, RuleRequestDTO addRule) {
    try {
      Rule rule = new Rule();
        rule.setProjectId(projectId);
        addRule
            .getTriggerRequestDtos()
            .forEach(
                (trigger) -> {
                  Trigger newTrigger = new Trigger();
                  System.out.println(trigger.getTriggerFieldId());

                  CustomField fieldTrigger =
                      projectService.getCustomeFieldById(trigger.getTriggerFieldId());
                  newTrigger.setTriggerField(fieldTrigger);
                  if (trigger.getTriggerCondition() == ConditionOnTrigger.STRING) {
                    StringTrigger stringTrigger = trigger.getStringTrigger();
                    newTrigger.setTriggerConditions(stringTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.NUMBER) {
                    NumberTrigger numberTrigger = trigger.getNumberTrigger();
                    newTrigger.setTriggerConditions(numberTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.DATE) {
                    DateTrigger dateTrigger = trigger.getDateTrigger();
                    newTrigger.setTriggerConditions(dateTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.ID) {
                    IdTrigger stageTrigger = trigger.getIdTrigger();
                    newTrigger.setTriggerConditions(stageTrigger);
                  } else if (trigger.getTriggerCondition() == ConditionOnTrigger.USER) {
                    UserTrigger userTrigger = trigger.getUserTrigger();
                    newTrigger.setTriggerConditions(userTrigger);
                  }
                  newTrigger.setRule(rule);
                  rule.getTriggerList().add(newTrigger);
                });
        actionSelect(addRule, rule);
        logger.info("Created rules for project with ID: {}", projectId);
        return ruleRepo.save(rule);
    } catch (Exception e) {
      logger.error(
          "Error occurred while creating rules for project with ID {}: {} {}",
          projectId,
          e.getMessage(),
          e);
      throw e;
    }
  }

  public void actionSelect(RuleRequestDTO addRule, Rule rule) {
    addRule
        .getActionRequestDtos()
        .forEach(
            action -> {
              Action newAction = new Action();
              System.out.println(action.getActionFieldId());

              CustomField fieldaAction = projectService.getCustomeFieldById(action.getActionFieldId());
              newAction.setActionField(fieldaAction);
              if (action.getActionCondition() == ConditionOnAction.ID) {
                IdAction idAction = action.getIdAction();
                newAction.setActionCondition(idAction);
              } else if (action.getActionCondition() == ConditionOnAction.NUMBER) {
                NumberAction numberAction = action.getNumberAction();
                newAction.setActionCondition(numberAction);
              } else if (action.getActionCondition() == ConditionOnAction.STRING) {
                StringAction stringAction = action.getStringAction();
                newAction.setActionCondition(stringAction);
              } else if (action.getActionCondition() == ConditionOnAction.PROJECT) {
                ProjectAction projectAction = action.getProjectAction();
                newAction.setActionCondition(projectAction);
              } else if (action.getActionCondition() == ConditionOnAction.DATE) {
                DateAction dateAction = action.getDateAction();
                newAction.setActionCondition(dateAction);
              } else if (action.getActionCondition() == ConditionOnAction.USER) {
                UserAction userAction = action.getUserAction();
                newAction.setActionCondition(userAction);
              }
              newAction.setRule(rule);
              rule.getActionList().add(newAction);
            });
  }

  public void deleteRule(Long projectId, Long ruleId) {
    try {
        Optional<Rule> ruleOptional = ruleRepo.findByProjectIdAndRuleId(projectId,ruleId);
        if(ruleOptional.isPresent()){
            ruleRepo.deleteById(ruleId);
        }else {
            throw new RuleNotFoundException("Rule Not Found");
        }
      logger.info("Deleted rule with ID {} in project with ID: {}", ruleId, projectId);
    } catch (Exception e) {
      logger.error(
          "Error occurred while deleting rule with Id {} in project with ID {}: {}",
          ruleId,
          projectId,
          e.getMessage());
      throw e;
    }
  }

  public List<Rule> getRuleByTriggerFieldAndProjectId(CustomField triggerField,Long projectId){
      return ruleRepo.findByTriggerList_TriggerFieldAndProjectId(triggerField,projectId);
  }
}
