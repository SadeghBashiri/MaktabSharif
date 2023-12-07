package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Quiz extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Integer time;

    @ManyToOne
    private Course course;

    @ManyToMany
    private List<Question> questions;

    private String defaultScoresList;

    @Column(nullable = false)
    private Long creatorTeacherId;

    private Boolean isActive;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getDefaultScoresList() {
        return defaultScoresList;
    }

    public void setDefaultScoresList(String defaultScoresList) {
        this.defaultScoresList = defaultScoresList;
    }

    public Long getCreatorTeacherId() {
        return creatorTeacherId;
    }

    public void setCreatorTeacherId(Long creatorTeacherId) {
        this.creatorTeacherId = creatorTeacherId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
