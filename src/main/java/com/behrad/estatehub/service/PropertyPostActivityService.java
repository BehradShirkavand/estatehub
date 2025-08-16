package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.*;
import com.behrad.estatehub.repository.PropertyPostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PropertyPostActivityService {

    private final PropertyPostActivityRepository propertyPostActivityRepository;

    public PropertyPostActivity addNew(PropertyPostActivity propertyPostActivity) {
        return propertyPostActivityRepository.save(propertyPostActivity);
    }

    public List<SellerPropertiesDto> getSellerProperties(int seller) {

        List<ISellerProperties> sellerPropertiesDtos = propertyPostActivityRepository.getSellerProperties(seller);
        List<SellerPropertiesDto> sellerPropertiesDtoList = new ArrayList<>();

        for (ISellerProperties sel : sellerPropertiesDtos) {

            PropertyLocation loc = new PropertyLocation(
                    sel.getLocationId(),
                    sel.getCity(),
                    sel.getState(),
                    sel.getCountry()
            );
            EstateAgency est = new EstateAgency(
                    sel.getAgencyId(),
                    sel.getName(),
                    ""
            );
            sellerPropertiesDtoList.add(new SellerPropertiesDto(
                    sel.getTotalCandidates(),
                    sel.getProperty_post_id(),
                    sel.getProperty_title(),
                    loc,
                    est)
            );
        }

        return sellerPropertiesDtoList;
    }

    public PropertyPostActivity getOne(int id) {
        return propertyPostActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public List<PropertyPostActivity> getAll() {
        return propertyPostActivityRepository.findAll();
    }

    public List<PropertyPostActivity> search(String property, String location, List<String> propertyType, List<String> listingType, LocalDate searchDate) {

//        List<String> cleanPropertyType = propertyType.stream()
//                .filter(Objects::nonNull) // Remove all null elements
//                .toList();
//        List<String> cleanListingType = listingType.stream()
//                .filter(Objects::nonNull) // Remove all null elements
//                .toList();

        return Objects.isNull(searchDate)?
                propertyPostActivityRepository
                        .searchWithoutDate(property, location, propertyType, listingType) :
                propertyPostActivityRepository
                        .search(property, location, propertyType, propertyType, searchDate);
    }
}
