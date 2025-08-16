package com.behrad.estatehub.repository;

import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.BuyerSave;
import com.behrad.estatehub.entity.PropertyPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BuyerSaveRepository extends JpaRepository<BuyerSave, Integer> {

    List<BuyerSave> findByUserId(BuyerProfile userId);

    List<BuyerSave> findByProperty(PropertyPostActivity property);
}
