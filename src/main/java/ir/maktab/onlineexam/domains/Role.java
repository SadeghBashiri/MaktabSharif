package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;
import ir.maktab.onlineexam.utility.RoleTitle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class Role extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false)
    private RoleTitle title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges;

    // Constructor


    public Role() {
    }

    public Role(RoleTitle title, List<Privilege> privileges) {
        this.title = title;
        this.privileges = privileges;
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

    public RoleTitle getTitle() {
        return title;
    }

    public void setTitle(RoleTitle title) {
        this.title = title;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }
}
