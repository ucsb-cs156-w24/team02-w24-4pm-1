package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.HelpRequest;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.HelpRequestRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.time.LocalDateTime;

@Tag(name = "HelpRequest")
@RequestMapping("/api/helprequests")
@RestController
@Slf4j
public class HelpRequestController extends ApiController{
    @Autowired
    HelpRequestRepository helpRequestRepository;

    @Operation(summary= "List all helprequests")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<HelpRequest> allHelpRequest() {
        Iterable<HelpRequest> helprequests = helpRequestRepository.findAll();
        return helprequests;
    }

    @Operation(summary= "Create a new help request")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public HelpRequest postHelpRequest(
            @Parameter(name="requesterEmail") @RequestParam String requesterEmail,
            @Parameter(name = "teamId") @RequestParam String teamId,
            @Parameter(name = "tableOrBreakoutRoom") @RequestParam String tableOrBreakoutRoom,
            @Parameter(name = "requestTime", description = "in iso format, e.g. YYYY-mm-ddTHH:MM:SSorg/wiki/ISO_8601") @RequestParam("requestTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestTime,
            @Parameter(name = "explanation") @RequestParam String explanation,
            @Parameter(name = "solved") @RequestParam boolean solved)
            throws JsonProcessingException {

        // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        // See: https://www.baeldung.com/spring-date-parameters
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setRequesterEmail(requesterEmail);
        helpRequest.setTeamId(teamId);
        helpRequest.setTableOrBreakoutRoom(tableOrBreakoutRoom);
        helpRequest.setRequestTime(requestTime);
        helpRequest.setExplanation(explanation);
        helpRequest.setSolved(solved);

        HelpRequest savedHelpRequest = helpRequestRepository.save(helpRequest);

        return savedHelpRequest;
    }
}