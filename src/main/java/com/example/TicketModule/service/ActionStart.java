package com.example.TicketModule.service;

import com.example.TicketModule.entity.Action;
import com.example.TicketModule.entity.Stage;
import com.example.TicketModule.entity.Ticket;
import com.example.TicketModule.entity.actionConditionType.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.TicketModule.constants.TriggerConstant.*;

@Service
public class ActionStart {

  private static final Logger logger = LoggerFactory.getLogger(ActionStart.class);

  //    @Autowired
  //    private  SimpMessagingTemplate messagingTemplate;

  //    @Autowired
  //    private ResponseWebSocket responseWebSocket;
  //    @Autowired
  //    private StageService stageService;
  @Autowired private StageService stageService;

  public void startAction(List<Action> actionList, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    for (Action action : actionList) {
      String fieldName = action.getActionField().getFieldName();
      if(FIELD_STATUS.equals(fieldName)
              || FIELD_PROIORTY.equals(fieldName)
              || FIELD_ACCOUNTABLEASSINEE.equals(fieldName)
              || FIELD_ASSIGNEE.equals(fieldName)
              || FIELD_CREATEDAT.equals(fieldName)
              || FIELD_ENDDATE.equals(fieldName)
              || FIELD_DESCRIPTION.equals(fieldName)
              || FIELD_ID.equals(fieldName)
              || FIELD_DESCRIPTION.equals(fieldName)
      ||FIELD_STAGEID.equals(fieldName)){
        nonCustomFieldAction(action,ticket);
      }else {
        customFieldAction(action,ticket);
      }
    }
    logger.info("action");
  }

  private void customFieldAction(Action action, Ticket ticket) {
    Map<String, Object> customFields = ticket.getCustomFields();
    if(action == null)return;
    String fieldName = action.getActionField().getFieldName();
    String dataType = action.getActionField().getDataType();
    if(DATA_TYPE_STRING.equals(dataType)){
      StringAction stringAction = (StringAction) action.getActionCondition();
      String newString = stringAction.getNewString();
      customFields.put(fieldName, newString);
    }else if(DATA_TYPE_DATE.equals(dataType)){
      NumberAction numberAction = (NumberAction) action.getActionCondition();
      int newnumber =  numberAction.getNewnumber();
      customFields.put(fieldName,newnumber);
    }else if(DATA_TYPE_NUMBER.equals(dataType)){
      DateAction dateAction = (DateAction) action.getActionCondition();
      Date newDate = dateAction.getNewDate();
      customFields.put(fieldName,newDate);
      ticket.getCustomFields().get(fieldName);
    }
  }

  public void nonCustomFieldAction(Action action, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    switch (action.getActionCondition().getConditionType()) {
      case NUMBER:
        actionOnNumber(action, ticket);
        break;
      case STRING:
        actionOnString(action, ticket);
        break;
      case ID:
        actionOnStage(action, ticket);
        break;
      case DATE:
        actionOnDate(action, ticket);
        break;
      case USER:
        actionOnUser(action, ticket);
      default:
    }
  }

  public void actionOnNumber(Action action, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException {
    logger.info("action on String ");
    Class<?> ticketClass = Ticket.class;
    Method[] methods = ticketClass.getMethods();
    NumberAction numberAction = (NumberAction) action.getActionCondition();
    for (Method method : methods) {
      if (method.getName().startsWith("set")) {
        String attributeName = method.getName().substring(3);
        logger.info(
            attributeName + "--" + capitalizeFirstLetter(action.getActionField().getFieldName()));
        if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getFieldName()))) {

          if (numberAction == null) continue;
          logger.info("set string action started");
          method.invoke(ticket, numberAction.getNewnumber());
        }
      }
    }
  }

  public void actionOnDate(Action action, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException {
    logger.info("action on String ");
    Class<?> ticketClass = Ticket.class;
    Method[] methods = ticketClass.getMethods();
    DateAction dateAction = (DateAction) action.getActionCondition();
    for (Method method : methods) {
      if (method.getName().startsWith("set")) {
        String attributeName = method.getName().substring(3);
        logger.info(
            attributeName + "--" + capitalizeFirstLetter(action.getActionField().getFieldName()));
        if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getFieldName()))) {

          if (dateAction == null) continue;

          logger.info("set date action started");
          method.invoke(ticket, dateAction.getNewDate());
        }
      }
    }
  }

  //    public void actionProject(Action action, Ticket ticket){
  //        ProjectAction projectAction = (ProjectAction) action.getActionCondition();
  //        logger.info("project set startes");
  //        String operation=projectAction.getOperation();
  //        Long currentprojectId=projectAction.getProjectId();
  //        Long stageId= projectAction.getStageId();
  //        Optional<Project> projectOptional = projectRepo.findById(currentprojectId);
  //        if (projectOptional.isPresent()) {
  //            Project project = projectOptional.get();
  //            Optional<Stage> stageOptional = project.getStageList().stream()
  //                    .filter(stage -> stage.getStageId().equals(stageId))
  //                    .findFirst();
  //            if (stageOptional.isPresent()) {
  //                Stage stage = stageOptional.get();
  //                if(operation.equals("set")){
  //                    logger.info("stage Set Successfully");
  //                    logger.info(String.valueOf(stage.getStageId()));
  //                    ticket.setStage(stage);
  //                    ticketRepo.save(ticket);
  //                }
  //            }
  //        }
  //    }

  public void actionOnStage(Action action, Ticket ticket) {
    logger.info("Changing stage to: {}");
    IdAction idAction = (IdAction) action.getActionCondition();
    try {
      System.out.println("runing");
      if(FIELD_STAGEID.equals(action.getActionField().getFieldName())){
        Stage stage = stageService.getStageByStageId(idAction.getNewStageId());
        logger.info("set Stage");
        ticket.setStageId(stage.getId());
      }else if(FIELD_ACCOUNTABLEASSINEE.equals(action.getActionField().getFieldName())){
        ticket.setAccountableAssignee(idAction.getId());
      }else if(FIELD_PROJECTID.equals(action.getActionField().getFieldName())){
        ticket.setProjectId(idAction.getId());
      }
    } catch (Exception e) {
      logger.error("Error occurred while saving ticket: {}", e.getMessage(), e);
    }
  }

  public void actionOnString(Action action, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    logger.info("action on String ");
    Class<?> ticketClass = Ticket.class;
    StringAction stringAction = (StringAction) action.getActionCondition();

    Method[] methods = ticketClass.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("set")) {
        String attributeName = method.getName().substring(3);
        logger.info(
            attributeName + "--" + capitalizeFirstLetter(action.getActionField().getFieldName()));
        if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getFieldName()))) {
            logger.info("set string action started");
            method.invoke(ticket, stringAction.getNewString());

        }
      }
    }
  }

  public void actionOnUser(Action action, Ticket ticket)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    logger.info("action on User ");
    Class<?> ticketClass = Ticket.class;
    Method[] methods = ticketClass.getMethods();
    UserAction userAction = (UserAction) action.getActionCondition();
    for (Method method : methods) {
      if (method.getName().startsWith("set")) {
        String attributeName = method.getName().substring(3);
        logger.info(
            attributeName + "--" + capitalizeFirstLetter(action.getActionField().getFieldName()));
        if (attributeName.equals(capitalizeFirstLetter(action.getActionField().getFieldName()))) {
            logger.info("set string action started");
            method.invoke(ticket, userAction.getUserAction());
        }
      }
    }
  }

  private String capitalizeFirstLetter(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }
}
