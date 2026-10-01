package com.cauanlagrotta.helper;

import com.cauanlagrotta.exceptions.InvalidPageTokenException;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DynamoTokenHelperTest {

    private final DynamoTokenHelper helper = new DynamoTokenHelper();

    @Test
    void encodeDecodePreservaValorDaChaveDeParticao() {
        Map<String, AttributeValue> key = Map.of(
            "id", AttributeValue.builder().s("EVT-123").build());

        Map<String, AttributeValue> decoded = helper.decodeToken(helper.encodeToken(key));

        assertThat(decoded).containsOnlyKeys("id");
        assertThat(decoded.get("id").s()).isEqualTo("EVT-123");
    }

    @Test
    void encodeDecodePreservaAtributoNumerico() {
        Map<String, AttributeValue> key = Map.of(
            "amount_tickets", AttributeValue.builder().n("500").build());

        Map<String, AttributeValue> decoded = helper.decodeToken(helper.encodeToken(key));

        assertThat(decoded.get("amount_tickets").n()).isEqualTo("500");
    }

    @Test
    void encodeDeChaveNulaOuVaziaRetornaNull() {
        assertThat(helper.encodeToken(null)).isNull();
        assertThat(helper.encodeToken(Map.of())).isNull();
    }

    @Test
    void encodeGeraTokenEmBase64DoFormatoDynamoDbJson() {
        Map<String, AttributeValue> key = Map.of(
            "id", AttributeValue.builder().s("EVT-123").build());

        String json = new String(Base64.getDecoder().decode(helper.encodeToken(key)),
            StandardCharsets.UTF_8);

        assertThat(json).contains("\"S\"").contains("EVT-123");
    }

    @Test
    void decodeDeChaveSemTipoDynamoLancaInvalidPageToken() {
        String token = Base64.getEncoder().encodeToString("{\"id\":{}}".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> helper.decodeToken(token))
            .isInstanceOf(InvalidPageTokenException.class);
    }

    @Test
    void decodeDeTipoDynamoDesconhecidoLancaInvalidPageToken() {
        String token = Base64.getEncoder().encodeToString("{\"id\":{\"X\":\"EVT-1\"}}".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> helper.decodeToken(token))
            .isInstanceOf(InvalidPageTokenException.class);
    }

    @Test
    void decodeDeBase64InvalidoLancaInvalidPageToken() {
        assertThatThrownBy(() -> helper.decodeToken("nao-e-base64!!"))
            .isInstanceOf(InvalidPageTokenException.class);
    }
}
