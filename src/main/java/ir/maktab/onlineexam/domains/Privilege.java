package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.utility.PrivilegeTitle;

import javax.persistence.*;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private PrivilegeTitle title;

    // Constructor


    public Privilege() {
    }

    public Privilege(PrivilegeTitle title) {
        this.title = title;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrivilegeTitle getTitle() {
        return title;
    }

    public void setTitle(PrivilegeTitle title) {
        this.title = title;
    }
}