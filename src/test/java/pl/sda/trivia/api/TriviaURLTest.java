package pl.sda.trivia.api;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TriviaURLTest {

    @Test
    public void shouldReturnURLWithAmountEqualTen() {
        // when
        URI uri = TriviaURL.builder()
                .amount(10)
                .build()
                .getURL();
        // then
        assertEquals("amount=10", uri.getQuery());
    }

    @Test
    public void shouldReturnURLWithDifficultyAndAmount() {
        // given
        URI uri = TriviaURL.builder()
                .amount(10)
                .difficulty(Difficulty.EASY)
                .encode(Encoding.BASE64)
                .type(Type.MULTIPLE_CHOICE)
                .build().getURL();
        // when
        Set<String> parameters = Set.of(uri.getQuery().split("&"));
        // then
        assertTrue(parameters.contains("amount=10"));
        assertTrue(parameters.contains("difficulty=easy"));
        assertTrue(parameters.contains("type=multiple"));
        assertTrue(parameters.contains("encode=base64"));
        assertEquals(4,parameters.size());
    }

}