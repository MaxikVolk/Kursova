package edu.vtc.kurs.exceptions;

/**
 * The type Status 403 wrong street exception.
 */
public class Status410WrongStreetException extends CustomException {
    public static final int CODE = 410;
    /**
     * Instantiates a new Status 403 wrong street exception.
     */
    public Status410WrongStreetException() {
        super(CODE,"There isn`t such street in this settlement");
    }
}
