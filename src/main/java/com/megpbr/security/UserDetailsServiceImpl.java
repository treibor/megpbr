package com.megpbr.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
    private final UserRepository userRepository;
    private final AuditRepository auditrepo;
    //private final Audit;
    public UserDetailsServiceImpl(UserRepository userRepository, AuditRepository auditrepo) {
        this.userRepository = userRepository;
        this.auditrepo=auditrepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin user = userRepository.findByUserName(username);
        AuditTrail audit=new AuditTrail();
    	if (user == null) {
        	throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
        	audit.setAction("Login Successful");
        	audit.setActionBy(username);
        	audit.setActionOn(LocalDateTime.now());
        	auditrepo.save(audit);
        	return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getHashedPassword(),
                    getAuthorities(user));
        }
    }

    private static List<GrantedAuthority> getAuthorities(UserLogin user) {
    	 //System.out.println("Role"+user.getRoles().get(0).getRoleName());
    	
       return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
    	        .collect(Collectors.toList());
      

    }

}
