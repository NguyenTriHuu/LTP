package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="choice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceEntity extends BaseEntity{
    private String content;
}
