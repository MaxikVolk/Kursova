package edu.vtc.kurs.exceptions;

/**
 * The type Status 403 wrong street exception.
 */
public class Status403WrongStreetException extends CustomException {
    /**
     * Instantiates a new Status 403 wrong street exception.
     */
    public Status403WrongStreetException() {
        super("There isn`t such street in this settlement");
    }
}
