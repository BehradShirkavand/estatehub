package com.behrad.estatehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PropertyLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank(message = "City name is required.")
    @Size(min = 2, max = 100, message = "City name must be between 2 and 100 characters.")
    private String city;

    @NotBlank(message = "State name is required.")
    @Size(min = 2, max = 100, message = "State name must be between 2 and 100 characters.")
    private String state;

    @NotBlank(message = "Country name is required.")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters.")
    private String country;
}
