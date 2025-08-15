package com.behrad.estatehub.repository;

import com.behrad.estatehub.entity.ISellerProperties;
import com.behrad.estatehub.entity.PropertyPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyPostActivityRepository extends JpaRepository<PropertyPostActivity, Integer> {

    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates,p.property_post_id,p.property_title,l.id as locationId,l.city,l.state,l.country,e.id as agencyId,e.name FROM property_post_activity p " +
            " inner join property_location l " +
            " on p.property_location_id = l.id " +
            " INNER join estate_agency e  " +
            " on p.estate_agency_id = e.id " +
            " left join buyer_apply s " +
            " on s.property = p.property_post_id " +
            " where p.posted_by_id = :seller " +
            " GROUP By p.property_post_id" ,nativeQuery = true)
    List<ISellerProperties> getSellerProperties(@Param("seller") int seller);
}
