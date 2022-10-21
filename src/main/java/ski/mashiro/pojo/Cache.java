package ski.mashiro.pojo;

import java.util.Date;
import java.util.List;

/**
 * @author MashiroT
 */
public class Cache {
    private List<Date> dateList;
    private Integer index;

    public Cache() {
    }

    public Cache(List<Date> dateList, Integer index) {
        this.dateList = dateList;
        this.index = index;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    public void setDateList(List<Date> dateList) {
        this.dateList = dateList;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
