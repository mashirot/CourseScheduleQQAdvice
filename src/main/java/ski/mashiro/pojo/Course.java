package ski.mashiro.pojo;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author MashiroT
 */
public class Course {
    private String courseName;
    private String courseLocation;
    private String courseLecturer;
    private String courseShowDate;
    private String courseShowTime;
    private String courseShowWeek;
    private String[] courseWeek;
    private String jsonCourseWeek;
    private String courseDate;
    private String[] courseInputDate;

    public Course() {
    }

    public Course(String courseName, String courseLocation, String courseLecturer, String courseShowDate, String courseShowTime, String courseShowWeek, String[] courseWeek, String jsonCourseWeek, String courseDate, String[] courseInputDate) {
        this.courseName = courseName;
        this.courseLocation = courseLocation;
        this.courseLecturer = courseLecturer;
        this.courseShowDate = courseShowDate;
        this.courseShowTime = courseShowTime;
        this.courseShowWeek = courseShowWeek;
        this.courseWeek = courseWeek;
        this.jsonCourseWeek = jsonCourseWeek;
        this.courseDate = courseDate;
        this.courseInputDate = courseInputDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public String getCourseLecturer() {
        return courseLecturer;
    }

    public void setCourseLecturer(String courseLecturer) {
        this.courseLecturer = courseLecturer;
    }

    public String getCourseShowDate() {
        return courseShowDate;
    }

    public void setCourseShowDate(String courseShowDate) {
        this.courseShowDate = courseShowDate;
    }

    public String getCourseShowTime() {
        return courseShowTime;
    }

    public void setCourseShowTime(String courseShowTime) {
        this.courseShowTime = courseShowTime;
    }

    public String getCourseShowWeek() {
        return courseShowWeek;
    }

    public void setCourseShowWeek(String courseShowWeek) {
        this.courseShowWeek = courseShowWeek;
    }

    public String[] getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String[] courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getJsonCourseWeek() {
        return jsonCourseWeek;
    }

    public void setJsonCourseWeek(String jsonCourseWeek) {
        this.jsonCourseWeek = jsonCourseWeek;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public String[] getCourseInputDate() {
        return courseInputDate;
    }

    public void setCourseInputDate(String[] courseInputDate) {
        this.courseInputDate = courseInputDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseName, course.courseName) && Objects.equals(courseLocation, course.courseLocation) && Objects.equals(courseLecturer, course.courseLecturer) && Objects.equals(courseShowDate, course.courseShowDate) && Objects.equals(courseShowTime, course.courseShowTime) && Objects.equals(courseShowWeek, course.courseShowWeek) && Arrays.equals(courseWeek, course.courseWeek) && Objects.equals(jsonCourseWeek, course.jsonCourseWeek) && Objects.equals(courseDate, course.courseDate) && Arrays.equals(courseInputDate, course.courseInputDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(courseName, courseLocation, courseLecturer, courseShowDate, courseShowTime, courseShowWeek, jsonCourseWeek, courseDate);
        result = 31 * result + Arrays.hashCode(courseWeek);
        result = 31 * result + Arrays.hashCode(courseInputDate);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", courseLocation='" + courseLocation + '\'' +
                ", courseLecturer='" + courseLecturer + '\'' +
                ", courseShowDate='" + courseShowDate + '\'' +
                ", courseShowTime='" + courseShowTime + '\'' +
                ", courseShowWeek='" + courseShowWeek + '\'' +
                ", courseWeek=" + Arrays.toString(courseWeek) +
                ", jsonCourseWeek='" + jsonCourseWeek + '\'' +
                ", courseDate='" + courseDate + '\'' +
                ", courseInputDate=" + Arrays.toString(courseInputDate) +
                '}';
    }
}
