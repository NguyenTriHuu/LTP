package com.example.demo.repo;

import com.example.demo.Entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
    @Query(value = "select address.id , address.name, address.city, address.country,address.postal_code, address.status, address.street from user join address on user.id = address.user_id where user.username =?1 and address.status=1" ,nativeQuery = true)
    AddressEntity getAddressByUserName(String userName);

}
