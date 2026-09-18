package com.cauanlagrotta.exceptions;

public record ErrorResponse(
    String name,
    int code,
    String errorCode,
    String detail
) {}
