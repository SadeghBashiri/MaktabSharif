package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CourseCategory")
public class CourseCategory extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, unique = true, length = 20)
    private String title;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseCategory", cascade = CascadeType.ALL)
//    private Set<Course> courses = new HashSet<Course>();

    //Getter and Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

//    public Set<Course> getCourses() {
//        return courses;
//    }
//
//    public void setCourses(Set<Course> courses) {
//        this.courses = courses;
//    }
}
