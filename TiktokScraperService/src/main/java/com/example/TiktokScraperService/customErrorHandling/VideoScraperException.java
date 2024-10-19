package com.example.TiktokScraperService.customErrorHandling;

public class VideoScraperException extends Exception{
    public VideoScraperException(String message) { super(message); }

    public VideoScraperException(String message, Throwable cause) {super(message, cause);}
}
