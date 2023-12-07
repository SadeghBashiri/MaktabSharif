package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course")
public class Course extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, unique = true, length = 30)
    private String title;

    @Column(name = "startDate", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "finishDate", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishDate;

//    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
//    private Boolean isActive;
//
//    @Column(name = "has_teacher", nullable = false, columnDefinition = "TINYINT(1)")
//    private Boolean hasTeacher;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "category_id", nullable = false)
//    private CourseCategory courseCategory;

    @ManyToOne
    private Profile teacher;

    @ManyToMany/*(cascade = CascadeType.ALL, fetch = FetchType.EAGER)*/
    @JoinTable(name = "course_student",
            joinColumns = {@JoinColumn(name = "course_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "student_id", nullable = false)})
    private List<Profile> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    // Getter and Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

//    public Boolean getActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }
//
//    public Boolean getHasTeacher() {
//        return hasTeacher;
//    }
//
//    public void setHasTeacher(Boolean hasTeacher) {
//        this.hasTeacher = hasTeacher;
//    }

//    public CourseCategory getCourseCategory() {
//        return courseCategory;
//    }
//
//    public void setCourseCategory(CourseCategory courseCategory) {
//        this.courseCategory = courseCategory;
//    }

    public Profile getTeacher() {
        return teacher;
    }

    public void setTeacher(Profile teacher) {
        this.teacher = teacher;
    }

    public List<Profile> getStudents() {
        return students;
    }

    public void setStudents(List<Profile> students) {
        this.students = students;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
