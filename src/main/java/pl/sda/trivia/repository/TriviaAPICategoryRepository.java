package pl.sda.trivia.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pl.sda.trivia.api.Category;
import pl.sda.trivia.api.TriviaCategory;
import pl.sda.trivia.error.DataFormatException;
import pl.sda.trivia.error.DataNotAvailableException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j

public class TriviaAPICategoryRepository implements CategoryRepository {

    private HttpClient client = HttpClient.newHttpClient();
    private TriviaCategory triviaCategory = new TriviaCategory();

    @Override
    public List<Category> findAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://opentdb.com/api_category.php"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info(response.request().toString());
            ObjectMapper mapper = new ObjectMapper();
            TriviaCategory triviaCategory = mapper.readValue(response.body(), TriviaCategory.class);
            return triviaCategory.getTriviaCategories();
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
