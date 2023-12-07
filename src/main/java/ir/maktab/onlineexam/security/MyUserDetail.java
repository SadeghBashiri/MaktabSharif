package ir.maktab.onlineexam.security;

import ir.maktab.onlineexam.domains.*;
import ir.maktab.onlineexam.utility.StatusTitle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetail implements UserDetails {

    private String username;
    private String password;
    private List<Role> roles;
    private Status status;
    private Person person;

    public MyUserDetail() {
    }

    public MyUserDetail(Profile profile) {
        this.username = profile.getUserName();
        this.password = profile.getPassword();
        this.status = profile.getStatus();
        this.roles = profile.getRoles();
        this.person = profile.getPerson();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role roleItem : roles){
            for (Privilege privilegeItem : roleItem.getPrivileges())
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + privilegeItem.getTitle().name()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (status.getTitle().equals(StatusTitle.ACTIVE)?true:false);
//        return true;
    }
}
