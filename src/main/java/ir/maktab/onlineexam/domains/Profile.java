package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "profile")
public class Profile extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true, length = 30)
    private String userName;

    @Column(name = "password", nullable = false, length = 68)
    private String password;

//    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="Person_ID")
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    // TODO: 12/23/2020 USE SET

    @ManyToOne
    private Status status;
    // TODO: 12/23/2020 add related attributes in both side (if needed) and refactor project

    // Constructor
    public Profile() {
    }

    public Profile(String userName, String password, Person person, List<Role> roles, Status status) {
        this.userName = userName;
        this.password = password;
        this.person = person;
        this.roles = roles;
        this.status = status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) /*throws HandlerDataException*/ {
//        if (!userName.matches("(\\w\\d*){3,}")) {
//            throw new HandlerDataException("Username");
//        }
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) /*throws HandlerDataException*/ {
//        if (!password.matches("[a-zA-Z0-9]{3,}")) {
//            throw new HandlerDataException("Password");
//        }
            this.password = password;
        }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
