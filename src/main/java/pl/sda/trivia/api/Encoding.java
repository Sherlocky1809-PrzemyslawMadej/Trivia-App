package pl.sda.trivia.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Encoding {

    @JsonProperty("url3986")
    URL3986,

    @JsonProperty("base64")
    BASE64,

    DEFAULT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
