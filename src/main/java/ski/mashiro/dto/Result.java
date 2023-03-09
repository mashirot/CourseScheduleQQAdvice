package ski.mashiro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class Result<T>{
    private int code;
    private T data;
    private String msg;

    public Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Result<T> success(int code, T data) {
        return new Result<>(code, data, null);
    }

    public static <T> Result<T> failed(int code, String msg) {
        return new Result<>(code, null, msg);
    }
}
