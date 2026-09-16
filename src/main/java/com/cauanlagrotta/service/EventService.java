package com.cauanlagrotta.service;

import java.util.List;

import com.cauanlagrotta.entity.Event;

public interface EventService {
    Event create(Event event);
    Event findById(Long eventId);
    List<Event> findAll();
    Event update(Event event);
    void delete(Long eventId);
 }
