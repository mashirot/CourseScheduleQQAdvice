package ski.mashiro.pojo;

import java.util.Objects;

/**
 * @author MashiroT
 */
public class User {
    private String userCode;
    private String userApiToken;

    public User() {
    }

    public User(String userCode, String userApiToken) {
        this.userCode = userCode;
        this.userApiToken = userApiToken;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserApiToken() {
        return userApiToken;
    }

    public void setUserApiToken(String userApiToken) {
        this.userApiToken = userApiToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userCode, user.userCode) && Objects.equals(userApiToken, user.userApiToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCode, userApiToken);
    }

    @Override
    public String toString() {
        return "User{" +
                "userCode='" + userCode + '\'' +
                ", userApiToken='" + userApiToken + '\'' +
                '}';
    }
}
