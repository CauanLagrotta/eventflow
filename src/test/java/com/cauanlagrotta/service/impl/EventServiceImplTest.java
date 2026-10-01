package com.cauanlagrotta.service.impl;

import com.cauanlagrotta.dto.request.EventCreateRequest;
import com.cauanlagrotta.dto.request.EventUpdateRequest;
import com.cauanlagrotta.dto.response.EventResponse;
import com.cauanlagrotta.entity.Event;
import com.cauanlagrotta.exceptions.EventNotFoundException;
import com.cauanlagrotta.helper.DynamoTokenHelper;
import com.cauanlagrotta.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    private static final String EVENT_ID = "EVT-123";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);

    @Mock
    private EventRepository repository;

    @Mock
    private DynamoTokenHelper tokenHelper;

    @InjectMocks
    private EventServiceImpl service;

    @Test
    void updateDevePersistirComIdDoPath() {
        when(repository.findById(EVENT_ID)).thenReturn(existingEvent());

        EventResponse response = service.update(EVENT_ID, updateRequest());

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(repository).update(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(EVENT_ID);
        assertThat(captor.getValue().getName()).isEqualTo("Show de Rock - Atualizado");
        assertThat(captor.getValue().getAmountTickets()).isEqualTo(750L);
        assertThat(captor.getValue().getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
        assertThat(response.id()).isEqualTo(EVENT_ID);
        assertThat(response.name()).isEqualTo("Show de Rock - Atualizado");
    }

    @Test
    void updateDeEventoInexistenteLancaNotFoundSemPersistir() {
        when(repository.findById("EVT-x")).thenReturn(null);

        assertThatThrownBy(() -> service.update("EVT-x", updateRequest()))
            .isInstanceOf(EventNotFoundException.class);

        verify(repository, never()).update(any());
    }

    @Test
    void createGeraIdComPrefixoEvtESetDatas() {
        EventResponse response = service.create(createRequest());

        ArgumentCaptor<Event> captor = ArgumentCaptor.forClass(Event.class);
        verify(repository).save(captor.capture());

        assertThat(response.id()).startsWith("EVT-").isEqualTo(captor.getValue().getId());
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getUpdatedAt()).isNotNull();
    }

    private Event existingEvent() {
        return Event.builder()
            .id(EVENT_ID)
            .name("Show de Rock")
            .description("Show ao vivo")
            .date(LocalDateTime.of(2024, 12, 20, 19, 30))
            .location("São Paulo")
            .amountTickets(500L)
            .createdAt(CREATED_AT)
            .build();
    }

    private EventUpdateRequest updateRequest() {
        EventUpdateRequest request = new EventUpdateRequest();
        request.setName("Show de Rock - Atualizado");
        request.setDescription("Show ao vivo - atualizado");
        request.setDate(LocalDateTime.of(2024, 12, 20, 19, 30));
        request.setLocation("São Paulo");
        request.setAmountTickets(750L);
        return request;
    }

    private EventCreateRequest createRequest() {
        EventCreateRequest request = new EventCreateRequest();
        request.setName("Show de Rock");
        request.setDescription("Show ao vivo");
        request.setDate(LocalDateTime.of(2024, 12, 20, 19, 30));
        request.setLocation("São Paulo");
        request.setAmountTickets(500L);
        return request;
    }
}
