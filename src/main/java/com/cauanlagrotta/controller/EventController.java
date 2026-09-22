package com.cauanlagrotta.controller;

import org.springframework.web.bind.annotation.*;

import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController 
@RequestMapping("/api/event")
@RequiredArgsConstructor 
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        
        Event createdEvent = this.eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> list = this.eventService.findAll();
        return  ResponseEntity.ok(list);
    }
}