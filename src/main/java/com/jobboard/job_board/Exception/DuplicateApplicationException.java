package com.jobboard.job_board.Exception;

public class DuplicateApplicationException extends RuntimeException{
    public DuplicateApplicationException (String message){
        super(message);
    }
}
