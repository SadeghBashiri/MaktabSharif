package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.Course;
import ir.maktab.onlineexam.services.CourseService;
import ir.maktab.onlineexam.utility.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final CourseService courseService;

    @Autowired

    public TeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String getTeacherCourses(Model model){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();

        model.addAttribute("teacherCourses", courseService.findByTeacherUsername(username));
        List<Course> course = courseService.findByTeacherUsername(username);
        model.addAttribute("teacherCoursesNameHeader", "لیست دوره های " + course.get(0).getTeacher().getPerson().getFirstName() + " " + course.get(0).getTeacher().getPerson().getLastName());

        return "teacher-courses";
    }
}
