package com.sal.flooringmastery.service;

public class FlooringMasteryStateNotFoundException extends Exception{

    public FlooringMasteryStateNotFoundException(String message) {
        super(message);
    }

    public FlooringMasteryStateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
