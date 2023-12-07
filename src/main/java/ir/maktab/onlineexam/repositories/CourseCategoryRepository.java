package ir.maktab.onlineexam.repositories;

import ir.maktab.onlineexam.domains.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {

    @Modifying
    @Transactional
    @Query("update CourseCategory cc set cc.isActive = true  where cc.id=?1")
    void  activeCategory(Long id);

    @Modifying
    @Transactional
    @Query("update CourseCategory cc set cc.isActive = false where cc.id= :categoryId")
    void  deActiveCategory(@Param("categoryId") Long id);

    CourseCategory findCourseCategoryByTitle(String title);

    CourseCategory findCourseCategoryById(Long id);

//        @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Profile c WHERE c.user_name = :user_name")
//        boolean existsByName(@Param("user_name") String user_name);
}
