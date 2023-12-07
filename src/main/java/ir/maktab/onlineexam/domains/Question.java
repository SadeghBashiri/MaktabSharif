package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public class Question extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long relatedCourseId;

    @Column(nullable = false)
    private Long creatorTeacherId;

    // Getter and Setter
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

    public Long getRelatedCourseId() {
        return relatedCourseId;
    }

    public void setRelatedCourseId(Long relatedCourseId) {
        this.relatedCourseId = relatedCourseId;
    }

    public Long getCreatorTeacherId() {
        return creatorTeacherId;
    }

    public void setCreatorTeacherId(Long creatorTeacherId) {
        this.creatorTeacherId = creatorTeacherId;
    }
}