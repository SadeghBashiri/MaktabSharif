package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Status;
import ir.maktab.onlineexam.utility.StatusTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findStatusByTitle(StatusTitle statusTitle);
}
