package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity extends BaseEntity{
    private String name;
}
