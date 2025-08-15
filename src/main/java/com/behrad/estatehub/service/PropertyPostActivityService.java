package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.PropertyPostActivity;
import com.behrad.estatehub.repository.PropertyPostActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyPostActivityService {

    private final PropertyPostActivityRepository propertyPostActivityRepository;

    public PropertyPostActivity addNew(PropertyPostActivity propertyPostActivity) {
        return propertyPostActivityRepository.save(propertyPostActivity);
    }
}
