package com.group5.lingual.data.hsqldb;

//Wraps a regular SQLException to make checking optional
public class HSQLDBException extends RuntimeException
{
    public HSQLDBException(Exception cause) { super(cause); }
}