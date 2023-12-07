package ir.maktab.onlineexam.utility;

import ir.maktab.onlineexam.domains.Profile;
import ir.maktab.onlineexam.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SignedInAccountTools {

    private final ProfileService accountService;

    @Autowired
    public SignedInAccountTools(ProfileService accountService) {
        this.accountService = accountService;
    }


    //following two methods copied to account service. find usages of these and refactor project
    public Profile getAccount(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails)principal).getUsername();
        else
            username = principal.toString();
        Profile p = accountService.findProfileByUserName(username);
        return accountService.findProfileByUserName(username);
    }

    public List<String> getStringTitlesOfRoles(){
        return getAccount().getRoles().stream().map(role -> role.getTitle().name()).collect(Collectors.toList());
    }
}