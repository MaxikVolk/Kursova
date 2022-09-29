package edu.vtc.kurs.util;

public class SerpApiSearchException extends Exception {
  public SerpApiSearchException(Exception exception) {
    super(exception);
  }
  public SerpApiSearchException(String message) {
    super(message);
  }
}
