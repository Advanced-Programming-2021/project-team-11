package model.exceptions;

public class ConfigLoadingException extends RuntimeException {
    public ConfigLoadingException(Throwable throwable) {
        super(throwable);
    }
}
