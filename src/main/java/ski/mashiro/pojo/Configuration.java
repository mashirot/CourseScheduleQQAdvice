package ski.mashiro.pojo;

import ski.mashiro.CourseScheduleQQAdvice;

/**
 * @author MashiroT
 */
public class Configuration {
    private String version = String.valueOf(CourseScheduleQQAdvice.INSTANCE.getDescription().getVersion());
    private String apiUrl = "https://schedule.mashiro.ski/api";
    private Long bot = 12345L;
    private Long owner = 12345L;

    public Configuration() {
    }

    public Configuration(String version, String apiUrl, Long bot, Long owner) {
        this.version = version;
        this.apiUrl = apiUrl;
        this.bot = bot;
        this.owner = owner;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public Long getBot() {
        return bot;
    }

    public Long getOwner() {
        return owner;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "version='" + version + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", bot=" + bot +
                ", owner='" + owner + '\'' +
                '}';
    }
}
