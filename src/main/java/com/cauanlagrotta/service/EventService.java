package com.cauanlagrotta.service;

import com.cauanlagrotta.dto.PaginatedResult;
import com.cauanlagrotta.dto.request.EventCreateRequest;
import com.cauanlagrotta.dto.response.EventResponse;
import com.cauanlagrotta.dto.request.EventUpdateRequest;

public interface EventService {
    EventResponse create(EventCreateRequest request);
    EventResponse findById(String eventId);
    PaginatedResult findAll(Integer limit, String pageToken);
    EventResponse update(String eventId, EventUpdateRequest request);
    void delete(String eventId);
 }
