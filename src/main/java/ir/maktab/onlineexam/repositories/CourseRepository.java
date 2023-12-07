package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.Course;
import ir.maktab.onlineexam.domains.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherUserName(String userName);
    List<Course> findCourseByStudentsContains(Profile studentAccount);
}
