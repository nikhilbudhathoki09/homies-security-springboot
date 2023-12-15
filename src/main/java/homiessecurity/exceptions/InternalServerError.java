package homiessecurity.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InternalServerError extends RuntimeException{

    String message;
    HttpStatus status;
    LocalDateTime timeStamp;

    public InternalServerError(String message){
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.timeStamp = LocalDateTime.now();
    }

    public InternalServerError(){
        super("Internal Server Error(Something went wrong)");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.timeStamp = LocalDateTime.now();
    }



}
