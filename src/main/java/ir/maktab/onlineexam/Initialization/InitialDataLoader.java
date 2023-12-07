package ir.maktab.onlineexam.Initialization;

import ir.maktab.onlineexam.domains.*;
import ir.maktab.onlineexam.repositories.*;
import ir.maktab.onlineexam.utility.PrivilegeTitle;
import ir.maktab.onlineexam.utility.RoleTitle;
import ir.maktab.onlineexam.utility.StatusTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private ProfileRepository profileRepository;
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private StatusRepository statusRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * preparing class requirements using @Autowired
     * @param profileRepository is autowired by constructor
     * @param personRepository is autowired by constructor
     * @param roleRepository is autowired by constructor
     * @param privilegeRepository is autowired by constructor
     * @param statusRepository is autowired by constructor
     * @param passwordEncoder is autowired by constructor
     */

    @Autowired
    public InitialDataLoader(ProfileRepository profileRepository,
                             PersonRepository personRepository,
                             RoleRepository roleRepository,
                             PrivilegeRepository privilegeRepository,
                             StatusRepository statusRepository,
                             PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * saves initial records if not exist
     * @param event context refreshed event
     */

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        else {
            if (statusRepository.findStatusByTitle(StatusTitle.ACTIVE) == null)
                statusRepository.save(new Status(StatusTitle.ACTIVE));
            if (statusRepository.findStatusByTitle(StatusTitle.INACTIVE) == null)
                statusRepository.save(new Status(StatusTitle.INACTIVE));
            if (statusRepository.findStatusByTitle(StatusTitle.WAITING_FOR_VERIFY) == null)
                statusRepository.save(new Status(StatusTitle.WAITING_FOR_VERIFY));
            // TODO: 12/20/2020 add more status if needed


            if (privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.MANAGER_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(PrivilegeTitle.MANAGER_GENERAL_PRIVILEGE));
            if (privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE));
            if (privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE) == null)
                privilegeRepository.save(new Privilege(PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE));

            // TODO: 12/20/2020 add other privileges when needed


            if (roleRepository.findRoleByTitle(RoleTitle.MANAGER) == null) {
                List<Privilege> adminPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.MANAGER_GENERAL_PRIVILEGE)));
                // TODO: 12/20/2020 add all privileges of admin
                roleRepository.save(new Role(RoleTitle.MANAGER, adminPrivileges));
            }

            if (roleRepository.findRoleByTitle(RoleTitle.TEACHER) == null) {
                List<Privilege> teacherPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.TEACHER_GENERAL_PRIVILEGE)));
                // TODO: 12/20/2020 add all privileges of teacher
                roleRepository.save(new Role(RoleTitle.TEACHER, teacherPrivileges));
            }

            if (roleRepository.findRoleByTitle(RoleTitle.STUDENT) == null) {
                List<Privilege> studentPrivileges = new ArrayList<>(
                        Arrays.asList(privilegeRepository.findPrivilegeByTitle(PrivilegeTitle.STUDENT_GENERAL_PRIVILEGE)));
                // TODO: 12/20/2020 add all privileges of student
                roleRepository.save(new Role(RoleTitle.STUDENT, studentPrivileges));
            }


            if (personRepository.findPersonByEmail("sample@gmail.com").isEmpty()) {
                /* # # # # # # 3*/
                Person person = new Person();
                person.setFirstName("admin");
                person.setLastName("admin");
                person.setEmail("sample@gmail.com");

                Profile profile = new Profile();
                profile.setUserName("sample@gmail.com");
                profile.setPassword(passwordEncoder.encode("1"));
                profile.setRoles(Arrays.asList(roleRepository.findRoleByTitle(RoleTitle.MANAGER)));
                profile.setStatus(statusRepository.findStatusByTitle(StatusTitle.ACTIVE));
                profile.setPerson(person);

                profileRepository.save(profile);
                /* # # # # # # 3*/

//                personRepository.save(new Person(
//                        "admin",
//                        "admin",
//                        "sample@gmail.com")
//                );
            }

//            if (profileRepository.findProfileByUserName("sample@gmail.com") == null)
//                profileRepository.save(new Profile(
//                        "sample@gmail.com",
//                        passwordEncoder.encode("1"),
//                        personRepository.findPersonByEmail("sample@gmail.com").get(),
//                        Arrays.asList(roleRepository.findRoleByTitle(RoleTitle.MANAGER)),
//                        statusRepository.findStatusByTitle(StatusTitle.ACTIVE))
//                );

            alreadySetup = true;
        }
    }
}