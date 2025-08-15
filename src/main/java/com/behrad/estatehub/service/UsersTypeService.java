package com.behrad.estatehub.service;

import com.behrad.estatehub.entity.UsersType;
import com.behrad.estatehub.repository.UsersTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersTypeService {

    private final UsersTypeRepository usersTypeRepository;

    public List<UsersType> getAll() {
        return usersTypeRepository.findAll();
    }
}
