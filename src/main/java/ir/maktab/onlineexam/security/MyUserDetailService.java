package ir.maktab.onlineexam.security;

import ir.maktab.onlineexam.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class MyUserDetailService implements UserDetailsService {

    private ProfileService profileService;

    @Autowired
    public MyUserDetailService(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new MyUserDetail(profileService.findProfileByUserName(userName));
    }
}
