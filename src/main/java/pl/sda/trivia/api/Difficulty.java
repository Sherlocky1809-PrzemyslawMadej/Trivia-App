package pl.sda.trivia.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Difficulty {

    @JsonProperty("easy")
    EASY,

    @JsonProperty("medium")
    MEDIUM,

    @JsonProperty("hard")
    HARD,

    ANY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
