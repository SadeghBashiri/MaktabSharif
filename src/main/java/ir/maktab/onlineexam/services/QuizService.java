package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Quiz;
import ir.maktab.onlineexam.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz save(Quiz quiz){
        return quizRepository.save(quiz);
    }

    public Quiz findQuizById(Long id){
        return quizRepository.findById(id).get();
    }

    public void removeQuizById(Long id){
        quizRepository.deleteById(id);
    }

}
