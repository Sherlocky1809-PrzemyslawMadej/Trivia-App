package pl.sda.trivia;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import pl.sda.trivia.api.Difficulty;
import pl.sda.trivia.api.TriviaURL;
import pl.sda.trivia.model.Quiz;
import pl.sda.trivia.model.TriviaResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class TriviaAppSimple {

    public static void main(String[] args) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(TriviaURL.builder()
                        .amount(5)
                        .difficulty(Difficulty.ANY)
                        .build()
                        .getURL())
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

//        Deserializacja z JSON na obiekt klasy
        ObjectMapper mapper = new ObjectMapper();
        TriviaResponse triviaResponse = mapper.readValue(response.body(), TriviaResponse.class);
        Quiz quiz = triviaResponse.getResults().get(0);
        System.out.println(Jsoup.parse(quiz.getQuestion()).text());


        List<String> options = new ArrayList<>(quiz.getIncorrectAnswers());
        options.add(quiz.getCorrectAnswer());
        Collections.shuffle(options);

        for (String option: options) {
            System.out.println(option);
        }
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        if(quiz.getCorrectAnswer().equals(answer)) {
            System.out.println("Brawo! Poprawna odpowiedź!");
        } else {
            System.out.println("Pudło!");
        }
    }
}
