package pl.sda.trivia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class QuizToComplete {

    private String question;
    private List<String> options;
    private String answer;
    private String correctAnswer;

}
