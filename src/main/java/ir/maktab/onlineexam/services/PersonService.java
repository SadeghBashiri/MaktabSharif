package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Person;
import ir.maktab.onlineexam.domains.Profile;
import ir.maktab.onlineexam.dto.RegisterDTO;
import ir.maktab.onlineexam.exeption.ProfileAlreadyExistException;
import ir.maktab.onlineexam.repositories.PersonRepository;
import ir.maktab.onlineexam.repositories.ProfileRepository;
import ir.maktab.onlineexam.repositories.RoleRepository;
import ir.maktab.onlineexam.utility.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository
                        , RoleRepository roleRepository
                        , ProfileRepository profileRepository
                        , PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public boolean isEmailExisting(String email){
        if (personRepository.findPersonByEmail(email) != null)
            return true;
        else
            return false;
    }

    Optional<Person> findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }
}
