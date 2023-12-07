package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findProfileByUserNameAndPassword(String userName, String password);

    Profile findProfileByUserName(String username);

    Optional<Profile> findProfileById(Long id);



}
