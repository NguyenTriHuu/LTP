package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequet<T> {
    private T id;
    private String question;
    private String solution;
    private String type;
    private List<ChoiceRequest<T>> choice = new ArrayList<>();
}
