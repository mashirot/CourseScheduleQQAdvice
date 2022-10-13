package ski.mashiro.pojo;

import java.util.List;

/**
 * @author MashiroT
 */
public class Whitelist {
    private List<String> whitelist;

    public Whitelist() {
    }

    public Whitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String toString() {
        return "Whitelist{" +
                "whitelist=" + whitelist +
                '}';
    }
}
