package ir.maktab.onlineexam.dto;

public class AnswerDTO {

    private Long id;

    private String content;

    private Long questionId;

    private Integer questionNumberInQuiz;

    public AnswerDTO() {
    }

    public AnswerDTO(Long id, String content, Long questionId, Integer questionNumberInQuiz) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.questionNumberInQuiz = questionNumberInQuiz;
    }

    public Long getId() {
        return id;
    }

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
}