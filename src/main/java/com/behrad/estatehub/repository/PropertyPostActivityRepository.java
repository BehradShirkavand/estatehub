package com.behrad.estatehub.repository;

import com.behrad.estatehub.entity.ISellerProperties;
import com.behrad.estatehub.entity.PropertyPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PropertyPostActivityRepository extends JpaRepository<PropertyPostActivity, Integer> {

    @Query(value = " SELECT COUNT(s.user_id) as totalCandidates, p.property_post_id, p.property_title, " +
            " p.property_photo as property_photo, " +
            " l.id as locationId, l.city, l.state, l.country, e.id as agencyId, e.name FROM property_post_activity p " +
            " inner join property_location l " +
            " on p.property_location_id = l.id " +
            " INNER join estate_agency e  " +
            " on p.estate_agency_id = e.id " +
            " left join buyer_apply s " +
            " on s.property = p.property_post_id " +
            " where p.posted_by_id = :seller " +
            " GROUP By p.property_post_id, p.property_photo" ,nativeQuery = true)
    List<ISellerProperties> getSellerProperties(@Param("seller") int seller);

    @Query(value = "SELECT * FROM property_post_activity p INNER JOIN property_location l ON p.property_location_id = l.id WHERE p" +
            ".property_title LIKE CONCAT('%', :property, '%')" +
            " AND (l.city LIKE CONCAT('%', :location, '%')" +
            " OR l.country LIKE CONCAT('%', :location, '%')" +
            " OR l.state LIKE CONCAT('%', :location, '%'))" +
            " AND (p.property_type IN(:propertyType))" +
            " AND (p.listing_type IN(:listingType))", nativeQuery = true)
    List<PropertyPostActivity> searchWithoutDate(
            @Param("property") String property,
            @Param("location") String location,
            @Param("propertyType") List<String> propertyType,
            @Param("listingType") List<String> listingType);

    @Query(value = "SELECT * FROM property_post_activity p INNER JOIN property_location l ON p.property_location_id = l.id WHERE p" +
            ".property_title LIKE CONCAT('%', :property, '%')" +
            " AND (l.city LIKE CONCAT('%', :location, '%')" +
            " OR l.country LIKE CONCAT('%', :location, '%')" +
            " OR l.state LIKE CONCAT('%', :location, '%'))" +
            " AND (p.property_type IN(:propertyType))" +
            " AND (p.listing_type IN(:listingType))" +
            " AND (p.posted_date >= :date)", nativeQuery = true)
    List<PropertyPostActivity> search(
            @Param("property") String property,
            @Param("location") String location,
            @Param("propertyType") List<String> propertyType,
            @Param("listingType") List<String> listingType,
            @Param("date") LocalDate searchDate);

}
