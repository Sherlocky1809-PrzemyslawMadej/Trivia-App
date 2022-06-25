package pl.sda.trivia.api;

import lombok.*;
import pl.sda.lib.query.RequestQuery;

import java.net.URI;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class TriviaURL {

    private static final String TRIVIA_HOST = "https://opentdb.com/api.php";

    private int amount;

    private Category category;

    private Type type;

    private Difficulty difficulty;

    private Encoding encode;


    public URI getURL() {
        RequestQuery.Builder builder = RequestQuery.builder()
                .addParameter("amount", amount);
        if(category != null) {
            builder.addParameter("category", category.getId());
        }
        if(type != null && type != Type.ANY) {
            builder.addParameter("type", type);
        }
        if(difficulty != null && difficulty != Difficulty.ANY) {
            builder.addParameter("difficulty", difficulty);
        }
        if(encode != null && encode != Encoding.DEFAULT) {
            builder.addParameter("encode", encode);
        }
        return URI.create(TRIVIA_HOST + "?" + builder.query());
    }
}
