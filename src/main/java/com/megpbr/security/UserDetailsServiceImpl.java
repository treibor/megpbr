package com.megpbr.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
    //ivate final LoginView loginform;
	public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }

    @Override
    @Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//stem.out.println(loginform.code.getValue());
		UserLogin user = userRepository.findByUserNameAndEnabled(username, true);
		if (user == null) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			return new User(user.getUserName(), user.getHashedPassword(), getAuthorities(user));
			
		}

	}

    private static List<GrantedAuthority> getAuthorities(UserLogin user) {
       return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
    	
    	        .collect(Collectors.toList());
      

    }
    

   

}
