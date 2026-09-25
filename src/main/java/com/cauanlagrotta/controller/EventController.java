package com.cauanlagrotta.controller;

import com.cauanlagrotta.dto.ListResponse;
import com.cauanlagrotta.dto.PaginatedResult;
import com.cauanlagrotta.dto.PaginationResponse;
import com.cauanlagrotta.helper.DynamoTokenHelper;
import org.springframework.web.bind.annotation.*;

import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


@RestController 
@RequestMapping("/api/event")
@RequiredArgsConstructor 
public class EventController {

    private final EventService eventService;
    private final DynamoTokenHelper tokenHelper;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        
        Event createdEvent = this.eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping
    public ResponseEntity<ListResponse<Event>> getAllEvents(@RequestParam(defaultValue = "10") Integer limit,
                                                            @RequestParam(required = false) String pageToken) {

        PaginatedResult result = this.eventService.findAll(limit, pageToken);
        String nextPageToken = this.tokenHelper.encodeToken(result.lastKey());

        return ResponseEntity.ok(new ListResponse<>(
            Map.of(),
            result.items(),
            new PaginationResponse(nextPageToken, limit, nextPageToken != null)
        ));
    }
}