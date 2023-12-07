package ir.maktab.onlineexam.dto;

public class GradingDTO {
    private Double defaultScoreForQuestion;
    private Double participantScoreForQuestion;

    public GradingDTO() {
    }

    public GradingDTO(Double defaultScoreForQuestion, Double participantScoreForQuestion) {
        this.defaultScoreForQuestion = defaultScoreForQuestion;
        this.participantScoreForQuestion = participantScoreForQuestion;
    }

    public Double getDefaultScoreForQuestion() {
        return defaultScoreForQuestion;
    }

    public void setDefaultScoreForQuestion(Double defaultScoreForQuestion) {
        this.defaultScoreForQuestion = defaultScoreForQuestion;
    }

    public Double getParticipantScoreForQuestion() {
        return participantScoreForQuestion;
    }

    public void setParticipantScoreForQuestion(Double participantScoreForQuestion) {
        this.participantScoreForQuestion = participantScoreForQuestion;
    }
}
