package homiessecurity.service;


import homiessecurity.entities.Role;

public interface RoleService {

    Role getRoleByTitle(String name);
    
}
