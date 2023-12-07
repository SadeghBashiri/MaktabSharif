package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Privilege;
import ir.maktab.onlineexam.utility.PrivilegeTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    public Privilege findPrivilegeByTitle(PrivilegeTitle privilegeTitle);
}