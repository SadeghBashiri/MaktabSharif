package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.CourseCategory;
import ir.maktab.onlineexam.repositories.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    @Autowired
    public CourseCategoryService(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }


    public List<CourseCategory> findAll() {
        return courseCategoryRepository.findAll();
    }

    public void activeCategory(Long id) {
        courseCategoryRepository.activeCategory(id);
    }

    public void deActiveCategory(Long id) {
        courseCategoryRepository.deActiveCategory(id);
    }

    public void save(CourseCategory category) {
        courseCategoryRepository.save(category);
    }

    public boolean isCategoryExisting(String title){
        if (courseCategoryRepository.findCourseCategoryByTitle(title) != null)
            return true;
        else
            return false;
    }

    public CourseCategory findCourseCategoryById (Long id) {
        return courseCategoryRepository.findCourseCategoryById(id);
    }

    public CourseCategory findCourseCategoryByTitle (String title) {
        return courseCategoryRepository.findCourseCategoryByTitle(title);
    }
}
