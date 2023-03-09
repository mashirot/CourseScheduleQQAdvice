package ski.mashiro.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private String url;
    private Integer leadTime;
    private Long owner;
    private Long bot;
}
