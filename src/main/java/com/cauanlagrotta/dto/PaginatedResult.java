package com.cauanlagrotta.dto;

import com.cauanlagrotta.entity.Event;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

public record PaginatedResult(List<Event> items, Map<String, AttributeValue> lastKey) {}
