package com.cauanlagrotta.service.impl;

import com.cauanlagrotta.dto.PaginatedResult;
import com.cauanlagrotta.dto.request.EventCreateRequest;
import com.cauanlagrotta.dto.request.EventUpdateRequest;
import com.cauanlagrotta.dto.response.EventResponse;
import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.exceptions.EventNotFoundException;
import com.cauanlagrotta.helper.DynamoTokenHelper;
import com.cauanlagrotta.repository.EventRepository;
import com.cauanlagrotta.service.EventService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Service
@Validated
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final EventRepository repository;
  private final DynamoTokenHelper tokenHelper;
  private static final String UUID_PREFIX = "EVT-";

  @Override
  public EventResponse create(EventCreateRequest request) {

    Event entity = request.toEntity();

    String uuid = UUID_PREFIX + UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    entity.setId(uuid);
    entity.setUpdatedAt(updatedAt);
    entity.setCreatedAt(createdAt);

    this.repository.save(entity);
    return entity.toResponse();
  }

  @Override
  public PaginatedResult findAll(Integer limit, String pageToken) {
    Map<String, AttributeValue> exclusiveStart = null;

    if (pageToken != null) {
      exclusiveStart = this.tokenHelper.decodeToken(pageToken);
    }

    return repository.findAll(limit, exclusiveStart);
  }

    @Override
    public EventResponse update(String eventId, EventUpdateRequest request) {
        findById(eventId);
        Event entity = request.toEntity();
        entity.setUpdatedAt(LocalDateTime.now());
        this.repository.update(entity);
        return entity.toResponse();
    }

    @Override
  public void delete(String eventId) {
    findById(eventId);
    this.repository.delete(eventId);
  }

  @Override
  public EventResponse findById(String eventId) {
    Event event = this.repository.findById(eventId);

    if (event == null || !Objects.equals(event.getId(), eventId))
      throw new EventNotFoundException();

    return event.toResponse();
  }
}
