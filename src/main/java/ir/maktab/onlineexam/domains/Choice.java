package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;

@Entity
public class Choice extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Boolean isTrueChoice;

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

    public Boolean getTrueChoice() {
        return isTrueChoice;
    }

    public void setTrueChoice(Boolean trueChoice) {
        isTrueChoice = trueChoice;
    }
}