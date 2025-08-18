package com.behrad.estatehub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seller_profile")
public class SellerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String firstName;

    private String lastName;

    private String city;

    private String state;

    private String country;

    private String agency;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    public SellerProfile(Users users) {
        this.userId = users;
    }

    @Transient
    public String getPhotosImagePath() {

        if (profilePhoto == null || userAccountId ==null || profilePhoto.isEmpty()) {
            return "/images/default-image-profile.png";
        }
        return "/photos/seller/" + userAccountId + "/" + profilePhoto;
    }
}
