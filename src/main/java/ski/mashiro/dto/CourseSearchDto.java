package ski.mashiro.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MashiroT
 */
@Data
@NoArgsConstructor
public class CourseSearchDto {
    private Integer uid;
    private Integer courseId;
    private Integer currWeek;
    private String dayOfWeek;
    private Date termStartDate;

    public CourseSearchDto(Integer uid) {
        this.uid = uid;
    }

    public CourseSearchDto(Integer uid, Date termStartDate) {
        this.uid = uid;
        this.termStartDate = termStartDate;
    }

    public CourseSearchDto(Integer uid, String dayOfWeek) {
        this.uid = uid;
        this.dayOfWeek = dayOfWeek;
    }

    public CourseSearchDto(Integer uid, String dayOfWeek, Date termStartDate) {
        this.uid = uid;
        this.dayOfWeek = dayOfWeek;
        this.termStartDate = termStartDate;
    }
}

