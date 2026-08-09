package com.flowforge.auth.security;

import com.flowforge.auth.entity.User;
import com.flowforge.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Given a username, load that user's information from the database and return it as UserDetails
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public  CustomUserDetailsService( UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    //by default method have UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"+username));

        return new CustomUserDetails(user);
    }
}
