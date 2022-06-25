package pl.sda.trivia.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {

    @JsonProperty("multiple")
    MULTIPLE_CHOICE("multiple"),

    @JsonProperty("boolean")
    TRUE_FALSE("boolean"),

    ANY("");

    private final String queryValue;

    Type(String queryValue) {
        this.queryValue = queryValue;
    }

    @Override
    public String toString() {
        return this.queryValue;
    }
}
