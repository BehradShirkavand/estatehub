package com.behrad.estatehub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buyer_profile")
public class BuyerProfile {

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

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    public BuyerProfile(Users users) {
        this.userId = users;
    }

    @Transient
    public String getPhotosImagePath() {

        if (profilePhoto == null || userAccountId == null) {
            return null;
        }
        return "/photos/buyer/" + userAccountId + "/" + profilePhoto;
    }
}
