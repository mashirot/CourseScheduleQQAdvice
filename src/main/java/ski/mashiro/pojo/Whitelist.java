package ski.mashiro.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MashiroT
 */
public class Whitelist {
    private String[] whitelist = {"12345"};

    public Whitelist() {
    }

    public Whitelist(String[] whitelist) {
        this.whitelist = whitelist;
    }

    public String[] getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String[] whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String toString() {
        return "Whitelist{" +
                "whitelist=" + Arrays.toString(whitelist) +
                '}';
    }
}
