package com.example.demo.service;

import com.example.demo.Entity.AddressEntity;

public interface AddressService {
    AddressEntity getAdress (String userName);

    void save (AddressEntity address,String userName);

    void delete(Long id);
}
