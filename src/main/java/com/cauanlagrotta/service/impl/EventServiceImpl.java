package com.cauanlagrotta.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.repository.EventRepository;
import com.cauanlagrotta.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor 
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    private final String UUID_PREFIX = "EVT-";

    @Override
    public Event create(Event event) {

        if(Objects.isNull(event.getId())){
            event.setId(UUID_PREFIX + UUID.randomUUID());
        }

        this.repository.save(event);
        return event;
    }

    @Override
    public List<Event> findAll() {
        return this.repository.findAll();
    }

    @Override
    public void delete(String eventId) {
        findById(eventId);
        this.repository.delete(eventId);
    }


    @Override
    public Event findById(String eventId) {
        Event event = this.repository.findById(eventId);

        if(event == null || event.getId() != eventId) throw new RuntimeException("Event not found");

        return event;
    }

    @Override
    public Event update(Event event) {
        findById(event.getId());
        this.repository.update(event);
        return event;
    }

    
}
