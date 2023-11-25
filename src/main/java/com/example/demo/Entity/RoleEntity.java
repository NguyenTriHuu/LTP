package com.example.demo.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
@Entity
@Table(name ="role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity extends BaseEntity {
    private String name;
}
