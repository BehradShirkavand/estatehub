package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.repository.BuyerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BuyerProfileService {

    private final BuyerProfileRepository buyerProfileRepository;

    public Optional<BuyerProfile> getOne(Integer id) {
        return buyerProfileRepository.findById(id);
    }

    public BuyerProfile addNew(BuyerProfile buyerProfile) {
        return buyerProfileRepository.save(buyerProfile);
    }
}
