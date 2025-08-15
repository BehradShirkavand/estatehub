package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.BuyerProfileRepository;
import com.behrad.estatehub.repository.SellerProfileRepository;
import com.behrad.estatehub.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final BuyerProfileRepository buyerProfileRepository;
    private final PasswordEncoder passwordEncoder;


    public Users addNew(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);

        if (users.getUserTypeId().getUserTypeId() == 1) {
            sellerProfileRepository.save(new SellerProfile(savedUser));
        } else {
            buyerProfileRepository.save(new BuyerProfile(savedUser));
        }

        return savedUser;
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users user = usersRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not found user with email: " + username));
            int userId = user.getUserId();

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Seller"))) {
                return sellerProfileRepository.findById(userId).orElse(new SellerProfile());
            } else {
                return buyerProfileRepository.findById(userId).orElse(new BuyerProfile());
            }
        }
        return null;
    }
}
