package edu.vtc.kurs.exceptions;

import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * The type Custom exception.
 */
@Component
@ToString
public class CustomException extends Exception {
    public int code = 400;
    private CustomException() {
    }

    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Custom exception.
     *
     * @param code    the code
     * @param message the message
     */
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }
}
