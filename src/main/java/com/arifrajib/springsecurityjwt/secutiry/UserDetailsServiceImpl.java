package com.arifrajib.springsecurityjwt.secutiry;

import com.arifrajib.springsecurityjwt.entity.User;
import com.arifrajib.springsecurityjwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with email "+email));

        return new org.springframework.security.core.userdetails
                .User(email, user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
