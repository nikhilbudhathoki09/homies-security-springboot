package homiessecurity.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    String message;

    public ResourceAlreadyExistsException(String message){
        super(message);
        this.message = message;
    }
    
}
