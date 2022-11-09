package com.sal.flooringmastery.dao;

public interface AuditDao {

    public void writeAuditEntry(String entry) throws FlooringMasteryException;

}
