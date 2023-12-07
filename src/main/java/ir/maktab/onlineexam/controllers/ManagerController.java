package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.*;
import ir.maktab.onlineexam.dto.CourseDTO;
import ir.maktab.onlineexam.dto.EditProfileDTO;
import ir.maktab.onlineexam.services.CourseCategoryService;
import ir.maktab.onlineexam.services.CourseService;
import ir.maktab.onlineexam.services.ProfileService;
import ir.maktab.onlineexam.services.RoleService;
import ir.maktab.onlineexam.utility.RoleTitle;
import org.joda.time.DateTime;
import org.kaveh.commons.farsi.date.JalaliDateImpl;
import org.kaveh.commons.farsi.utils.JalaliCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ProfileService profileService;
    private final RoleService roleService;
    private final CourseCategoryService courseCategoryService;
    private final CourseService courseService;

    @Autowired
    public ManagerController(ProfileService profileService
            , RoleService roleService
            , CourseCategoryService courseCategoryService
            , CourseService courseService) {
        this.roleService = roleService;
        this.profileService = profileService;
        this.courseCategoryService = courseCategoryService;
        this.courseService = courseService;
    }

    @GetMapping("/profileList")
    public String pendingProfile(Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profiles", profiles);
        return "profileList";
    }

    @GetMapping(value = "/activeProfile/{id}")
    public String activeProfile(@PathVariable("id") Long id) {
        profileService.activateProfile(id);
        return "redirect:/manager/profileList";
    }

    @GetMapping(value = "/deActiveProfile/{id}")
    public String deActiveProfile(@PathVariable("id") Long id) {
        profileService.deActivateProfile(id);
        return "redirect:/manager/profileList";
    }

    @GetMapping(value = "/editProfile/{id}")
    public String editProfile(Model model, @PathVariable(value = "id") final Long id) {
        Optional<Profile> requestProfile = profileService.findProfileById(id);
        EditProfileDTO editProfileDTO = new EditProfileDTO();
        editProfileDTO.setId(requestProfile.get().getId());
        editProfileDTO.setPersonId(requestProfile.get().getPerson().getId());
        editProfileDTO.setFirstName(requestProfile.get().getPerson().getFirstName());
        editProfileDTO.setLastName(requestProfile.get().getPerson().getLastName());
        editProfileDTO.setRoleTitle(requestProfile.get().getRoles().get(0).getTitle().name());
        List<Role> roles = roleService.findAllRole();
        model.addAttribute("editProfile", editProfileDTO);
        model.addAttribute("roles", roleService.findAllRole());
//        model.addAttribute("editProfileDto", new EditProfileDTO());
        return "edit-profile";
    }

    @PostMapping(value = "/editProfile/{id}")
    public String submitEditAccount(@ModelAttribute EditProfileDTO editProfileDTO,
                                    @PathVariable(value = "id") final Long id) {

        boolean isInputInvalid = false;
        String redirectUrl = "redirect:/manager/editProfile/" + id + "/?";
        if (editProfileDTO.getFirstName() == "" || editProfileDTO.getLastName() == "") {
            redirectUrl += "Error";
            isInputInvalid = true;
        }
        if (isInputInvalid) {
            return redirectUrl;
        } else {
            Person requestedPerson = profileService.findProfileById(id).get().getPerson();
            requestedPerson.setFirstName(editProfileDTO.getFirstName());
            requestedPerson.setLastName(editProfileDTO.getLastName());

            Profile requestedProfile = profileService.findProfileById(id).get();
            requestedProfile.setRoles(new ArrayList<>(
                    Arrays.asList(roleService.findRoleByTitle(RoleTitle.valueOf(editProfileDTO.getRoleTitle())))
            ));
            requestedProfile.setPerson(requestedPerson);
            profileService.update(requestedProfile);
            return "redirect:/manager/profileList";
        }
    }

    @GetMapping("/courseCategory")
    public String CourseCategory(Model model) {
        List<CourseCategory> courseCategories = courseCategoryService.findAll();
        model.addAttribute("courseCategories", courseCategories);
        model.addAttribute("courseCategory", new CourseCategory());
        return "courseCategory";
    }

    @GetMapping(value = "/active/{id}")
    public String activeCategory(@PathVariable("id") Long id) {
        courseCategoryService.activeCategory(id);
        return "redirect:/manager/courseCategory";
    }

    @GetMapping(value = "/deActive/{id}")
    public String deActiveCategory(@PathVariable("id") Long id) {
        courseCategoryService.deActiveCategory(id);
        return "redirect:/manager/courseCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute CourseCategory addCategory) {
        String redirectUrl = "redirect:/manager/courseCategory/?";
        if (courseCategoryService.isCategoryExisting(addCategory.getTitle())) {
            redirectUrl += "&";
            redirectUrl += "categoryTitleError";
            return redirectUrl;
        } else {
            CourseCategory category = new CourseCategory();
            category.setId(addCategory.getId());
            category.setTitle(addCategory.getTitle());
            category.setActive(true);
            courseCategoryService.save(category);
            return "redirect:/manager/courseCategory";
        }
    }

    @GetMapping(value = "/editCategory/{id}")
    public String editCategory(Model model, @PathVariable(value = "id") final Long id) {
        CourseCategory requestCategory = courseCategoryService.findCourseCategoryById(id);
        CourseCategory sendCategory = new CourseCategory();
        sendCategory.setId(id);
        sendCategory.setTitle(requestCategory.getTitle());
        List<CourseCategory> categoryList = courseCategoryService.findAll();
        model.addAttribute("courseCategory", sendCategory);
        model.addAttribute("courseCategories", categoryList);
        return "courseCategory";
    }

    @RequestMapping(value = "/courseList")
    public String addCourse(Model model) {

        List<CourseDTO> allCoursesDtos = new ArrayList<>();
        List<Course> allCourses = courseService.findAll();
        if (allCourses != null && allCourses.size() > 0) {
            for (Course courseItem : allCourses) {
                allCoursesDtos.add(
                        new CourseDTO(
                                courseItem.getId(),
                                courseItem.getTitle(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getStartDate())).toString(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getFinishDate())).toString()
                        )
                );
            }
        }

        model.addAttribute("allCoursesDtoList", allCoursesDtos);
        model.addAttribute("courseDto", new CourseDTO());

        return "courseList";
    }

    @GetMapping(value = "/deleteCourse/{id}")
    public String deleteCourse(Model model, @PathVariable Long id){
        courseService.deleteCourseById(id);
        return "redirect:/manager/courseList";
    }

    @PostMapping(value = "/courseList")
    public String submitCourseAddition(Model model, @ModelAttribute CourseDTO addingCourseDto) {

        Course inputCourse = new Course();
        inputCourse.setId(addingCourseDto.getId());
        inputCourse.setTitle(addingCourseDto.getTitle());

        if (addingCourseDto.getStartDate() != null && !addingCourseDto.getStartDate().isEmpty()
                && addingCourseDto.getStartDate().split("/").length == 3) {
            //add more condition above to be sure about input validation (for example all parts being numeric)
            int persianYear = Integer.parseInt(addingCourseDto.getStartDate().split("/")[0]);
            int persianMonth = Integer.parseInt(addingCourseDto.getStartDate().split("/")[1]);
            int persianDay = Integer.parseInt(addingCourseDto.getStartDate().split("/")[2]);

            inputCourse.setStartDate(JalaliCalendarUtils
                    .convertJalaliToGregorian(new JalaliDateImpl(persianYear, persianMonth, persianDay)).toDate());
        }

        if (addingCourseDto.getFinishDate() != null && !addingCourseDto.getFinishDate().isEmpty()
                && addingCourseDto.getFinishDate().split("/").length == 3) {
            //add more condition above to be sure about input validation (for example all parts being numeric)
            int persianYear = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[0]);
            int persianMonth = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[1]);
            int persianDay = Integer.parseInt(addingCourseDto.getFinishDate().split("/")[2]);

            inputCourse.setFinishDate(JalaliCalendarUtils
                    .convertJalaliToGregorian(new JalaliDateImpl(persianYear, persianMonth, persianDay)).toDate());
        }

        Date dateToday = new Date();
        dateToday.setHours(0);
        dateToday.setMinutes(0);
        dateToday.setSeconds(0);
        if (inputCourse.getStartDate() != null && inputCourse.getFinishDate() != null
                && inputCourse.getTitle() != null && !inputCourse.getTitle().isEmpty()
                && inputCourse.getStartDate().getTime() / 1000 / 60 / 60 / 24 >= dateToday.getTime() / 1000 / 60 / 60 / 24
                && inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) < 0) {

            //because the course may be related to teacher or student or quiz
            if (inputCourse.getId() != null) {
                Course requestedCourseBeforeUpdate = courseService.findCourseById(inputCourse.getId());
                inputCourse.setTeacher(requestedCourseBeforeUpdate.getTeacher());
                inputCourse.setStudents(requestedCourseBeforeUpdate.getStudents());
                inputCourse.setQuizzes(requestedCourseBeforeUpdate.getQuizzes());
            }
            courseService.save(inputCourse);
        }

        String redirectUrl = "redirect:/manager/courseList?";
        if (inputCourse.getTitle().isEmpty())
            redirectUrl += "&invalidTitleError";
        if (inputCourse.getStartDate().getTime() / 1000 / 60 / 60 / 24 < dateToday.getTime() / 1000 / 60 / 60 / 24)
            redirectUrl += "&invalidStartDateError";
        if (inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) > 0)
            redirectUrl += "&startDateGreaterThanFinishDateError";
        if (inputCourse.getStartDate().compareTo(inputCourse.getFinishDate()) == 0)
            redirectUrl += "&startAndFinishAtSameDateError";
        return redirectUrl;
    }

    @GetMapping(value = "/editCourse/{id}")
    public String editCourse(Model model, @PathVariable Long id) {

        List<CourseDTO> allCoursesDtos = new ArrayList<>();
        List<Course> allCourses = courseService.findAll();
        if (allCourses != null && allCourses.size() > 0) {
            for (Course courseItem : allCourses) {
                allCoursesDtos.add(
                        new CourseDTO(
                                courseItem.getId(),
                                courseItem.getTitle(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getStartDate())).toString(),
                                JalaliCalendarUtils.convertGregorianToJalali(
                                        new DateTime(courseItem.getFinishDate())).toString()
                        )
                );
            }
        }

        model.addAttribute("allCoursesDtoList", allCoursesDtos);

        Course requestedCourse = courseService.findCourseById(id);
        CourseDTO sendingCourseDto = new CourseDTO(
                requestedCourse.getId(),
                requestedCourse.getTitle(),
                requestedCourse.getStartDate().toString(),
                requestedCourse.getFinishDate().toString()
        );
        model.addAttribute("courseDto", sendingCourseDto);
        return "courseList";
    }

    @GetMapping("/courseMembers/{courseId}")
    public String getCourseMembers(Model model, @PathVariable Long courseId) {
        model.addAttribute("allRoles", roleService.findAllRole());
        model.addAttribute("course", courseService.findCourseById(courseId));
        model.addAttribute("courseTitleNamePersianHeader", "اعضاء دوره "
                + courseService.findCourseById(courseId).getTitle());
        return "courseMembers";
    }

    @GetMapping("/addMemberToCourse/{courseId}")
    public String addMemberToCourse(Model model,
                                    @PathVariable Long courseId,
                                    @RequestParam(name = "roleTitleName") String roleTitleName) {

        Course requestedCourse = courseService.findCourseById(courseId);
        List<Profile> accountsWithRequestedRole = profileService.findAll().stream()
                .filter(account -> account.getRoles()
                        .contains(roleService.findRoleByTitle(RoleTitle.valueOf(roleTitleName))))
                .collect(Collectors.toList());

        List<Profile> accountsToLoad = new ArrayList<>();
        if (roleTitleName.equals("TEACHER")) {
            if (requestedCourse.getTeacher() != null) {
                accountsToLoad = accountsWithRequestedRole.stream()
                        .filter(account -> !requestedCourse.getTeacher().equals(account))
                        .collect(Collectors.toList());
            } else
                accountsToLoad = accountsWithRequestedRole;
        } else if (roleTitleName.equals("STUDENT")) {
            accountsToLoad = accountsWithRequestedRole.stream()
                    .filter(account -> !requestedCourse.getStudents().contains(account))
                    .collect(Collectors.toList());
        } else {
            // TODO: 12/21/2020 if members with other role added to course
        }

        model.addAttribute("roleTitleName", roleTitleName);
        model.addAttribute("roleTitleNamePersianHeader", "لیست "
                + RoleTitle.valueOf(roleTitleName).getPersian() + "ها");
        model.addAttribute("course", requestedCourse);
        model.addAttribute("accountsToAdd", accountsToLoad);
        return "choose-account-to-add-to-course";
    }

    @PostMapping(value = "/addMemberToCourse/{courseId}")
    public String submitAddMemberToCourse(@PathVariable Long courseId,
                                          @RequestParam(name = "accountId") Long accountId,
                                          @RequestParam(name = "roleTitleName") String roleTitleName) {
        Course requestedCourse = courseService.findCourseById(courseId);
        Profile requestedAccount = profileService.findProfileById(accountId).get();
        if (roleTitleName.equals("TEACHER")) {
            requestedCourse.setTeacher(requestedAccount);
        } else if (roleTitleName.equals("STUDENT"))
            requestedCourse.getStudents().add(requestedAccount);
        else {
            // TODO: 12/21/2020 if members with other role added to course
        }
        courseService.save(requestedCourse);
        return "redirect:/manager/addMemberToCourse/" + courseId + "?roleTitleName=" + roleTitleName;
    }

    @GetMapping("/deleteCourseMember/{courseId}")
    public String deleteCourseMember(@PathVariable Long courseId,
                                     @RequestParam(name = "memberId") Long memberId,
                                     @RequestParam(name = "roleTitleName") String roleTitleName) {
        Course requestedCourse = courseService.findCourseById(courseId);
        Profile requestedMember = profileService.findProfileById(memberId).get();
        if (roleTitleName.equals("TEACHER") && requestedCourse.getTeacher().equals(requestedMember)) {
            requestedCourse.setTeacher(null);
        } else if (roleTitleName.equals("STUDENT")) {
            requestedCourse.getStudents().remove(requestedMember);
        } else {
            // TODO: 12/21/2020  if members with other role added to course
        }
        courseService.save(requestedCourse);
        return "redirect:/manager/courseMembers/" + courseId;
    }
}
