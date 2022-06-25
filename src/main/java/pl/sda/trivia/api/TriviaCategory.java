package pl.sda.trivia.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TriviaCategory {

    @JsonProperty("trivia_categories")
    private List<Category> triviaCategories;

}
