//package com.example.TicketModule.Controller;
//
//import com.example.TicketModule.DTO.ReestimatedTicketDTO;
//import com.example.TicketModule.Entity.ReestimatedTicket;
//import com.example.TicketModule.Entity.Ticket;
//import com.example.TicketModule.Exception.ReestimationCreationException;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/tickets")
//public class ReestimatedTicketController {
//    private final Logger logger = LoggerFactory.getLogger(ReestimatedTicketController.class);
//
////    @Autowired
////    private ReestimatedTicketService reestimatedTicketService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//
//    @PostMapping("/{id}/re-estimation")
//    public ResponseEntity<Map<String, Object>> createReestimatedTicket(@PathVariable Long id, @RequestBody ReestimatedTicket reestimatedTicket) {
//        logger.info("Inside create reestimation ticket");
//        Map<String, Object> response = new HashMap<>();
//        HttpStatus status = null;
//
//        try {
//            logger.info("Creating Reestimated Ticket");
//            ReestimatedTicket createdReestimatedTicket = reestimatedTicketService.createReestimatedTicket(id, reestimatedTicket);
//            response.put("message", "Reestimated ticket created successfully");
//            ReestimatedTicketDTO ticket = new ReestimatedTicketDTO(createdReestimatedTicket);
//            response.put("data", ticket);
//            status = HttpStatus.CREATED;
//
//        } catch (ReestimationCreationException e) {
//            logger.error("ReestimationCreationException: " + e.getMessage(), e);
//            response.put("message", e.getMessage());
//            status = HttpStatus.BAD_REQUEST;
//        } catch (Exception e) {
//            logger.error("An error occurred while creating a reestimated ticket", e);
//            response.put("message", "An error occurred while creating a reestimated ticket");
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//
//        return new ResponseEntity<>(response, status);
//    }
//
//    @GetMapping("/{id}/re-estimation")
//    public ResponseEntity<List<ReestimatedTicketDTO>> getReestimatedTicketByUserId(@PathVariable Long id) {
//        logger.info("Inside getReestimatedTicketByUserId");
//        Map<String, Object> response = new HashMap<>();
//        HttpStatus status = null;
//
//        try {
//            logger.info("Fetching Reestimated Tickets by User ID");
//            List<ReestimatedTicketDTO> reestimatedTicketDTO = reestimatedTicketService.getReestimatedTicketByUserId(id);
//
//            return new ResponseEntity<>(reestimatedTicketDTO, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//
//    }
////
////    @GetMapping("/{id}/reestimation-history")
////    public ResponseEntity<Map<String, Object>> getReestimatedAllTicketByUserId(@PathVariable Long id) {
////        logger.info("Inside getReestimatedAllTicketByUserId");
////        Map<String, Object> response = new HashMap<>();
////        HttpStatus status = null;
////
////        try {
////            logger.info("Fetching Reestimated Tickets by User ID");
////            Optional<List<ReestimatedTicket>> reestimatedTickets = reestimatedTicketService.getReestimatedAllTicketByUserId(id);
////
////            if (reestimatedTickets.isPresent()) {
////                response.put("message", "Success");
////                response.put("data", reestimatedTickets.get());
////                status = HttpStatus.OK;
////            } else {
////                response.put("message", "Reestimated Tickets not found for ticket ID: " + id);
////                response.put("data", null);
////                status = HttpStatus.NOT_FOUND;
////            }
////        } catch (Exception e) {
////            logger.error("An error occurred", e);
////            response.put("message", "An error occurred");
////            response.put("data", null);
////            status = HttpStatus.INTERNAL_SERVER_ERROR;
////        }
////        return new ResponseEntity<>(response, status);
////    }
////
////    @PutMapping("/{id}/re-estimation")
////    public ResponseEntity<Map<String, Object>> updateReestimatedTicket(@PathVariable Long id, @RequestBody ReestimatedTicket reestimatedTicket) {
////        logger.info("Inside updateReestimatedTicket");
////        Map<String, Object> response = new HashMap<>();
////        HttpStatus status = null;
////
////        try {
////            logger.info("Updating Reestimated Ticket");
////            ReestimatedTicket updatedReestimatedTicket = reestimatedTicketService.updateReestimatedTicket(id, reestimatedTicket);
////
////            if (updatedReestimatedTicket != null) {
////                response.put("message", "Ticket updated successfully");
////                response.put("data", updatedReestimatedTicket);
////                status = HttpStatus.OK;
////            } else {
////                response.put("message", "Reestimated Ticket not found with id: " + id);
////                response.put("data", null);
////                status = HttpStatus.NOT_FOUND;
////            }
////        } catch (Exception e) {
////            logger.error("An error occurred", e);
////            response.put("message", "An error occurred");
////            response.put("data", null);
////            status = HttpStatus.INTERNAL_SERVER_ERROR;
////        }
////
////        return new ResponseEntity<>(response, status);
////    }
//}
