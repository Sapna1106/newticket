package com.example.TicketModule.service;

import com.example.TicketModule.entity.*;
import com.example.TicketModule.entity.triggerConditionTypes.*;
import com.example.TicketModule.repository.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TriggerStart {
  private static final Logger logger = LoggerFactory.getLogger(TriggerStart.class);
  @Autowired private ProjectRepository projectRepo;

  @Autowired private TicketRepository ticketRepo;
  //  @Autowired private StageService stageService;
  @Autowired private ActionStart actionStart;

  //  @Autowired private StageRepo stageRepo;

  @Autowired private CustomFieldRepository fieldRepo;

  @Autowired private RuleRepo ruleRepo;

  @Autowired private TriggerRepo triggerRepo;

  @Autowired private ProjectService projectService;

  //  @Autowired private ResponseWebSocket responseWebSocket;

  public void triggerOnUpdate(Ticket existing, Ticket updated, Long projectId)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Class<?> ticketClass = Ticket.class;
    Method[] methods = ticketClass.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {

        String attribute = method.getName().substring(3);

        CustomField customField = fieldRepo.findByFieldName(attribute);
        if (attribute.equals("Status")
            || attribute.equals("Priority")
            || attribute.equals("StageId")
            || attribute.equals("EndDate")) {
          checkMethodToExicute(customField, existing, updated, method, projectId);
        } else {
//          customeFieldMethod(customField, existing, updated, method, projectId);
        }
      }
    }
  }

  private void customeFieldMethod(
      CustomField customField, Ticket existing, Ticket updated, Method method, Long projectId) throws InvocationTargetException, IllegalAccessException, JsonProcessingException {
//    String existingCustomFields = existing.getCustomFields();
//    String updatedCustomFields = updated.getCustomFields();
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    JsonNode jsonNode = objectMapper.readTree(existingCustomFields);

    // Convert JsonNode to Map
//    Map<String, Object> resultMap = jsonToMap(jsonNode);
  }

  public void checkMethodToExicute(
      CustomField customField, Ticket existing, Ticket updated, Method method, Long projectId)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

    if (customField == null) return;

    if (customField.getDataType().equals("STRING")) {

      String existingValue = (existing != null) ? (String) method.invoke(existing) : null;
      String updatedValue = (updated != null) ? (String) method.invoke(updated) : null;

      System.out.println(existingValue + "-----" + updatedValue);

      if ((updatedValue != null && existingValue == null)
          || (updatedValue == null && existingValue != null)
          || (updatedValue != null
              && existingValue != null
              && !updatedValue.equals(existingValue))) {
        System.out.println("string trigger co");
        //            System.out.println(customField+""+project);
        List<Rule> ruleList =
            ruleRepo.findByTriggerList_TriggerFieldAndProjectId(customField, projectId);
        //            System.out.println(ruleList);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
            //                responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    } else if (customField.getDataType().equals("NUMBER")) {
      Long existingValue = (existing != null) ? (Long) method.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) method.invoke(updated) : null;

      System.out.println(existingValue + "-----" + updatedValue);

      if ((updatedValue != null && existingValue == null)
          || (updatedValue == null && existingValue != null)
          || (updatedValue != null
              && existingValue != null
              && !updatedValue.equals(existingValue))) {
        System.out.println("number trigger");
        List<Rule> ruleList =
            ruleRepo.findByTriggerList_TriggerFieldAndProjectId(customField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
            //                responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    } else if (customField.getDataType().equals("STAGE")) {
      Long existingValue = (existing != null) ? (Long) method.invoke(existing) : null;
      Long updatedValue = (updated != null) ? (Long) method.invoke(updated) : null;

      System.out.println(existingValue + "-----" + updatedValue);

      if ((updatedValue != null && existingValue == null)
          || (updatedValue == null && existingValue != null)
          || (updatedValue != null
              && existingValue != null
              && !updatedValue.equals(existingValue))) {
        System.out.println("stage trigger");
        List<Rule> ruleList =
            ruleRepo.findByTriggerList_TriggerFieldAndProjectId(customField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
            //                responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    } else if (customField.getDataType().equals("USER")) {
      User existingValue = (existing != null) ? (User) method.invoke(existing) : null;
      User updatedValue = (updated != null) ? (User) method.invoke(updated) : null;

      System.out.println(existingValue + "-----" + updatedValue);

      if ((updatedValue != null && existingValue == null)
          || (updatedValue == null && existingValue != null)
          || (updatedValue != null
              && existingValue != null
              && !updatedValue.equals(existingValue))) {
        System.out.println("user trigger");
        List<Rule> ruleList =
            ruleRepo.findByTriggerList_TriggerFieldAndProjectId(customField, projectId);
        for (Rule rule : ruleList) {
          if (checkTrigger(rule.getTriggerList(), existing, updated)) {
            System.out.println("actions stated");
            actionStart.startAction(rule.getActionList(), updated);
            //                responseWebSocket.sendMessaage(rule.getRuleId());
          }
        }
      }
    }
  }

  public boolean checkTrigger(List<Trigger> triggerList, Ticket existing, Ticket updated)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    logger.info("cheack method");
    Class<?> ticketClass = Ticket.class;
    for (Trigger trigger : triggerList) {
      String name = trigger.getTriggerField().getFieldName();
      String attributeName = "get" + capitalizeFirstLetter(name);
      if (trigger.getTriggerField().getDataType().equals("STRING")) {
        Method getterMethod = ticketClass.getMethod(attributeName);
        String existingValue = (existing != null) ? (String) getterMethod.invoke(existing) : null;
        String updatedValue = (updated != null) ? (String) getterMethod.invoke(updated) : null;
        if (!triggerOnString(trigger, existingValue, updatedValue, updated)) return false;
        System.out.println(existingValue + "--changed--" + updatedValue);
      } else if (trigger.getTriggerField().getDataType().equals("STAGE")) {
        Method getterMethod = ticketClass.getMethod(attributeName);
        System.out.println(attributeName);
        Long existingValue = (existing != null) ? (Long) getterMethod.invoke(existing) : null;
        Long updatedValue = (updated != null) ? (Long) getterMethod.invoke(updated) : null;
        System.out.println("start");
        if (!stageTrigger(trigger, existingValue, updatedValue, updated)) return false;
      } else if (trigger.getTriggerField().getDataType().equals("NUMBER")) {
        Method getterMethod = ticketClass.getMethod(attributeName);
        Long existingValue = (existing != null) ? (Long) getterMethod.invoke(existing) : null;
        Long updatedValue = (updated != null) ? (Long) getterMethod.invoke(updated) : null;
        if (!triggerOnNumber(trigger, existingValue, updatedValue, updated)) return false;
      } else if (trigger.getTriggerField().getDataType().equals("DATE")) {
        if (!DateCheck(trigger, updated)) return false;
      } else if (trigger.getTriggerField().getDataType().equals("USER")) {
        Method getterMethod = ticketClass.getMethod(attributeName);
        User existingValue = (existing != null) ? (User) getterMethod.invoke(existing) : null;
        User updatedValue = (updated != null) ? (User) getterMethod.invoke(updated) : null;
        if (!triggerOnUser(trigger, existingValue, updatedValue, updated)) return false;
      }
    }
    return true;
  }

  private String capitalizeFirstLetter(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  public boolean triggerOnUser(
      Trigger trigger, User existingValue, User updatedValue, Ticket ticket) {
    logger.info("trigger on user started ");
    UserTrigger userTrigger = (UserTrigger) trigger.getTriggerConditions();
    logger.info(String.valueOf(updatedValue));
    System.out.println(userTrigger);
    logger.info(userTrigger.getOperation() + "---less");
    if (userTrigger.getOperation().equals("set")) {
      logger.info(updatedValue + "----" + userTrigger.getCurrent());
      if (updatedValue != null
          && userTrigger.getCurrent() != null
          && updatedValue.getId().equals(userTrigger.getCurrent())) {
        logger.info("string set started");
        return true;
      }
      logger.info("set string  exicuted");
    } else if (userTrigger.getOperation().equals("change")) {
      if (updatedValue != null
          && existingValue != null
          && userTrigger.getCurrent() != null
          && userTrigger.getPrevious() != null
          && updatedValue.getId().equals(userTrigger.getCurrent())
          && existingValue.getId().equals(userTrigger.getPrevious())) {
        logger.info("string change started");
        return true;
      }
    } else if (userTrigger.getOperation().equals("remove")) {
      if (existingValue != null
          && userTrigger.getPrevious() != null
          && existingValue.getId().equals(userTrigger.getPrevious())) {
        logger.info("string remove started");
        return true;
      }
    }
    return false;
  }

  public boolean triggerOnNumber(
      Trigger trigger, Long existingValue, Long updatedValue, Ticket ticket) {
    NumberTrigger numberTrigger = (NumberTrigger) trigger.getTriggerConditions();
    logger.info(String.valueOf(updatedValue));
    System.out.println(numberTrigger);
    logger.info(numberTrigger.getOperation() + "---less");
    if (numberTrigger.getOperation().equals("less")) {
      logger.info(updatedValue + "----" + numberTrigger.getValue());
      if (updatedValue < numberTrigger.getValue()) {
        logger.info("number less started");
        return true;
      }
      logger.info("set string  exicuted");
    } else if (numberTrigger.getOperation().equals("equall")) {
      System.out.println(updatedValue + "new" + numberTrigger.getValue());
      if (updatedValue == numberTrigger.getValue()) {
        logger.info("number equall  started");
        return true;
      }
    } else if (numberTrigger.getOperation().equals("greater")) {
      if (updatedValue > numberTrigger.getValue()) {
        logger.info("string remove started");
        return true;
      }
    }
    return false;
  }

  public boolean triggerOnString(
      Trigger trigger, String existingValue, String updatedValue, Ticket ticket) {
    StringTrigger stringTrigger = (StringTrigger) trigger.getTriggerConditions();
    logger.info(updatedValue);
    System.out.println(stringTrigger);
    logger.info(stringTrigger.getOperation() + "---less");
    if (stringTrigger.getOperation().equals("set")) {
      logger.info(updatedValue + "----" + stringTrigger.getCurrentString());
      if (updatedValue != null
          && stringTrigger.getCurrentString() != null
          && updatedValue.equals(stringTrigger.getCurrentString())) {
        logger.info("string set started");
        return true;
      }
    } else if (stringTrigger.getOperation().equals("change")) {
      System.out.println(updatedValue + "new" + stringTrigger.getCurrentString());
      System.out.println(existingValue + "old" + stringTrigger.getPreviousString());
      if (updatedValue != null
          && existingValue != null
          && stringTrigger.getCurrentString() != null
          && stringTrigger.getPreviousString() != null
          && updatedValue.equals(stringTrigger.getCurrentString())
          && existingValue.equals(stringTrigger.getPreviousString())) {
        logger.info("string change started");
        return true;
      }
    } else if (stringTrigger.getOperation().equals("remove")) {
      if (existingValue != null
          && stringTrigger.getPreviousString() != null
          && existingValue.equals(stringTrigger.getPreviousString())) {
        logger.info("string remove started");
        return true;
      }
    }
    return false;
  }

  public boolean stageTrigger(
      Trigger trigger, Long existingStage, Long currentStage, Ticket ticket) {
    logger.info("stageTrigger started");
    StageTrigger stageTrigger = (StageTrigger) trigger.getTriggerConditions();
    System.out.println(stageTrigger.getOperation());
    if (stageTrigger.getOperation().equals("set")) {
      System.out.println(existingStage + "00" + stageTrigger.getPreviousStage());
      System.out.println(currentStage + "11" + stageTrigger.getCurrentStage());
      if (currentStage != null
          && stageTrigger.getCurrentStage() != null
          && currentStage.equals(stageTrigger.getCurrentStage())) {
        logger.info("stage set exicuted");
        return true;
      }
    } else if (stageTrigger.getOperation().equals("change")) {
      System.out.println(existingStage + "00" + stageTrigger.getPreviousStage());
      System.out.println(currentStage + "11" + stageTrigger.getCurrentStage());

      if (existingStage != null
          && currentStage != null
          && stageTrigger.getCurrentStage() != null
          && stageTrigger.getPreviousStage() != null
          && stageTrigger.getPreviousStage().equals(existingStage)
          && stageTrigger.getCurrentStage().equals(currentStage)) {
        logger.info("stage change exicuted");
        return true;
      }
    } else if (stageTrigger.getOperation().equals("remove")) {
      if (existingStage != null
          && stageTrigger.getPreviousStage() != null
          && stageTrigger.getPreviousStage().equals(existingStage)) {
        logger.info("stage removed exicuted");
        return true;
      }
    }
    return false;
  }

  public boolean triggerOnDate(Trigger trigger)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

    Rule rule = trigger.getRule();
    Long projectId = rule.getProjectId();
    Project project = projectRepo.findById(projectId).orElse(null);
    logger.info("state cheak");
    if (project != null) {
      logger.info("date");
      Date currentDate = new Date();
      List<Ticket> ticketList = ticketRepo.findByProjectId(projectId);
      for (Ticket ticket : ticketList) {
        if (checkTrigger(trigger.getRule().getTriggerList(), null, ticket)) {
          logger.info("Action Started on Date ");
          actionStart.startAction(rule.getActionList(), ticket);
          Ticket savedTicket = ticketRepo.save(ticket);
          //          responseWebSocket.sendResponse(savedTicket);
          //          responseWebSocket.sendMessaage(rule.getRuleId());
        }
      }
    }

    return false;
  }

  public boolean DateCheck(Trigger trigger, Ticket ticket)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    DateTrigger dateTrigger = (DateTrigger) trigger.getTriggerConditions();
    if (dateTrigger.getOperation().equals("before")) {
      logger.info("before date  exicuted");
      return dueDateApprocha(
          dateTrigger.getDate(),
          -dateTrigger.getDays(),
          -dateTrigger.getHours(),
          -dateTrigger.getMinuter(),
          ticket,
          trigger);
    } else if (dateTrigger.getOperation().equals("equall")) {
      logger.info("equall date exicuted");
      return dueDateApprocha(dateTrigger.getDate(), 0, 0, 0, ticket, trigger);
    } else if (dateTrigger.getOperation().equals("after")) {
      logger.info("after date exicuted");
      return dueDateApprocha(
          dateTrigger.getDate(),
          dateTrigger.getDays(),
          dateTrigger.getHours(),
          dateTrigger.getMinuter(),
          ticket,
          trigger);
    }
    return false;
  }

  public boolean dueDateApprocha(
      Date ondate, int days, int hours, int minute, Ticket ticket, Trigger trigger)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    String attribute = trigger.getTriggerField().getFieldName();
    String attributeName = "get" + capitalizeFirstLetter(attribute);
    Class<?> ticketClass = Ticket.class;
    Method getterMethod = ticketClass.getMethod(attributeName);
    Date dateField = (Date) getterMethod.invoke(ticket);
    if (dateField != null) {
      logger.info(" ticket ");
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

      System.out.println(notificationDate + "--" + truncatedCurrentDate);

      if (notificationDate.compareTo(truncatedCurrentDate) == 0) {
        System.out.println("hello");
        return true;
      }
    }
    return false;
  }

  @Scheduled(cron = "0 * * * * *")
  public void startEveryMin() {
    List<CustomField> fieldList = fieldRepo.findByDataType("DATE");
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
