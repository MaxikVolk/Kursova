package edu.vtc.kurs.exceptions;

public class Status403WrongStreetException extends CustomException{
    public Status403WrongStreetException(){
        super("There isn`t such street in this settlement");
    }
}
