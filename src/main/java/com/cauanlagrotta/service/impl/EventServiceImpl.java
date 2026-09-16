package com.cauanlagrotta.service.impl;

import java.util.List;

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

    @Override
    public Event create(@Valid Event event) {
        this.repository.save(event);
        return event;
    }

    @Override
    public List<Event> findAll() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void delete(Long eventId) {
        
        
    }


    @Override
    public Event findById(Long eventId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Event update(Event event) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
