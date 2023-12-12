package com.example.demo.service;

import com.example.demo.Entity.AddressEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repo.AddressRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressServiceImpl implements AddressService{
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    @Override
    public AddressEntity getAdress(String userName) {
        return addressRepo.getAddressByUserName(userName);
    }

    @Override
    public void save(AddressEntity address,String userName) {
        UserEntity userEntity = userRepo.findByUsername(userName).get();
        if(address.getId()!=null){
            AddressEntity addressEntity = addressRepo.findById(address.getId()).get();
            if(addressEntity!=null){
                addressEntity.setCity(address.getCity());
                addressEntity.setName(address.getName());
                addressEntity.setCountry(address.getCountry());
                addressEntity.setStreet(address.getStreet());
                addressEntity.setStatus(address.isStatus());
                addressEntity.setPostalCode(address.getPostalCode());
                userEntity.getAddress().add(addressRepo.save(addressEntity));
            }
        }else{
            address.setStatus(true);
            userEntity.getAddress().add(addressRepo.save(address));

        }

    }

    @Override
    public void delete(Long id) {
        AddressEntity addressEntity = addressRepo.findById(id).get();
        if(addressEntity!=null){
            addressEntity.setStatus(false);
            addressRepo.save(addressEntity);
        }
    }
}
