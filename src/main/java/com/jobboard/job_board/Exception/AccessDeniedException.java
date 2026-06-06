package com.jobboard.job_board.Exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException (String message){
        super(message);
    }
}
