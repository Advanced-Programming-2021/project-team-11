package controller.menucontrollers;

import com.google.gson.Gson;
import controller.webserver.Types;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import model.User;
import model.exceptions.*;

public class LoginMenuController {
    public static final String TOKEN_HEADER = "token";
    private static String token;

    public static User login(String username, String password) throws InvalidCredentialException {
        Types.LoginBody body = new Types.LoginBody();
        body.setUsername(username);
        body.setPassword(password);
        HttpResponse<Types.LoginResponse> response = Unirest.post("/users/login")
                .header("Content-Type", "application/json")
                .body(body)
                .asObject(Types.LoginResponse.class);
        if (response.getStatus() != 200)
            throw new InvalidCredentialException();
        token = response.getBody().getToken();
        return getUserByUsername(username);
    }

    public static void register(String username, String password, String passwordConfirm, String nickname) throws NetworkErrorException {
        Types.RegisterRequest body = new Types.RegisterRequest();
        body.setUsername(username);
        body.setPassword(password);
        body.setPasswordConfirm(passwordConfirm);
        body.setNickname(nickname);
        HttpResponse<String> response = Unirest.post("/users/register")
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(new Gson().fromJson(response.getBody(), Types.ErrorMessage.class));
    }

    public static User getUserByUsername(String username) {
        HttpResponse<Types.ProfileDetails> response = Unirest.get("/users/profile")
                .queryString("username", username)
                .asObject(Types.ProfileDetails.class);
        if (response.getStatus() != 200)
            return null;
        User user = new User(response.getBody().getUsername(), "", response.getBody().getNickname());
        user.setMoney(response.getBody().getMoney());
        user.increaseScore(response.getBody().getScore());
        user.setProfilePicBytes(response.getBody().getPic());
        return user;
    }

    /**
     * The token of the logged in user
     */
    public static String getToken() {
        return token;
    }
}
