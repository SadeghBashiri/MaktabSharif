package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;
import ir.maktab.onlineexam.utility.StatusTitle;

import javax.persistence.*;

@Entity
@Table(name = "status")
public class Status extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false)
    private StatusTitle title;

    // Constructor


    public Status() {
    }

    public Status(StatusTitle title) {
        this.title = title;
    }

    // Getter and Setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public StatusTitle getTitle() {
        return title;
    }

    public void setTitle(StatusTitle title) {
        this.title = title;
    }
}
