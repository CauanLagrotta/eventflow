package com.cauanlagrotta.wrapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cauanlagrotta.entity.Event;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component 
public class EventWrapper {
    private final DynamoDbTable<Event> table;

    public EventWrapper(DynamoDbEnhancedClient client) {
        this.table = client.table("Events", TableSchema.fromBean(Event.class));
    }

    public void save(Event event) { table.putItem(event); }
    public Event findById(Long id) { return table.getItem(Key.builder().partitionValue(id).build()); }
    public List<Event> findAll() { return table.scan().items().stream().toList(); }
    public void delete(Long id) { table.deleteItem(Key.builder().partitionValue(id).build()); }
}
