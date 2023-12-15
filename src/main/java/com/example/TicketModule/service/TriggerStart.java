package com.example.TicketModule.service;

import com.example.TicketModule.entity.*;
import com.example.TicketModule.entity.triggerConditionTypes.*;
import com.example.TicketModule.repository.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.example.TicketModule.constants.TriggerConstant.*;

@Service
@Slf4j
public class TriggerStart {
  @Autowired private TicketRepository ticketRepository;
  //  @Autowired private StageService stageService;
  @Autowired private ActionStart actionStart;

  @Autowired private RuleService ruleService;

  //  @Autowired private StageRepo stageRepo;

  @Autowired private TriggerRepo triggerRepo;

  @Autowired private ProjectService projectService;

  @Autowired private ResponseWebSocket responseWebSocket;

  public void handleTicketUpdate(Ticket existing, Ticket updated, Long projectId)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    log.info("Class: TriggerStart, Method: handleTicketUpdateTrigger, Execution Start");
    Class<?> ticketClass = Ticket.class;
    Method[] methods = ticketClass.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {

        String attribute = method.getName().substring(3);
        if (attribute.equals(FIELD_CUSTOMEFIELD)) {
          handleCustomFieldChanges(existing, updated, projectId);
        } else {
          CustomField customField = projectService.getCustomFieldsByName(attribute);
          handleNonCustomFieldChanges(customField, existing, updated, method, projectId);
        }
      }
    }
    log.info("Class: TriggerStart, Method: handleTicketUpdateTrigger, Execution End");
  }

  public boolean checkChangeCondition(Object existingValue, Object updatedValue) {
    return ((updatedValue != null && existingValue == null)
        || (updatedValue != null && existingValue != null && !updatedValue.equals(existingValue)));
  }

  private void handleCustomFieldChanges(Ticket existing, Ticket updated, Long projectId)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    log.info("Class: TriggerStart, Method: handleCustomFieldChanges, Execution Start");

    Map<String, Object> existingValue = (existing != null) ? existing.getCustomFields() : null;
    Map<String, Object> updatedValue = (updated != null) ? updated.getCustomFields() : null;

    if (updatedValue == null) {
      log.warn("customField is null.");
      return;
    }

    if (checkChangeCondition(existingValue, updatedValue)) {
      log.info(
          "Class: TriggerStart, Method: handleCustomFieldChanges, Custom Field Method Started");
      for (Map.Entry<String, Object> entry : updatedValue.entrySet()) {
        String key = entry.getKey();
        Object updateValueObject = entry.getValue();
        Object existingValueObject =
            (existing != null) ? existing.getCustomFields().get(key) : null;
        findCustomFieldChanges(
            key, existingValueObject, updateValueObject, existing, updated, projectId);
      }
    }
    log.info("Class: TriggerStart, Method: handleCustomFieldChanges, Execution End");
  }

  public void findCustomFieldChanges(
      String key,
      Object existingValuseObject,
      Object updatedValueObject,
      Ticket existing,
      Ticket updated,
      Long projectId)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: findCustomFieldChanges, Execution Start");
    CustomField customField = projectService.getCustomFieldsByName(key);

    if(customField == null)return;

    if (DATA_TYPE_STRING.equals(customField.getDataType())) {
      String existingValue = (existingValuseObject != null) ? (String) existingValuseObject : null;
      String updatedValue = (updatedValueObject != null) ? (String) updatedValueObject : null;
      if (checkChangeCondition(existingValue, updatedValue)) {
        List<Rule> ruleList = ruleService.getRuleByTriggerFieldAndProjectId(customField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
          }
        }
      }
    } else if (DATA_TYPE_NUMBER.equals(customField.getDataType())) {
      Long existingValue = (existingValuseObject != null) ? (Long) existingValuseObject : null;
      Long updatedValue = (updatedValueObject != null) ? (Long) updatedValueObject : null;
      if (checkChangeCondition(existingValue, updatedValue)) {
        List<Rule> ruleList = ruleService.getRuleByTriggerFieldAndProjectId(customField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
          }
        }
      }
    }
    log.info("Class: TriggerStart, Method: evaluateCustomFieldDataType, Execution End");
  }

  public void handleNonCustomFieldChanges(
      CustomField nonCustomField, Ticket existing, Ticket updated, Method method, Long projectId)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

    log.info("Class: TriggerStart, Method: handleNonCustomFieldChanges, Execution Start");

    if (nonCustomField == null) {
      log.warn("Field is null.");
      return;
    }

    if (DATA_TYPE_STRING.equals(nonCustomField.getDataType())) {
      String existingValue = (existing != null) ? (String) method.invoke(existing) : null;
      String updatedValue = (updated != null) ? (String) method.invoke(updated) : null;

      log.info("Existing Value: {} ----- Updated Value: {}", existingValue, updatedValue);

      if (checkChangeCondition(existingValue, updatedValue)) {
        log.info("String trigger condition met");
        List<Rule> ruleList = ruleService.getRuleByTriggerFieldAndProjectId(nonCustomField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            actionStart.startAction(rule.getActionList(), updated);
            log.info("Actions executed Successfully");
          }
        }
      }
    } else if (DATA_TYPE_NUMBER.equals(nonCustomField.getDataType())) {
      Long existingValue = (existing != null) ? (Long) method.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) method.invoke(updated) : null;

      log.info("Existing Value: {} ----- Updated Value: {}", existingValue, updatedValue);

      if (checkChangeCondition(existingValue, updatedValue)) {
        log.info("Number trigger condition met");
        List<Rule> ruleList = ruleService.getRuleByTriggerFieldAndProjectId(nonCustomField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            log.info("Actions started");
            actionStart.startAction(rule.getActionList(), updated);
            // responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    } else if (DATA_TYPE_ID.equals(nonCustomField.getDataType())) {
      Long existingValue = (existing != null) ? (Long) method.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) method.invoke(updated) : null;

      log.info("Existing Value: {} ----- Updated Value: {}", existingValue, updatedValue);

      if (checkChangeCondition(existingValue, updatedValue)) {
        log.info("Stage trigger condition met");
        List<Rule> ruleList = ruleService.getRuleByTriggerFieldAndProjectId(nonCustomField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            log.info("Actions started");
            actionStart.startAction(rule.getActionList(), updated);
            // responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    }
    //    else if (DATA_TYPE_USER.equals(customField.getDataType())) {
    //      User existingValue = (existing != null) ? (User) method.invoke(existing) : null;
    //      User updatedValue = (updated != null) ? (User) method.invoke(updated) : null;
    //
    //      System.out.println(existingValue + "-----" + updatedValue);
    //
    //      if ((updatedValue != null && existingValue == null)
    //          || (updatedValue == null && existingValue != null)
    //          || (updatedValue != null
    //              && existingValue != null
    //              && !updatedValue.equals(existingValue))) {
    //        System.out.println("user trigger");
    //        List<Rule> ruleList =
    //            ruleRepo.findByTriggerList_TriggerFieldAndProjectId(customField, projectId);
    //        for (Rule rule : ruleList) {
    //          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
    //            System.out.println("actions stated");
    //            actionStart.startAction(rule.getActionList(), updated);
    //            //                responseWebSocket.sendMessaage(rule.getRuleId());
    //          }
    //        }
    //      }
    //    }
    log.info("Class: TriggerStart, Method: executeTriggerActionForField, Execution End");
  }

  public boolean checkTrigger(List<Trigger> triggerList, Ticket existing, Ticket updated)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: checkTrigger, Execution Start");
    for (Trigger trigger : triggerList) {
      String name = trigger.getTriggerField().getFieldName();
      if (FIELD_STATUS.equals(name)
          || FIELD_PROIORTY.equals(name)
          || FIELD_ACCOUNTABLEASSINEE.equals(name)
          || FIELD_ASSIGNEE.equals(name)
          || FIELD_CREATEDAT.equals(name)
          || FIELD_ENDDATE.equals(name)
          || FIELD_DESCRIPTION.equals(name)
          || FIELD_ID.equals(name)
          || FIELD_DESCRIPTION.equals(name)) {
        if (!nonCustomeFieldCheck(trigger, existing, updated)) {
          return false;
        }
      } else {
        if (!customFieldCheck(trigger, existing, updated)) {
          return false;
        }
      }
    }
    log.info("Class: TriggerStart, Method: checkTrigger, Execution End");
    return true;
  }

  private boolean customFieldCheck(Trigger trigger, Ticket existing, Ticket updated)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: customFieldCheck, Execution Start");

    Map<String, Object> existingcustomFields = updated.getCustomFields();
    Map<String, Object> updatedcustomFields = updated.getCustomFields();
    Object existingValueObject = existingcustomFields.get(trigger.getTriggerField().getFieldName());
    Object updatedValueObject = updatedcustomFields.get(trigger.getTriggerField().getFieldName());
    if (DATA_TYPE_STRING.equals(trigger.getTriggerField().getDataType())) {
      String existingValue = (String) existingValueObject;
      String updatedValue = (String) updatedValueObject;
      if (!triggerOnString(trigger, existingValue, updatedValue, updated)) {
        log.info("String trigger condition not met. Exiting method.");
        return false;
      }
      log.info("{} -- changed -- {}", existingValue, updatedValue);
    } else if (DATA_TYPE_NUMBER.equals(trigger.getTriggerField().getDataType())) {
      Long existingValue = (Long) existingValueObject;
      Long updatedValue = (Long) updatedValueObject;
      if (!triggerOnNumber(trigger, existingValue, updatedValue, updated)) {
        log.info("Number trigger condition not met. Exiting method.");
        return false;
      }
    } else if (DATA_TYPE_DATE.equals(trigger.getTriggerField().getDataType())) {
      if (!DateCheck(trigger, updated)) {
        log.info("Date trigger condition not met. Exiting method.");
        return false;
      }
    }

    log.info("Class: TriggerStart, Method: customFieldCheck, Execution End");
    return true;
  }

  public boolean nonCustomeFieldCheck(Trigger trigger, Ticket existing, Ticket updated)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: nonCustomeFieldCheck, Execution Start");

    Class<?> ticketClass = Ticket.class;
    String name = trigger.getTriggerField().getFieldName();
    String attributeName = "get" + capitalizeFirstLetter(name);

    if (DATA_TYPE_STRING.equals(trigger.getTriggerField().getDataType())) {
      Method getterMethod = ticketClass.getMethod(attributeName);
      String existingValue = (existing != null) ? (String) getterMethod.invoke(existing) : null;
      String updatedValue = (updated != null) ? (String) getterMethod.invoke(updated) : null;
      if (!triggerOnString(trigger, existingValue, updatedValue, updated)) {
        log.info("String trigger condition not met. Exiting method.");
        return false;
      }
      log.info("{} -- changed -- {}", existingValue, updatedValue);
    } else if (DATA_TYPE_ID.equals(trigger.getTriggerField().getDataType())) {
      Method getterMethod = ticketClass.getMethod(attributeName);
      log.info(attributeName);
      Long existingValue = (existing != null) ? (Long) getterMethod.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) getterMethod.invoke(updated) : null;
      if (!idTrigger(trigger, existingValue, updatedValue, updated)) {
        log.info("Stage trigger condition not met. Exiting method.");
        return false;
      }
    } else if (DATA_TYPE_NUMBER.equals(trigger.getTriggerField().getDataType())) {
      Method getterMethod = ticketClass.getMethod(attributeName);
      Long existingValue = (existing != null) ? (Long) getterMethod.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) getterMethod.invoke(updated) : null;
      if (!triggerOnNumber(trigger, existingValue, updatedValue, updated)) {
        log.info("Number trigger condition not met. Exiting method.");
        return false;
      }
    } else if (DATA_TYPE_DATE.equals(trigger.getTriggerField().getDataType())) {
      if (!DateCheck(trigger, updated)) {
        log.info("Date trigger condition not met. Exiting method.");
        return false;
      }
    } else if (DATA_TYPE_USER.equals(trigger.getTriggerField().getDataType())) {
      log.info(attributeName);
      System.out.println(updated);
      List<User> existingValue = (existing != null) ? existing.getAssignee() : null;
      List<User> updatedValue = (updated != null) ? updated.getAssignee() : null;
      if (!triggerOnUser(trigger, existingValue, updatedValue, updated)) {
        log.info("User trigger condition not met. Exiting method.");
        return false;
      }
    }
    log.info("Class: TriggerStart, Method: nonCustomeFieldCheck, Execution End");
    return true;
  }

  private String capitalizeFirstLetter(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  public boolean triggerOnUser(
      Trigger trigger, List<User> existingValueList, List<User> updatedValueList, Ticket ticket) {
    log.info("Class: TriggerStart, Method: triggerOnUser, Execution Start");

    UserTrigger userTrigger = (UserTrigger) trigger.getTriggerConditions();
    log.info("User Trigger: {}", userTrigger);
    log.info("Operation: {} --- operation", userTrigger.getOperation());
    if (OPERATION_SET.equals(userTrigger.getOperation())) {
      for(User updatedValue:updatedValueList){
        log.info("{} ---- {}", updatedValue, userTrigger.getCurrent());
        if (updatedValue != null
                && userTrigger.getCurrent() != null
                && updatedValue.getId().equals(userTrigger.getCurrent())) {
          log.info("Class: TriggerStart, Method: triggerOnUser, Execution End");
          return true;
        }
      }
      log.info("Set User executed");
    } else if (OPERATION_CHANGE.equals(userTrigger.getOperation())) {
//      if (updatedValue != null
//          && existingValue != null
//          && userTrigger.getCurrent() != null
//          && userTrigger.getPrevious() != null
//          && updatedValue.equals(userTrigger.getCurrent())
//          && existingValue.equals(userTrigger.getPrevious())) {
//        log.info("String change started");
//        log.info("Class: TriggerStart, Method: triggerOnUser, Execution End");
//        return true;
//      }
    } else if (OPERATION_REMOVE.equals(userTrigger.getOperation())) {
      for(User existingValue : existingValueList){
        if (existingValue != null
                && userTrigger.getPrevious() != null
                && existingValue.getId().equals(userTrigger.getPrevious())) {
          log.info("String remove started");
          log.info("Class: TriggerStart, Method: triggerOnUser, Execution End");
          return true;
        }
      }
    }
    log.info("Class: TriggerStart, Method: triggerOnUser, Execution End");
    return false;
  }

  public boolean triggerOnNumber(
      Trigger trigger, Long existingValue, Long updatedValue, Ticket ticket) {
    NumberTrigger numberTrigger = (NumberTrigger) trigger.getTriggerConditions();
    log.info("Class: TriggerStart, Method: triggerOnNumber, Execution Start");
    log.info("Updated Value: {}", updatedValue);
    log.info("Number Trigger: {}", numberTrigger);
    log.info("Operation: {} --- less", numberTrigger.getOperation());

    if (OPERATION_LESS.equals(numberTrigger.getOperation())) {
      log.info("{} ---- {}", updatedValue, numberTrigger.getValue());
      if (updatedValue < numberTrigger.getValue()) {
        log.info("Number less started");
        log.info("Class: TriggerStart, Method: triggerOnNumber, Execution End");
        return true;
      }
      log.info("Number less not triggered");
    } else if (OPERATION_EQUAL.equals(numberTrigger.getOperation())) {
      log.info("{} ---- {}", updatedValue, numberTrigger.getValue());
      if (updatedValue.equals(numberTrigger.getValue())) {
        log.info("Number equal started");
        log.info("Class: TriggerStart, Method: triggerOnNumber, Execution End");
        return true;
      }
      log.info("Number equal not triggered");
    } else if (OPERATION_GREATER.equals(numberTrigger.getOperation())) {
      log.info("{} ---- {}", updatedValue, numberTrigger.getValue());
      if (updatedValue > numberTrigger.getValue()) {
        log.info("Number greater started");
        log.info("Class: TriggerStart, Method: triggerOnNumber, Execution End");
        return true;
      }
      log.info("Number greater not triggered");
    }

    log.info("Class: TriggerStart, Method: triggerOnNumber, Execution End");
    return false;
  }

  public boolean triggerOnString(
      Trigger trigger, String existingValue, String updatedValue, Ticket ticket) {
    StringTrigger stringTrigger = (StringTrigger) trigger.getTriggerConditions();
    log.info("Class: TriggerStart, Method: triggerOnString, Execution Start");
    log.info("Updated Value: {}", updatedValue);
    log.info("String Trigger: {}", stringTrigger);
    log.info("Operation: {} --- less", stringTrigger.getOperation());

    if (OPERATION_SET.equals(stringTrigger.getOperation())) {
      log.info("{} ---- {}", updatedValue, stringTrigger.getCurrentString());
      if (updatedValue != null
          && stringTrigger.getCurrentString() != null
          && updatedValue.equals(stringTrigger.getCurrentString())) {
        log.info("String set started");
        log.info("Class: TriggerStart, Method: triggerOnString, Execution End");
        return true;
      }
      log.info("String set not triggered");
    } else if (OPERATION_CHANGE.equals(stringTrigger.getOperation())) {
      log.info("{} ---- {}", updatedValue, stringTrigger.getCurrentString());
      log.info("{} ---- {}", existingValue, stringTrigger.getPreviousString());
      if (updatedValue != null
          && existingValue != null
          && stringTrigger.getCurrentString() != null
          && stringTrigger.getPreviousString() != null
          && updatedValue.equals(stringTrigger.getCurrentString())
          && existingValue.equals(stringTrigger.getPreviousString())) {
        log.info("String change started");
        log.info("Class: TriggerStart, Method: triggerOnString, Execution End");
        return true;
      }
      log.info("String change not triggered");
    } else if (OPERATION_REMOVE.equals(stringTrigger.getOperation())) {
      log.info("{} ---- {}", existingValue, stringTrigger.getPreviousString());
      if (existingValue != null
          && stringTrigger.getPreviousString() != null
          && existingValue.equals(stringTrigger.getPreviousString())) {
        log.info("String remove started");
        log.info("Class: TriggerStart, Method: triggerOnString, Execution End");
        return true;
      }
      log.info("String remove not triggered");
    }

    log.info("Class: TriggerStart, Method: triggerOnString, Execution End");
    return false;
  }

  public boolean idTrigger(
      Trigger trigger, Long existingStage, Long currentStage, Ticket ticket) {
    log.info("Class: TriggerStart, Method: idTrigger, Execution Start");
    log.info("Stage Trigger started");
    IdTrigger idTrigger = (IdTrigger) trigger.getTriggerConditions();
    log.info("Operation: {}", idTrigger.getOperation());

    if (OPERATION_SET.equals(idTrigger.getOperation())) {
      log.info("{}00{}", currentStage, idTrigger.getCurrentId());
      if (currentStage != null
          && idTrigger.getCurrentId() != null
          && currentStage.equals(idTrigger.getCurrentId())) {
        log.info("Stage set executed");
        log.info("Class: TriggerStart, Method: idTrigger, Execution End");
        return true;
      }
      log.info("Stage set not triggered");
    } else if (OPERATION_CHANGE.equals(idTrigger.getOperation())) {
      log.info("{}00{}", existingStage, idTrigger.getPreviousId());
      log.info("{}11{}", currentStage, idTrigger.getCurrentId());

      if (existingStage != null
          && currentStage != null
          && idTrigger.getCurrentId() != null
          && idTrigger.getPreviousId() != null
          && idTrigger.getPreviousId().equals(existingStage)
          && idTrigger.getCurrentId().equals(currentStage)) {
        log.info("Stage change executed");
        log.info("Class: TriggerStart, Method: idTrigger, Execution End");
        return true;
      }
      log.info("Stage change not triggered");
    } else if (OPERATION_REMOVE.equals(idTrigger.getOperation())) {
      if (existingStage != null
          && idTrigger.getPreviousId() != null
          && idTrigger.getPreviousId().equals(existingStage)) {
        log.info("Stage removed executed");
        log.info("Class: TriggerStart, Method: idTrigger, Execution End");
        return true;
      }
      log.info("Stage removed not triggered");
    }

    log.info("Class: TriggerStart, Method: idTrigger, Execution End");
    return false;
  }

  public boolean triggerOnDate(Trigger trigger)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

    log.info("Class: TriggerStart, Method: triggerOnDate, Execution Start");

    Rule rule = trigger.getRule();
    Long projectId = rule.getProjectId();
    Project project = projectService.getProjectById(projectId);

    if (project != null) {
      log.info("Project found with ID: {}", projectId);
      Date currentDate = new Date();
      List<Ticket> ticketList = ticketRepository.findByProjectId(projectId);

      for (Ticket ticket : ticketList) {
        if (checkTrigger(trigger.getRule().getTriggerList(), null, ticket)) {
          log.info("Trigger condition met for ticket with ID: {}", ticket.getId());
          log.info("Action Started on Date ");
          actionStart.startAction(rule.getActionList(), ticket);
          Ticket savedTicket = ticketRepository.save(ticket);
           responseWebSocket.sendResponse(savedTicket);
           responseWebSocket.sendMessaage(rule.getRuleId());
        }
      }
    } else {
      log.warn("Project not found with ID: {}", projectId);
    }

    log.info("Class: TriggerStart, Method: triggerOnDate, Execution End");
    return false;
  }

  public boolean DateCheck(Trigger trigger, Ticket ticket)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: DateCheck, Execution Start");

    DateTrigger dateTrigger = (DateTrigger) trigger.getTriggerConditions();
    if (OPERATION_BEFORE.equals(dateTrigger.getOperation())) {
      log.info("Operation: BEFORE, Executing...");
      return dueDateApprocha(
          dateTrigger.getDate(),
          -dateTrigger.getDays(),
          -dateTrigger.getHours(),
          -dateTrigger.getMinuter(),
          ticket,
          trigger);
    } else if (OPERATION_EQUAL.equals(dateTrigger.getOperation())) {
      log.info("Operation: EQUAL, Executing...");
      return dueDateApprocha(dateTrigger.getDate(), 0, 0, 0, ticket, trigger);
    } else if (OPERATION_AFTER.equals(dateTrigger.getOperation())) {
      log.info("Operation: AFTER, Executing...");
      return dueDateApprocha(
          dateTrigger.getDate(),
          dateTrigger.getDays(),
          dateTrigger.getHours(),
          dateTrigger.getMinuter(),
          ticket,
          trigger);
    }

    log.info("Class: TriggerStart, Method: DateCheck, Execution End");
    return false;
  }

  public boolean dueDateApprocha(
      Date ondate, int days, int hours, int minute, Ticket ticket, Trigger trigger)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    log.info("Class: TriggerStart, Method: dueDateApprocha, Execution Start");

    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    String attribute = trigger.getTriggerField().getFieldName();
    Date dateField = null;
    if (FIELD_ENDDATE.equals(trigger.getTriggerField().getFieldName())
        || FIELD_STARTDATE.equals(trigger.getTriggerField().getFieldName())) {
      String attributeName = "get" + capitalizeFirstLetter(attribute);
      Class<?> ticketClass = Ticket.class;
      Method getterMethod = ticketClass.getMethod(attributeName);
      dateField = (Date) getterMethod.invoke(ticket);
    } else {
      Map<String, Object> customFields = ticket.getCustomFields();
      String dateString = (String) customFields.get(trigger.getTriggerField().getFieldName());
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
      try {
        dateField = dateFormat.parse(dateString);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    if (dateField != null) {
      log.info("Field: {}", attribute);
      calendar.setTime(dateField);
      calendar.add(Calendar.DAY_OF_MONTH, days);
      calendar.add(Calendar.HOUR, hours);
      calendar.add(Calendar.MINUTE, minute);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Date notificationDate = calendar.getTime();

      Calendar currentCalendar = Calendar.getInstance();
      if (ondate != null) {
        currentCalendar.setTime(currentDate);
      }
      currentCalendar.set(Calendar.MILLISECOND, 0);
      Date truncatedCurrentDate = currentCalendar.getTime();

      log.info(
          "Notification Date: {}, Truncated Current Date: {}",
          notificationDate,
          truncatedCurrentDate);

      if (notificationDate.compareTo(truncatedCurrentDate) == 0) {
        log.info("Notification Date equals Truncated Current Date. Triggering action.");
        return true;
      }
    }

    log.info("Class: TriggerStart, Method: dueDateApprocha, Execution End");
    return false;
  }

  @Scheduled(cron = "0 * * * * *")
  public void startEveryMin() {
    List<CustomField> fieldList = projectService.getCustomFieldByDataType("DATE");
    fieldList.forEach(
        field -> {
          List<Trigger> triggerList = triggerRepo.findByTriggerField(field);
          if (!triggerList.isEmpty()) {
            for (Trigger trigger : triggerList) {
              try {
                triggerOnDate(trigger);
              } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
              } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            }
          }
        });
  }
}
