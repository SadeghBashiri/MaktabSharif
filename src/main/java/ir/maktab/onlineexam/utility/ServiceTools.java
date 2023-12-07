package ir.maktab.onlineexam.utility;

import ir.maktab.onlineexam.services.ProfileService;
import ir.maktab.onlineexam.services.QuestionService;
import ir.maktab.onlineexam.services.QuizOperationService;
import ir.maktab.onlineexam.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTools {

    private static QuizService quizService;
    private static ProfileService accountService;
    private static QuestionService questionService;
    private static QuizOperationService quizOperationService;

    @Autowired
    private void setQuizService(QuizService quizService){
        ServiceTools.quizService = quizService;
    }

    @Autowired
    private void setAccountService(ProfileService accountService){
        ServiceTools.accountService = accountService;
    }

    @Autowired
    private void setQuestionService(QuestionService questionService){
        ServiceTools.questionService = questionService;
    }

    @Autowired
    private void setQuizOperationService(QuizOperationService quizOperationService){
        ServiceTools.quizOperationService = quizOperationService;
    }

    public static QuizService getQuizService(){
        return ServiceTools.quizService;
    }
    public static ProfileService getAccountService(){
        return ServiceTools.accountService;
    }
    public static QuestionService getQuestionService(){
        return ServiceTools.questionService;
    }
    public static QuizOperationService getQuizOperationService(){
        return ServiceTools.quizOperationService;
    }
}