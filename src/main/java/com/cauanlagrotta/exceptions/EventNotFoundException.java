package com.cauanlagrotta.exceptions;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(){
        super("Event not found");
    }
}
