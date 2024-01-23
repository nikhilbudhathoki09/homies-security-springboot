package homiessecurity.exceptions;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomCommonException extends RuntimeException{
    String message;

    public  CustomCommonException(String message){
        super(message);
        this.message = message;
    }
}
