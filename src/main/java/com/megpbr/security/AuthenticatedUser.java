package com.megpbr.security;


import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final AuditRepository auditrepo;
    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository, AuditRepository auditrepo) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
        this.auditrepo=auditrepo;
    }

    @Transactional
    public Optional<UserLogin> get() {
    	//System.out.println("Test2");
    	return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUserName(userDetails.getUsername()));
        
    }

    public void logout() {
    	
        authenticationContext.logout();
    }

}
