package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.entity.Users;
import com.behrad.estatehub.repository.BuyerProfileRepository;
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
public class BuyerProfileService {

    private final BuyerProfileRepository buyerProfileRepository;
    private final UsersRepository usersRepository;

    public Optional<BuyerProfile> getOne(Integer id) {
        return buyerProfileRepository.findById(id);
    }

    public BuyerProfile addNew(BuyerProfile buyerProfile) {
        return buyerProfileRepository.save(buyerProfile);
    }

    public BuyerProfile getCurrentBuyerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not found user with email: " + currentUsername));

            Optional<BuyerProfile> buyerProfile = getOne(user.getUserId());
            return buyerProfile.orElse(null);
        } else return null;
    }
}
