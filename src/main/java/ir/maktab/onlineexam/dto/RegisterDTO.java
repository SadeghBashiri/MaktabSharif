package ir.maktab.onlineexam.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

public class RegisterDTO implements Serializable {

    @Size(min = 2, message = "نام باید بیشتر از دو کاراکتر باشد")
    private String firstName;

    @Size(min = 3, message = "نام خانوادگی باید بیشتر از سه کاراکتر باشد")
    private String lastName;

    @Email
    private String email;

//    @Pattern(regexp = "^(?!(|null|none))$", message = "نوع شخص را مشخص کنید")
    @Pattern(regexp = "^((?!none).)*$", message = "نوع شخص را مشخص کنید")
    private String roleTitle;

    @Min(value = 4, message = "پسورد بیشتر از 4 کاراکتر باشد")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
