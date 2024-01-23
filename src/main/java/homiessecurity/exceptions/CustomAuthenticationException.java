package homiessecurity.exceptions;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomAuthenticationException extends RuntimeException{

    String message;

    public CustomAuthenticationException(String message){
        super(message);
        this.message = message;
    }


}
