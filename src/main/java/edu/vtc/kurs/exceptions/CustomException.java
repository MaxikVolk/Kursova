package edu.vtc.kurs.exceptions;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class CustomException extends Exception{
    private static final String GENERAL_EXCEPTION_GUID = "ErrorCodeException";
    public int code = 400;
    private String errorCode = GENERAL_EXCEPTION_GUID; //Unique string for the exception (used by feign decoder imp)

    private CustomException() {
        super("Error code exception without message");
    }

    CustomException(String message) {
        super(message);
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(int code, String message, final String globallyUniqueErrorIdentifier) {
        super(message);
        this.code = code;
        errorCode = globallyUniqueErrorIdentifier;
    }

}
