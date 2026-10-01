package com.cauanlagrotta.helper;

import org.springframework.stereotype.Component;

import com.cauanlagrotta.exceptions.InvalidPageTokenException;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DynamoTokenHelper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String encodeToken(Map<String, AttributeValue> key) {
        if (key == null || key.isEmpty()) return null;

        try {
            Map<String, Object> json = new LinkedHashMap<>();
            key.forEach((name, value) -> json.put(name, toJson(value)));

            byte[] bytes = objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar pageToken: " + e.getMessage(), e);
        }
    }

    public Map<String, AttributeValue> decodeToken(String token) {
        if (token == null) return null;

        try {
            byte[] bytes = Base64.getDecoder().decode(token);
            Map<String, Object> parsed = objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), MAP_TYPE);

            Map<String, AttributeValue> key = new LinkedHashMap<>();
            parsed.forEach((name, node) -> key.put(name, fromJson(node, name)));
            return key;
        } catch (Exception e) {
            throw new InvalidPageTokenException("pageToken inválido", e);
        }
    }

    private Object toJson(AttributeValue value) {
        if (value.s() != null) return Map.of("S", value.s());
        if (value.n() != null) return Map.of("N", value.n());
        if (value.b() != null) return Map.of("B", Base64.getEncoder().encodeToString(value.b().asByteArray()));
        if (value.bool() != null) return Map.of("BOOL", value.bool());
        if (Boolean.TRUE.equals(value.nul())) return Map.of("NULL", true);
        if (value.l() != null) return Map.of("L", value.l().stream().map(this::toJson).toList());
        if (value.m() != null) {
            Map<String, Object> nested = new LinkedHashMap<>();
            value.m().forEach((name, item) -> nested.put(name, toJson(item)));
            return Map.of("M", nested);
        }
        throw new IllegalStateException("Atributo DynamoDB sem valor para o pageToken");
    }

    private AttributeValue fromJson(Object node, String path) {
        if (!(node instanceof Map<?, ?> map) || map.size() != 1) {
            throw new IllegalArgumentException("pageToken inválido no campo '" + path + "'");
        }

        Map.Entry<?, ?> entry = map.entrySet().iterator().next();
        String type = String.valueOf(entry.getKey());
        Object value = entry.getValue();

        AttributeValue.Builder builder = AttributeValue.builder();
        switch (type) {
            case "S" -> builder.s(String.valueOf(value));
            case "N" -> builder.n(String.valueOf(value));
            case "B" -> builder.b(SdkBytes.fromByteArray(Base64.getDecoder().decode(String.valueOf(value))));
            case "BOOL" -> builder.bool(Boolean.TRUE.equals(value));
            case "NULL" -> builder.nul(true);
            case "L" -> builder.l(toAttributeList(value, path));
            case "M" -> builder.m(toAttributeMap(value, path));
            default -> throw new IllegalArgumentException(
                "pageToken inválido no campo '" + path + "': tipo DynamoDB '" + type + "'");
        }
        return builder.build();
    }

    private List<AttributeValue> toAttributeList(Object value, String path) {
        if (!(value instanceof List<?> items)) {
            throw new IllegalArgumentException("pageToken inválido no campo '" + path + "'");
        }
        return items.stream().map(item -> fromJson(item, path)).toList();
    }

    private Map<String, AttributeValue> toAttributeMap(Object value, String path) {
        if (!(value instanceof Map<?, ?> items)) {
            throw new IllegalArgumentException("pageToken inválido no campo '" + path + "'");
        }

        Map<String, AttributeValue> result = new LinkedHashMap<>();
        items.forEach((name, item) -> result.put(String.valueOf(name), fromJson(item, path)));
        return result;
    }
}
