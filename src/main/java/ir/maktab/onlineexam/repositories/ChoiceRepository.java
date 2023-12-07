package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
