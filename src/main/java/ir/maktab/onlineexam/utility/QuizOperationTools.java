package ir.maktab.onlineexam.utility;

import ir.maktab.onlineexam.domains.*;

import java.util.ArrayList;
import java.util.List;

public class QuizOperationTools {

    public static String autoPrepareStudentScores(QuizOperation finishedQuizOperation){

        Quiz requestedQuiz = ServiceTools.getQuizService().findQuizById(finishedQuizOperation.getQuizId());

        List<Double> defaultScoresOfQuiz = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList());

        List<Answer> userAnswers = finishedQuizOperation.getAnswerList();


        ArrayList<Double> studentScoresList = new ArrayList<>();
        while (studentScoresList.size() < defaultScoresOfQuiz.size())
            studentScoresList.add(0.0);

        if (userAnswers != null) {
            for (Answer answerItem : userAnswers) {
                Question correspondingQuestion = ServiceTools.getQuestionService().findQuestionById(answerItem.getQuestionId());
                if (correspondingQuestion instanceof MultiChoiceQuestion) {
                    if (QuestionTools.isAnswerOfMultiChoiceQuestion((MultiChoiceQuestion) correspondingQuestion,
                            answerItem.getContent()))
                        studentScoresList.set(
                                answerItem.getQuestionNumberInQuiz() - 1,
                                defaultScoresOfQuiz.get(answerItem.getQuestionNumberInQuiz() - 1));
                }
            }
        }
        return ScoresListTools.arrayListToString(studentScoresList);
    }
}