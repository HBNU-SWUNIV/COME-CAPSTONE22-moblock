package com.capstone.hyperledgerfabrictransferserver.aop.customException;

public class AlreadyExistsCoinException extends RuntimeException{
    public AlreadyExistsCoinException(String message) {
        super(message);
    }
}
