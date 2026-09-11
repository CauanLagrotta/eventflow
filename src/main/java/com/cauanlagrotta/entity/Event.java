package com.cauanlagrotta.entity;

import java.time.LocalDateTime;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Event {

    private Long id;

    private String name;

    private String image;

    private String description;

    private LocalDateTime date;

    private String location;

    private Long amountTickets;

    @DynamoDbPartitionKey 
    @DynamoDbAttribute("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    @DynamoDbAttribute("image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @DynamoDbAttribute("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDbAttribute("date")
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @DynamoDbAttribute("location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDbAttribute("amount_tickets")
    public Long getAmountTickets() {
        return amountTickets;
    }

    public void setAmountTickets(Long amountTickets) {
        this.amountTickets = amountTickets;
    }

    
}
