package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;

@Entity
public class Answer extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String content;

    private Long questionId;

    private Integer questionNumberInQuiz;

    @ManyToOne
    private QuizOperation quizOperation;

    // Default Constructor
    public Answer() {
    }

    // Constructor
    public Answer(Long id, String content, Long questionId, Integer questionNumberInQuiz, QuizOperation quizOperation) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.questionNumberInQuiz = questionNumberInQuiz;
        this.quizOperation = quizOperation;
    }


    // Getter and Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionNumberInQuiz() {
        return questionNumberInQuiz;
    }

    public void setQuestionNumberInQuiz(Integer questionNumberInQuiz) {
        this.questionNumberInQuiz = questionNumberInQuiz;
    }

    public QuizOperation getQuizOperation() {
        return quizOperation;
    }

    public void setQuizOperation(QuizOperation quizOperation) {
        this.quizOperation = quizOperation;
    }
}
