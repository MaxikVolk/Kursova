package edu.vtc.kurs.exceptions;

import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * The type Custom exception.
 */
@Component
@ToString
public class CustomException extends Exception {
    private static final String GENERAL_EXCEPTION_GUID = "ErrorCodeException";
    /**
     * The Code.
     */
    public int code = 400;
    private String errorCode = GENERAL_EXCEPTION_GUID; //Unique string for the exception (used by feign decoder imp)

    private CustomException() {
        super("Error code exception without message");
    }

    /**
     * Instantiates a new Custom exception.
     *
     * @param message the message
     */
    CustomException(String message) {
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

    /**
     * Instantiates a new Custom exception.
     *
     * @param code                          the code
     * @param message                       the message
     * @param globallyUniqueErrorIdentifier the globally unique error identifier
     */
    public CustomException(int code, String message, final String globallyUniqueErrorIdentifier) {
        super(message);
        this.code = code;
        errorCode = globallyUniqueErrorIdentifier;
    }

}
