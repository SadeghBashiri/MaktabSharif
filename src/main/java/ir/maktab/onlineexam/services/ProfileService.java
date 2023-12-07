package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Person;
import ir.maktab.onlineexam.domains.Profile;
import ir.maktab.onlineexam.dto.RegisterDTO;
import ir.maktab.onlineexam.exeption.ProfileAlreadyExistException;
import ir.maktab.onlineexam.repositories.PersonRepository;
import ir.maktab.onlineexam.repositories.ProfileRepository;
import ir.maktab.onlineexam.repositories.RoleRepository;
import ir.maktab.onlineexam.repositories.StatusRepository;
import ir.maktab.onlineexam.utility.RoleTitle;
import ir.maktab.onlineexam.utility.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(PersonRepository personRepository
                        , RoleRepository roleRepository
                        , ProfileRepository profileRepository
                        , StatusRepository statusRepository
                        , PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Profile saveProfile(final RegisterDTO registerDTO) {
        if (personRepository.findPersonByEmail(registerDTO.getEmail()).isPresent()) {
            throw new ProfileAlreadyExistException("There is an account with that email address: " + registerDTO.getEmail());
        }
        Person person = new Person();
        person.setFirstName(registerDTO.getFirstName());
        person.setLastName(registerDTO.getLastName());
        person.setEmail(registerDTO.getEmail());

        Profile profile = new Profile();
        profile.setUserName(registerDTO.getEmail());
        profile.setPassword(passwordEncoder.encode((registerDTO.getPassword())));
        profile.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findRoleByTitle
                (RoleTitle.valueOf(registerDTO.getRoleTitle())))));
        profile.setStatus(statusRepository.findStatusByTitle(StatusTitle.WAITING_FOR_VERIFY));
        profile.setPerson(person);

        return profileRepository.save(profile);
    }

    public Profile update(Profile profile){
        return profileRepository.save(profile);
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Optional<Profile> findProfileById(Long id) {
        return profileRepository.findProfileById(id);
    }

    public void activateProfile(Long id){
        Profile requestedProfile = findProfileById(id).get();
        requestedProfile.setStatus(statusRepository.findStatusByTitle(StatusTitle.ACTIVE));
        profileRepository.save(requestedProfile);
    }
    public void deActivateProfile(Long id){
        Profile requestedProfile = findProfileById(id).get();
        requestedProfile.setStatus(statusRepository.findStatusByTitle(StatusTitle.INACTIVE));
        profileRepository.save(requestedProfile);
    }

    public Profile findProfileByUserName(String userName){
        return profileRepository.findProfileByUserName(userName);
    }

    public Profile getSignedInAccount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        return findProfileByUserName(username);
    }

    public List<String> getStringTitlesOfRolesOfSignedInAccount(){
        return getSignedInAccount().getRoles().stream().map(role -> role.getTitle().name()).collect(Collectors.toList());
    }
}
