package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Role;
import ir.maktab.onlineexam.utility.RoleTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findRoleByTitle(RoleTitle roleTitle);
}
