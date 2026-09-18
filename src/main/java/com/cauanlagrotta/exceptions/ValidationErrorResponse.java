package com.cauanlagrotta.exceptions;

import java.util.List;

public record ValidationErrorResponse(
    String name,
    int code,
    String errorCode,
    List<FieldError> details
) {
    public record FieldError(String field, String message) {}
}
