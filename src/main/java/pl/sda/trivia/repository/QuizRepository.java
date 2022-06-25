package pl.sda.trivia.repository;

import pl.sda.trivia.api.Category;
import pl.sda.trivia.api.Difficulty;
import pl.sda.trivia.api.Type;
import pl.sda.trivia.model.Quiz;

import java.util.List;

public interface QuizRepository {

    List<Quiz> findQuizByDifficultyAndTypeAndCategory(int amount, Difficulty difficulty, Type type, Category category);
}
