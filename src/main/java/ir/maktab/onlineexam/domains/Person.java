package ir.maktab.onlineexam.domains;

import ir.maktab.onlineexam.base.domains.BaseEntity;
import ir.maktab.onlineexam.exeption.HandlerDataException;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

//    @OneToOne(cascade = CascadeType.ALL)
//    private Profile profile;

    // NoArgsConstructor
    public Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) /*throws HandlerDataException*/ {
//        if (!firstName.matches("@\"^[\\u0600-\\u06FF]+$\"")) { // //@"^[\u0600-\u06FF]+$" //  /^[\u0600-\u06FF\s]+$/      // /@"^([\u0600-\u06FF]+\s?)+$"/ // "\\w{3,}"
//            throw new HandlerDataException("FirstName");
//        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) /*throws HandlerDataException*/ {
//        if (!lastName.matches("\\w{3,}")) {
//            throw new HandlerDataException("LastName");
//        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) /*throws HandlerDataException*/ {
//        if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")) {
//            throw new HandlerDataException("Email Address");
//        }
        this.email = email;
    }

//    public Profile getProfile() {
//        return profile;
//    }
//
//    public void setProfile(Profile profile) {
//        this.profile = profile;
//    }
}
