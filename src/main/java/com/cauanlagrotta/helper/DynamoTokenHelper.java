package com.cauanlagrotta.helper;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

@Component
public class DynamoTokenHelper {

    private final ObjectMapper objectMapper =  new ObjectMapper();

    public String encodeToken(Map<String, AttributeValue> key) {
        if(key == null) return null;

        try{
            String json = objectMapper.writeValueAsString(key);
            return Base64.getEncoder().encodeToString(json.getBytes());
        }catch (Exception e){
            throw new RuntimeException("Error to decode pageToken: " + e);
        }
    }

    public Map<String, AttributeValue> decodeToken(String token){
        if(token == null) return null;

        try{
            byte[] bytes = Base64.getDecoder().decode(token);
            String json = new String(bytes);
            return objectMapper.readValue(json, new TypeReference<Map<String, AttributeValue>>() {});
        }catch (Exception e){
            throw new RuntimeException("Error to decode pageToken: " + e);
        }
    }
}
