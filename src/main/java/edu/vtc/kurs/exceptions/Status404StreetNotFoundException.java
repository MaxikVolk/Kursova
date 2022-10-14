package edu.vtc.kurs.exceptions;

/**
 * The type Status 404 street not found exception.
 */
public class Status404StreetNotFoundException extends CustomException {
    /**
     * The constant CODE.
     */
    public static final int CODE = 404;


    /**
     * Instantiates a new Status 404 street not found exception.
     */
    public Status404StreetNotFoundException() {
        super(CODE, "Street not found");
    }
}
