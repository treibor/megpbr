package com.megpbr.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.UserLoginLevel;
import com.megpbr.data.entity.UserRole;
import com.megpbr.data.repository.UserLevelRepository;
import com.megpbr.data.repository.UserRepository;
import com.megpbr.data.repository.UserRolesRpository;
import com.megpbr.security.SecurityService;

@Service
public class UserService {
	@Autowired
	SecurityService sservice;
	@Autowired
	UserLevelRepository levelrepo;
	@Autowired
	UserRolesRpository rolesrepository;
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public List<UserLoginLevel> getUserLevels(long a){
    	return levelrepo.findByLevelGreaterThan(a);
    }
    
    public UserDetails getUser() {
    	return sservice.getAuthenticatedUser();
    }
    
    public String getLoggedUserName() {
    	return getUser().getUsername();
    }
    
    public UserLogin getLoggedUser() {
    	return repository.findByUserName(getLoggedUserName());
    }
    public String getLoggedUserLevel() {
    	return getLoggedUser().getLevel().getLevelName();
    }
    public boolean isSuperAdmin() {
    	String level= repository.findByUserName(getLoggedUserName()).getLevel().getLevelName();
    	if(level.startsWith("SUPER")) {
    		return true;
    	}else {
    		return false;
    	}
    }
    public boolean isStateAdmin() {
    	String level= repository.findByUserName(getLoggedUserName()).getLevel().getLevelName();
    	if(level.startsWith("STATE")&& level.endsWith("ADMIN")) {
    		return true;
    	}else {
    		return false;
    	}
    }
    public boolean isDistrictAdmin() {
    	String level= repository.findByUserName(getLoggedUserName()).getLevel().getLevelName();
    	if(level.startsWith("DISTRICT")&& level.endsWith("ADMIN")) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    
    public Optional<UserLogin> get(long id) {
        return repository.findById(id);
    }
    public UserLogin getUserByName(String username) {
        return repository.findByUserName(username);
    }
    public UserLogin update(UserLogin entity) {
        return repository.save(entity);
    }
    public UserRole update(UserRole entity) {
        return rolesrepository.save(entity);
    }
    public void delete(long id) {
        repository.deleteById(id);
    }

    public Page<UserLogin> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

  
    public int count() {
        return (int) repository.count();
    }

}
