package com.behrad.estatehub.repository;

import com.behrad.estatehub.entity.BuyerApply;
import com.behrad.estatehub.entity.BuyerProfile;
import com.behrad.estatehub.entity.PropertyPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BuyerApplyRepository extends JpaRepository<BuyerApply, Integer> {

    List<BuyerApply> findByUserId(BuyerProfile userId);

    List<BuyerApply> findByProperty(PropertyPostActivity property);
}
