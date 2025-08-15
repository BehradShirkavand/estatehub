package com.behrad.estatehub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PropertyPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propertyPostId;

    @ManyToOne
    @JoinColumn(name = "postedById", referencedColumnName = "userId")
    private Users postedById;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "propertyLocationId", referencedColumnName = "Id")
    private PropertyLocation propertyLocationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estateAgencyId", referencedColumnName = "Id")
    private EstateAgency estateAgencyId;

    @Transient
    private Boolean isActive;

    @Transient
    private Boolean isSaved;

    @Length(max = 10000)
    private String descriptionOfProperty;

    private String propertyType;

    private String price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postedDate;

    private String propertyTitle;

    private String listingType;
}
