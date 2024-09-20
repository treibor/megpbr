package com.megpbr.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.megpbr.audit.Audit;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.UserRepository;
import com.megpbr.data.service.AuditService;
import com.megpbr.data.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }

    @Override
    @Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//stem.out.println(loginform.code.getValue());
		UserLogin user = userRepository.findByUserNameAndEnabled(username, true);
		if (user == null) {
			//audit.saveAudit("Login Failure", username);
			//System.out.println("Login Fail");
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			//audit.saveAudit("Login Success", username);
			return new User(user.getUserName(), user.getHashedPassword(), getAuthorities(user));
			
		}

	}

    private static List<GrantedAuthority> getAuthorities(UserLogin user) {
       return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
    	
    	        .collect(Collectors.toList());
      

    }
    

   

}
