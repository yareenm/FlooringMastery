package com.sal.flooringmastery.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class AuditDaoImpl implements AuditDao{
    @Autowired
    public static final String AUDIT_FILE = "audit.txt";

    // This function writes an entry to the file called audit.txt
    // This written file help us to track our operations on the vending machine
    @Override
    public void writeAuditEntry(String entry) throws FlooringMasteryException{
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new FlooringMasteryException("Could not persist audit information.", e);
        }

        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }
}
