package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.QuizOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizOperationRepository extends JpaRepository<QuizOperation, Long> {

    List<QuizOperation> findAllByStudentIdAndCourseId(Long studentId, Long courseId);
    List<QuizOperation> findByQuizIdAndStudentId(Long quizId, Long studentId);
    QuizOperation findByStudentIdAndCourseIdAndQuizId(Long studentId, Long courseId, Long quizId);
    List<QuizOperation> findAllByQuizId(Long quizId);
}
