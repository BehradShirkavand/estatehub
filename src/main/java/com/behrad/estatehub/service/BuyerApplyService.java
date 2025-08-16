package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.BuyerApply;
import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.repository.BuyerApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerApplyService {

    private final BuyerApplyRepository buyerApplyRepository;

    public List<BuyerApply> getCandidatesProperties(BuyerProfile userAccountId) {
        return buyerApplyRepository.findByUserId(userAccountId);
    }

    public List<BuyerApply> getPropertyCandidates(PropertyPostActivity property) {
        return buyerApplyRepository.findByProperty(property);
    }

}
