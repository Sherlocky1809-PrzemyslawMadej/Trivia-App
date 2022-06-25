package pl.sda.trivia.repository;

import pl.sda.trivia.api.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

}
