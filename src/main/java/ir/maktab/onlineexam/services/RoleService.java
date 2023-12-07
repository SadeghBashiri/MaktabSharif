package ir.maktab.onlineexam.services;

import ir.maktab.onlineexam.domains.Role;
import ir.maktab.onlineexam.repositories.RoleRepository;
import ir.maktab.onlineexam.utility.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    public Role findRoleByTitle(RoleTitle roleTitle) {
        return roleRepository.findRoleByTitle(roleTitle);
    }

}
