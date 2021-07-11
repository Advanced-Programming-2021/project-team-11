package controller.webserver.routes;

import controller.menucontrollers.LoginMenuController;
import controller.menucontrollers.ProfileMenuController;
import controller.menucontrollers.ScoreboardMenuController;
import controller.webserver.TokenManager;
import controller.webserver.Types;
import io.javalin.http.Context;
import model.User;
import model.exceptions.*;

import java.util.concurrent.atomic.AtomicInteger;

public class UsersRoute {
    private final static String TOKEN_HEADER = "token";
    private final static AtomicInteger onlineUsers = new AtomicInteger(0);

    public static void register(Context context) {
        Types.RegisterBody body = context.bodyAsClass(Types.RegisterBody.class);
        try {
            LoginMenuController.register(body.getUsername(), body.getPassword(), body.getPasswordConfirm(), body.getNickname());
        } catch (NicknameExistsException | PasswordsDontMatchException | UsernameExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void scoreboard(Context context) {
        context.json(ScoreboardMenuController.getScoreboardRows());
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
        context.result(token);
    }

    public static void updateProfileNickname(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ChangeNicknameBody body = context.bodyAsClass(Types.ChangeNicknameBody.class);
            ProfileMenuController.changeNickname(user, body.getNickname());
        } catch (NicknameExistsException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void updateProfilePassword(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        try {
            Types.ChangePasswordBody body = context.bodyAsClass(Types.ChangePasswordBody.class);
            ProfileMenuController.changePassword(user, body.getOldPassword(), body.getNewPassword(), body.getNewPasswordConfirm());
        } catch (CurrentPasswordInvalidException | PasswordsDontMatchException | SameNewPasswordException e) {
            context.status(400);
            context.json(Types.ErrorMessage.from(e));
        }
    }

    public static void updateProfileImage(Context context) {
        User user = TokenManager.getInstance().getUser(context.header(TOKEN_HEADER));
        if (user == null) {
            context.status(401);
            return;
        }
        user.setProfilePicBytes(context.bodyAsClass(Types.ProfilePictureBody.class).getPic());
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

    public static void activeUsers(Context context) {
        context.result("" + onlineUsers.get());
    }

    public static void addOnlineUsers() {
        onlineUsers.incrementAndGet();
    }

    public static void decreaseOnlineUsers() {
        onlineUsers.decrementAndGet();
    }
}
