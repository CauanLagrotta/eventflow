package com.cauanlagrotta.repository;

import java.util.List;
import java.util.Map;

import com.cauanlagrotta.dto.PaginatedResult;
import org.springframework.stereotype.Component;

import com.cauanlagrotta.entity.Event;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.IgnoreNullsMode;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component 
public class EventRepository {
    private final DynamoDbTable<Event> table;

    public EventRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("Events", TableSchema.fromBean(Event.class));
    }

    public void save(Event event) { table.putItem(event); }
    public Event findById(String eventId) { return table.getItem(Key.builder().partitionValue(eventId).build()); }
    public void update(Event event) { table.updateItem(builder -> builder.item(event).ignoreNullsMode(IgnoreNullsMode.SCALAR_ONLY) ); }
    public void delete(String eventId) { table.deleteItem(Key.builder().partitionValue(eventId).build()); }

    public PaginatedResult findAll(Integer limit, Map<String, AttributeValue> exclusiveStartKey){

        ScanEnhancedRequest.Builder requestBuilder = ScanEnhancedRequest.builder().limit(limit);

        if(exclusiveStartKey != null){
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        Page<Event> page = table.scan(requestBuilder.build()).stream().findFirst().orElse(null);

        if(page == null) return new PaginatedResult(List.of(), null);

    return new PaginatedResult(page.items(), page.lastEvaluatedKey());
    }
}
