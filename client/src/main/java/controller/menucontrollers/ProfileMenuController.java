package controller.menucontrollers;

import controller.webserver.Types;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import model.User;
import model.exceptions.*;

public class ProfileMenuController {
    public static void changeNickname(String newNickname) throws NicknameExistsException {
        Types.ChangeNickname nickname = new Types.ChangeNickname();
        nickname.setNickname(newNickname);
        HttpResponse<String> response = Unirest.post("/users/profile/nickname")
                .header(LoginMenuController.TOKEN_HEADER, LoginMenuController.getToken())
                .header("Content-Type", "application/json")
                .body(nickname)
                .asString();
        if (response.getStatus() != 200)
            throw new NicknameExistsException(newNickname);
    }

    public static void changePassword(String oldPassword, String newPassword, String newPasswordConfirm) throws NetworkErrorException {
        Types.ChangePassword body = new Types.ChangePassword();
        body.setOldPassword(oldPassword);
        body.setNewPassword(newPassword);
        body.setNewPassword(newPasswordConfirm);
        HttpResponse<String> response = Unirest.post("/users/profile/password")
                .header(LoginMenuController.TOKEN_HEADER, LoginMenuController.getToken())
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        if (response.getStatus() != 200)
            throw new NetworkErrorException(response);
    }

    public static void setProfilePic(byte[] bytes) {
        Types.ProfilePicture profilePicture = new Types.ProfilePicture();
        profilePicture.setPic(bytes);
        Unirest.post("/users/profile/image")
                .header(LoginMenuController.TOKEN_HEADER, LoginMenuController.getToken())
                .header("Content-Type", "application/json")
                .body(profilePicture)
                .asString();
    }
}
