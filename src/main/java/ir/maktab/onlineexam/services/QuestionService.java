package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Question;
import ir.maktab.onlineexam.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question){
        return questionRepository.save(question);
    }

    public Question findQuestionById(Long id){
        return questionRepository.findById(id).get();
    }

    public List<Question> findBankQuestions(Long creatorTeacherId, Long relatedCourseId){
        return questionRepository.findByCreatorTeacherIdAndRelatedCourseId(creatorTeacherId, relatedCourseId);
    }

    public void deleteQuestionById(Long id){
        questionRepository.deleteById(id);
    }
}
