package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.Role;
import ir.maktab.onlineexam.dto.RegisterDTO;
import ir.maktab.onlineexam.exeption.ProfileAlreadyExistException;
import ir.maktab.onlineexam.services.PersonService;
import ir.maktab.onlineexam.services.ProfileService;
import ir.maktab.onlineexam.services.RoleService;
import ir.maktab.onlineexam.services.StatusService;
import ir.maktab.onlineexam.utility.PrivilegeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class MainController {

    private final RoleService roleService;
    private final ProfileService profileService;
    private final PersonService personService;
    private final StatusService statusService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(RoleService roleService
            , ProfileService profileService
            , PersonService personService
            , StatusService statusService
            , PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.profileService = profileService;
        this.personService = personService;
        this.statusService = statusService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.GET)
    public String signUp(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("roles", roleService.findAllRole());
        return "signUp-signIn";
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public String signIn(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("roles", roleService.findAllRole());
        return "signUp-signIn";
    }

    @Secured(value = {
            "ROLE_MANAGER_GENERAL_PRIVILEGE",
            "ROLE_TEACHER_GENERAL_PRIVILEGE",
            "ROLE_STUDENT_GENERAL_PRIVILEGE"})
    @RequestMapping(value = "/home")
    public String getMenuPage(Model model) {
        List<String> authoritiesNames = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(authority -> authority.toString()).collect(Collectors.toList());
        List<String> accountPrivilegesPersian = authoritiesNames.stream()
                .map(privilege -> privilege.substring(5, privilege.length()))
                .map(privilege -> PrivilegeTitle.valueOf(privilege).getPersian())
                .collect(Collectors.toList());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails)
            username = ((UserDetails) principal).getUsername();
        else
            username = principal.toString();

        model.addAttribute("statusTitleName", profileService.findProfileByUserName(username).getStatus().getTitle().name());
        model.addAttribute("privilegesNamesPersian", accountPrivilegesPersian);
        model.addAttribute("authoritiesNames", authoritiesNames);
        return "home-page";
    }

    @PostMapping("signUp")
    public String register(@ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO
            , Errors errors
            , Model model
            , RedirectAttributes redirectAttributes) {
        String msg = "";
        if (null != errors && errors.getErrorCount() > 0) {
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            List<Role> roles = roleService.findAllRole();
            model.addAttribute("roles", roles);
            return "signup-signin";
        } else {
            try {
                profileService.saveProfile(registerDTO);
                redirectAttributes.addFlashAttribute("msg", "ثبت نام شما با موفقیت انجام شد");
                return "redirect:/signUp";
            } catch (final ProfileAlreadyExistException profileEx) {
                msg = "ایمیل تکراری است";
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:/signUp";
            }
        }
    }

    @RequestMapping(value = "/signOut")
    public String signOut() {
        return "index";
    }
}
