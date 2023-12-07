package ir.maktab.onlineexam.dto;

import ir.maktab.onlineexam.utility.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoresOfQuizDTO {

    private List<Score> scores = new ArrayList<>();

    public void add(Score score){
        this.scores.add(score);
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
