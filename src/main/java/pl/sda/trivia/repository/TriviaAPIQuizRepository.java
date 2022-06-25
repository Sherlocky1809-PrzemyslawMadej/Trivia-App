package pl.sda.trivia.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import pl.sda.trivia.api.Category;
import pl.sda.trivia.api.Difficulty;
import pl.sda.trivia.api.TriviaURL;
import pl.sda.trivia.api.Type;
import pl.sda.trivia.error.DataFormatException;
import pl.sda.trivia.error.DataNotAvailableException;
import pl.sda.trivia.model.Quiz;
import pl.sda.trivia.model.TriviaResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

public class TriviaAPIQuizRepository implements QuizRepository {

    private HttpClient client = HttpClient.newHttpClient();

    @Override
    public List<Quiz> findQuizByDifficultyAndTypeAndCategory(int amount, Difficulty difficulty,
                                                             Type type, Category category) {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(TriviaURL.builder()
                        .amount(amount)
                        .difficulty(difficulty)
                        .type(type)
                        .category(category)
                        .build()
                        .getURL())
                .build();

        return getQuizzes(request);
    }

    private List<Quiz> getQuizzes(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info(response.request().toString());
//        Deserializacja z JSON na obiekt klasy
            ObjectMapper mapper = new ObjectMapper();
            TriviaResponse triviaResponse = mapper.readValue(response.body(), TriviaResponse.class);
            triviaResponse.setResults(triviaResponse.getResults().stream()
                    .map(quiz -> Quiz.builder()
                            .difficulty(quiz.getDifficulty())
                            .type(quiz.getType())
                            .category(quiz.getCategory())
                            .correctAnswer(Jsoup.parse(quiz.getCorrectAnswer()).text())
                            .question(Jsoup.parse(quiz.getQuestion()).text())
                            .incorrectAnswers(
                                    quiz.getIncorrectAnswers()
                                            .stream()
                                            .map(answer -> Jsoup.parse(answer).text())
                                            .collect(Collectors.toList()))
                            .build()
                    ).collect(Collectors.toList())
            ); // Jsoup - klasa która umożliwia wyświetlić w pełni zapytanie w html wstawia własciwy znak (escapowanie)
            return triviaResponse.getResults();
        } catch (JsonMappingException ex) {
            log.error(ex.getMessage());
            throw new DataFormatException("Oczekiwano innego formatu danych!");
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
            throw new DataFormatException("Oczekiwano innego formatu danych!");
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new DataNotAvailableException("Brak dostępu do danych!");
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
