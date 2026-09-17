package com.cauanlagrotta.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cauanlagrotta.entity.Event;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component 
public class EventRepository {
    private final DynamoDbTable<Event> table;

    public EventRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("Events", TableSchema.fromBean(Event.class));
    }

    public void save(Event event) { table.putItem(event); }
    public Event findById(String eventId) { return table.getItem(Key.builder().partitionValue(eventId).build()); }
    public List<Event> findAll() { return table.scan().items().stream().toList(); }
    public void update(Event event) { table.updateItem(event); }
    public void delete(String eventId) { table.deleteItem(Key.builder().partitionValue(eventId).build()); }
}
