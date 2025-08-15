package com.behrad.estatehub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerPropertiesDto {

    private Long totalCandidates;

    private Integer propertyPostId;

    private String propertyTitle;

    private PropertyLocation propertyLocationId;

    private EstateAgency estateAgencyId;

}
