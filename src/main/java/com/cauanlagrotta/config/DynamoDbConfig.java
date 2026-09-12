package com.cauanlagrotta.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cauanlagrotta.entity.Event;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Configuration 
public class DynamoDbConfig {

    @Bean 
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
            .endpointOverride(URI.create("http://localhost:4566"))
            .region(Region.SA_EAST_1)
                .credentialsProvider(StaticCredentialsProvider
                    .create(AwsBasicCredentials.create("test", "test")))
            .build();
    }

    @Bean 
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }

    @Bean
    public DynamoDbTable<Event> eventTable(DynamoDbEnhancedClient client){

        DynamoDbTable<Event> table = client.table("Events", TableSchema.fromBean(Event.class));

        try{
            table.describeTable();
        }catch(ResourceNotFoundException e){
            table.createTable();
        }
        return table;
    }
}
