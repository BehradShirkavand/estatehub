package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.BuyerSave;
import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.repository.BuyerSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerSaveService {

    private final BuyerSaveRepository buyerSaveRepository;

    public List<BuyerSave> getCandidatesProperties(BuyerProfile userAccountId) {
        return buyerSaveRepository.findByUserId(userAccountId);
    }

    public List<BuyerSave> getPropertyCandidates(PropertyPostActivity property) {
        return buyerSaveRepository.findByProperty(property);
    }

    public void addNew(BuyerSave buyerSave) {
        buyerSaveRepository.save(buyerSave);
    }

    public void deleteByUserByProperty(int userId, PropertyPostActivity propertyPostActivity) {
        List<BuyerSave> buyerSaveList = buyerSaveRepository.findByProperty(propertyPostActivity);

        for (BuyerSave buyerSave:buyerSaveList) {
            if (buyerSave.getUserId().getUserAccountId() == userId) {
                buyerSaveRepository.delete(buyerSave);
            }
        }
    }
}
