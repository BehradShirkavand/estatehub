package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.SellerProfile;
import com.behrad.estatehub.repository.SellerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;

    public Optional<SellerProfile> getOne(Integer id) {
        return sellerProfileRepository.findById(id);
    }

    public SellerProfile addNew(SellerProfile sellerProfile) {
        return sellerProfileRepository.save(sellerProfile);
    }
}
