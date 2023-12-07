package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.QuizOperation;
import ir.maktab.onlineexam.repositories.QuizOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizOperationService {

    private QuizOperationRepository quizOperationRepository;

    @Autowired
    public QuizOperationService(QuizOperationRepository quizOperationRepository) {
        this.quizOperationRepository = quizOperationRepository;
    }

    public List<QuizOperation> findAllByStudentIdAndCourseId(Long studentId, Long courseId){
        return quizOperationRepository.findAllByStudentIdAndCourseId(studentId, courseId);
    }

    public QuizOperation findByQuizIdAndStudentId(Long quizId, Long studentId){
        if (quizOperationRepository.findByQuizIdAndStudentId(quizId, studentId) != null
                && quizOperationRepository.findByQuizIdAndStudentId(quizId, studentId).size() > 0)
            return quizOperationRepository.findByQuizIdAndStudentId(quizId, studentId).get(0);
        else
            return null;
    }

    public boolean quizOperationExist(Long studentId, Long courseId, Long quizId){
        if (quizOperationRepository.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId) == null)
            return false;
        else
            return true;
    }

    public QuizOperation findByStudentIdAndCourseIdAndQuizId(Long studentId, Long courseId, Long quizId){
        return quizOperationRepository.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
    }

    public QuizOperation save(QuizOperation quizOperation){
        return quizOperationRepository.save(quizOperation);
    }

    public QuizOperation findById(Long id){
        return quizOperationRepository.findById(id).get();
    }

    public List<QuizOperation> findAllByQuizId(Long quizId){
        return quizOperationRepository.findAllByQuizId(quizId);
    }
}
