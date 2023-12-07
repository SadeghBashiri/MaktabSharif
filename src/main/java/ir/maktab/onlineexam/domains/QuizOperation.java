package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;





@Entity
public class QuizOperation extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long quizId;

    private Boolean isFinished;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date finishDate;

    private String resultScores;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizOperation")
    private List<Answer> answerList;

    private Boolean isAutoGraded;

    private Boolean isCustomGraded;

    // Getter and Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getResultScores() {
        return resultScores;
    }

    public void setResultScores(String resultScores) {
        this.resultScores = resultScores;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public Boolean getAutoGraded() {
        return isAutoGraded;
    }

    public void setAutoGraded(Boolean autoGraded) {
        isAutoGraded = autoGraded;
    }

    public Boolean getCustomGraded() {
        return isCustomGraded;
    }

    public void setCustomGraded(Boolean customGraded) {
        isCustomGraded = customGraded;
    }
}
