package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Quiz;
import ir.maktab.onlineexam.domains.QuizOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
