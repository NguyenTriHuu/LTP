package com.example.demo.service;

import com.example.demo.dto.ChoiceRequest;
import com.example.demo.dto.ExerciseRequet;

import java.util.List;

public interface ExerciseService {
    void save (Long idLecture , List<ExerciseRequet<String>> exerciseRequet);

    List<ExerciseRequet<Long>> getExercises (Long idLecture);
    List<ExerciseRequet<Long>> getExercisesNoAnswer (Long idLecture);
}
