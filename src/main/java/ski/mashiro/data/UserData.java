package ski.mashiro.data;

import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.dto.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.util.HttpUtils;

/**
 * @author MashiroT
 */
public class UserData {

    public static User getUser(User user) {
        Result<User> userResult = HttpUtils.sendUserReq(user);
        if (userResult.getCode() == StatusCodeConstants.USER_LOGIN_FAILED || userResult.getCode() == 500) {
            return null;
        }
        userResult.getData().setQq(user.getQq());
        return userResult.getData();
    }
}
