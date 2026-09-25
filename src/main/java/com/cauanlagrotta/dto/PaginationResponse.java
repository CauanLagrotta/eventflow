package com.cauanlagrotta.dto;


public record PaginationResponse(String pageToken,
                                 Integer pageSize,
                                 Boolean hasNext) {
}
