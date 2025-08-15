package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.UsersRepository;
import com.behrad.estatehub.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user with email: "+ username));

        return new CustomUserDetails(user);
    }
}
