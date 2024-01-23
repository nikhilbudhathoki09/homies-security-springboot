package homiessecurity.service.impl;


import homiessecurity.entities.Role;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.RoleRepository;
import homiessecurity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepo = roleRepository;
    }


    @Override
    public Role getRoleByTitle(String name) {
        Role role = this.roleRepo.findByTitle(name).orElseThrow(() -> new ResourceNotFoundException("Role", "RoleName", name));
        return role;
    }
    
}
