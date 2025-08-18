package com.behrad.estatehub.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PropertyPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propertyPostId;

    @NotNull(message = "Property must be associated with a seller.")
    @ManyToOne
    @JoinColumn(name = "postedById", referencedColumnName = "userId")
    private Users postedById;

    @NotNull(message = "Property location cannot be empty.")
    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "propertyLocationId", referencedColumnName = "Id")
    private PropertyLocation propertyLocationId;

    @NotNull(message = "Estate agency cannot be empty.")
    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estateAgencyId", referencedColumnName = "Id")
    private EstateAgency estateAgencyId;

    @Transient
    private Boolean isActive;

    @Transient
    private Boolean isSaved;

    @Size(max = 10000, message = "Description is too long.")
    private String descriptionOfProperty;

    @NotBlank(message = "Please select a property type.")
    private String propertyType;

    @NotNull(message = "Price is required.")
    @Digits(integer = 12, fraction = 2, message = "Invalid price format.")
    @Positive(message = "Price must be a positive number.")
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @NotBlank(message = "Property title cannot be empty.")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters.")
    private String propertyTitle;

    @NotBlank(message = "Please select a listing type.")
    private String listingType;

    @Column(nullable = true, length = 264)
    private String propertyPhoto;

    @Transient
    public String getPhotosImagePath() {
        if (propertyPhoto == null || propertyPostId == null || propertyPhoto.isEmpty()) {
            return "/images/default-image-property.png";
        }
        return "/photos/property/" + propertyPostId + "/" + propertyPhoto;
    }
}