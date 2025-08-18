package com.behrad.estatehub.entity;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerPropertiesDto {

    private Long totalCandidates;

    private Integer propertyPostId;

    private String propertyTitle;

    private String propertyPhoto;

    private PropertyLocation propertyLocationId;

    private EstateAgency estateAgencyId;

    @Transient
    public String getPhotosImagePath() {
        if (propertyPhoto == null || propertyPostId == null || propertyPhoto.isEmpty()) {
            return "/images/default-image-property.png";
        }
        return "/photos/property/" + propertyPostId + "/" + propertyPhoto;
    }

}
