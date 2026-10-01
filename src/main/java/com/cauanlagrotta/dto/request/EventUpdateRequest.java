package com.cauanlagrotta.dto.request;

import com.cauanlagrotta.entity.Event;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateRequest {

    @NotNull(message = "Name is mandatory")
    private String name;

    private String image;

    @NotNull(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Date is mandatory")
    private LocalDateTime date;

    @NotNull(message = "Location is mandatory")
    private String location;

    @NotNull(message = "Amount tickets is mandatory")
    @Min(value = 0, message = "Amount tickets cannot be less than 0")
    private Long amountTickets;

    public Event toEntity(){
        return Event.builder()
            .name(name)
            .image(image)
            .description(description)
            .date(date)
            .location(location)
            .amountTickets(amountTickets)
            .build();
    }
}
