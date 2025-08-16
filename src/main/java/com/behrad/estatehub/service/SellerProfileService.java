package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.SellerProfileRepository;
import com.behrad.estatehub.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UsersRepository usersRepository;

    public Optional<SellerProfile> getOne(Integer id) {
        return sellerProfileRepository.findById(id);
    }

    public SellerProfile addNew(SellerProfile sellerProfile) {
        return sellerProfileRepository.save(sellerProfile);
    }

    public SellerProfile getCurrentSellerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could nit found user with email: " + currentUsername));

            Optional<SellerProfile> sellerProfile = getOne(user.getUserId());
            return sellerProfile.orElse(null);
        } else return null;
    }
}
