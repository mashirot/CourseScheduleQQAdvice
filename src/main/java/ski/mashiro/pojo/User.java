package ski.mashiro.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class User {
    private Long qq;
    private Integer uid;
    private String username;
    private String password;
    private String apiToken;
    private Date termStartDate;
    private Date termEndDate;
    private String authToken;

    public User(Long qq, String username, String apiToken) {
        this.qq = qq;
        this.username = username;
        this.apiToken = apiToken;
    }
}
