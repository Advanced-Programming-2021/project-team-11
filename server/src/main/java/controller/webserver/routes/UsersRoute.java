package controller.webserver.routes;

import controller.menucontrollers.LoginMenuController;
import controller.menucontrollers.ProfileMenuController;
import controller.menucontrollers.ScoreboardMenuController;
import controller.webserver.TokenManager;
import controller.webserver.Types;
import controller.webserver.Webserver;
import io.javalin.http.Context;
import model.User;
import model.exceptions.*;

public class UsersRoute {
    public static void register(Context context) {
        Types.RegisterRequest body = context.bodyAsClass(Types.RegisterRequest.class);
        try {
            LoginMenuController.register(body.getUsername(), body.getPassword(), body.getPasswordConfirm(), body.getNickname());
        } catch (NicknameExistsException | PasswordsDontMatchException | UsernameExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void login(Context context) {
        Types.LoginBody body = context.bodyAsClass(Types.LoginBody.class);
        User user;
        try {
            user = LoginMenuController.login(body.getUsername(), body.getPassword());
        } catch (InvalidCredentialException e) {
            context.status(401);
            context.json(Types.ErrorMessage.from(e));
            return;
        }
        String token = TokenManager.getInstance().addUser(user);
        Types.LoginResponse response = new Types.LoginResponse();
        response.setToken(token);
        context.json(response);
    }

    public static void updateProfileNickname(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ChangeNickname body = context.bodyAsClass(Types.ChangeNickname.class);
            ProfileMenuController.changeNickname(user, body.getNickname());
        } catch (NicknameExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void updateProfilePassword(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ChangePassword body = context.bodyAsClass(Types.ChangePassword.class);
            ProfileMenuController.changePassword(user, body.getOldPassword(), body.getNewPassword(), body.getNewPasswordConfirm());
        } catch (CurrentPasswordInvalidException | PasswordsDontMatchException | SameNewPasswordException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void updateProfileImage(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TokenManager.TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        user.setProfilePicBytes(context.bodyAsClass(Types.ProfilePicture.class).getPic());
    }

    public static void getProfile(Context context) {
        User user = User.getUserByUsername(context.queryParam("username"));
        if (user == null) {
            context.status(404);
            return;
        }
        Types.ProfileDetails body = new Types.ProfileDetails();
        body.setUser(user);
        context.json(user);
    }
}
