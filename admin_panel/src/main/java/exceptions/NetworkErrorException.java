package exceptions;

public class NetworkErrorException extends Exception {
    public NetworkErrorException(int status, String message) {
        super("status was " + status + " and the body was " + message);
    }
}
