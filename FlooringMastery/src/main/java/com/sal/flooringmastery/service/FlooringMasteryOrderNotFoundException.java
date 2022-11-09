package com.sal.flooringmastery.service;

public class FlooringMasteryOrderNotFoundException extends Exception{

    public FlooringMasteryOrderNotFoundException(String message) {
        super(message);
    }

    public FlooringMasteryOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
