package com.example.demo.api.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Entity.AddressEntity;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.cluster.metadata.AliasAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class AddressApi {
    private final AddressService addressService;

    @GetMapping(value = "/user/{userName}/address")
    public ResponseEntity<AddressEntity> getAddress(@PathVariable("userName") String userName){
        return ResponseEntity.ok().body(addressService.getAdress(userName));
    }

    @PostMapping(value = "/user/address/save")
    public void Save(@RequestHeader(AUTHORIZATION) String authorizationHeader ,@RequestBody AddressEntity address){
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm= Algorithm.HMAC256("secrect".getBytes());
        JWTVerifier verifier= JWT.require(algorithm).build();
        DecodedJWT decodedJWT= verifier.verify(token);
        String username=decodedJWT.getSubject();
        addressService.save(address,username);
    }

    @DeleteMapping(value = "/user/address/{id}/delete")
    public void delete(@PathVariable("id") Long id){
        addressService.delete(id);
    }
}
