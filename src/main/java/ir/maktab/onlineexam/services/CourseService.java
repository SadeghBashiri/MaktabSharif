package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Course;
import ir.maktab.onlineexam.domains.Profile;
import ir.maktab.onlineexam.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private CourseRepository courseRepository;


    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public Course findCourseById(Long id){
        return courseRepository.findById(id).get();
    }

    public Course save(Course course){
        return courseRepository.save(course);
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> findByTeacherUsername(String userName){
        return courseRepository.findByTeacherUserName(userName);
    }

    public List<Course> findCourseByStudentsContains(Profile studentAccount){
        return courseRepository.findCourseByStudentsContains(studentAccount);
    }
}
