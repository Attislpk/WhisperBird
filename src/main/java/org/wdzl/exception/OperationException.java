package org.wdzl.exception;

public class OperationException extends RuntimeException{
    public OperationException(String errMsg){
        super(errMsg);
    }
}
