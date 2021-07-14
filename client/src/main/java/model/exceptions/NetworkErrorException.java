package model.exceptions;

import com.google.gson.Gson;
import controller.webserver.Types;
import kong.unirest.HttpResponse;

public class NetworkErrorException extends Exception {
    public NetworkErrorException(HttpResponse<String> response) {
        this(new Gson().fromJson(response.getBody(), Types.ErrorMessage.class));
    }

    public NetworkErrorException(Types.ErrorMessage message) {
        this(message.getError());
    }

    public NetworkErrorException(String message) {
        super(message);
    }
}
