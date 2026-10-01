package com.cauanlagrotta.dto.response;

import com.cauanlagrotta.entity.Event;

import java.time.LocalDateTime;

public record EventResponse(String id,
                            String name,
                            String image,
                            String description,
                            LocalDateTime date,
                            String location,
                            Long amountTickets,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
}
