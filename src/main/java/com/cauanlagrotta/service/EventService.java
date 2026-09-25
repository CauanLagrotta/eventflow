package com.cauanlagrotta.service;

import java.util.List;

import com.cauanlagrotta.dto.PaginatedResult;
import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.repository.EventRepository;

public interface EventService {
    Event create(Event event);
    Event findById(String eventId);
    PaginatedResult findAll(Integer limit, String pageToken);
    Event update(Event event);
    void delete(String eventId);
 }
