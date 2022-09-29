package edu.vtc.kurs.exceptions;

public class Status404StreetNotFoundException extends CustomException{
    public static final int CODE = 404;

    public Status404StreetNotFoundException(String message) {
        super(CODE,message);
    }
    public Status404StreetNotFoundException() {
        super(CODE,"Street not found");
    }
}
