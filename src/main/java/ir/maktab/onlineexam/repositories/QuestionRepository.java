package ir.maktab.onlineexam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ir.maktab.onlineexam.domains.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCreatorTeacherIdAndRelatedCourseId(Long creatorTeacherId, Long relatedCourseId);
}
