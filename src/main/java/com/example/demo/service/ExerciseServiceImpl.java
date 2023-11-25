package com.example.demo.service;

import com.example.demo.Entity.AnswerEntity;
import com.example.demo.Entity.ChoiceEntity;
import com.example.demo.Entity.ExerciseEntity;
import com.example.demo.Entity.LectureEntity;
import com.example.demo.dto.ChoiceRequest;
import com.example.demo.dto.ExerciseRequet;
import com.example.demo.repo.AnswerRepo;
import com.example.demo.repo.ChoiceRepo;
import com.example.demo.repo.ExerciseRepo;
import com.example.demo.repo.LectureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseServiceImpl implements ExerciseService{
    private final LectureRepo lectureRepo;
    private final ExerciseRepo exerciseRepo;
    private final ChoiceRepo choiceRepo;
    private final AnswerRepo answerRepo;
    @Override
    public void save(Long idLecture, List<ExerciseRequet<String>> exerciseRequet) {
        LectureEntity lectureEntity = lectureRepo.findById(idLecture).get();
        List<String > ids = new ArrayList<>();
        List<ExerciseEntity> exerciseEntities = new ArrayList<>();
        for(ExerciseRequet<String> exercise: exerciseRequet){
            ids.add(exercise.getId());
        }
        if(lectureEntity.getExercises().size()!=0){
            for(ExerciseEntity exerciseEntity:lectureEntity.getExercises()){
                //Delete
                if(!ids.contains( String.valueOf(exerciseEntity.getId()))){
                    exerciseRepo.deleteByChoice(exerciseEntity.getId());
                    exerciseRepo.deleteByAnswer(exerciseEntity.getId());
                    exerciseRepo.deleteByLecture(exerciseEntity.getId());
                    exerciseRepo.deleteById(exerciseEntity.getId());

                }else{
                    if(ids.contains( String.valueOf(exerciseEntity.getId()))){
                        ExerciseRequet<String> requet = new ExerciseRequet<String>();
                        for(ExerciseRequet<String> exerciseRequet1: exerciseRequet){
                            if(String.valueOf(exerciseEntity.getId()).equals(exerciseRequet1.getId())){
                                requet =exerciseRequet1;
                            }
                        }
                        exerciseEntity.setType(requet.getType());
                        exerciseEntity.setContent(requet.getQuestion());
                        exerciseEntity.setSolution(requet.getQuestion());

                        //Delete all anwser
                        answerRepo.deleteALLByIdEx(exerciseEntity.getId());
                        List<AnswerEntity> newLisstAnswerEntities = new ArrayList<>();


                        List<String > idChois = new ArrayList<>();
                        for(ChoiceRequest<String> choiceRequest: requet.getChoice()){
                            if(choiceRequest.getContent()!=null){
                                idChois.add(choiceRequest.getId());
                            }
                        }

                        List<ChoiceEntity> newListChoiceEntity =new ArrayList<>();
                        //Update Choice And Delete
                        for(ChoiceEntity choiceEntity: exerciseEntity.getChoices()){
                            if(!idChois.contains(String.valueOf(choiceEntity.getId()))){
                                choiceRepo.deleteByAnswer(choiceEntity.getId());
                                choiceRepo.deleteById(choiceEntity.getId());
                                idChois.remove(String.valueOf(exerciseEntity.getId()));
                            }
                            if(idChois.contains(String.valueOf(choiceEntity.getId()))){
                                for(ChoiceRequest<String> choiceRequest1: requet.getChoice()){
                                    if(String.valueOf(choiceEntity.getId()).equals(choiceRequest1.getId()) ){
                                        choiceEntity.setContent(choiceRequest1.getContent());
                                        ChoiceEntity choiceSave = choiceRepo.save(choiceEntity);
                                        newListChoiceEntity.add(choiceSave);
                                        if(choiceRequest1.getRight()!=null && choiceRequest1.getRight()==true){
                                            AnswerEntity answerEntity = new AnswerEntity();
                                            answerEntity.setChoice(choiceSave);
                                            AnswerEntity answer= answerRepo.save(answerEntity);
                                            newLisstAnswerEntities.add(answer);
                                        }
                                        idChois.remove(choiceRequest1.getId());
                                        break;
                                    }
                                }
                            }
                        }
                        // Add new Choice
                        for(ChoiceRequest<String> choiceRequest : requet.getChoice()){
                            if(idChois.contains(choiceRequest.getId())){
                                ChoiceEntity choiceEntity = new ChoiceEntity();
                                choiceEntity.setContent(choiceEntity.getContent());
                                ChoiceEntity choiceSave = choiceRepo.save(choiceEntity);
                                if(choiceRequest.getRight()!=null && choiceRequest.getRight()==true){
                                    AnswerEntity answerEntity = new AnswerEntity();
                                    answerEntity.setChoice(choiceSave);
                                    AnswerEntity answer= answerRepo.save(answerEntity);
                                    newLisstAnswerEntities.add(answer);
                                }
                                newListChoiceEntity.add(choiceSave);
                            }
                        }
                        exerciseEntity.setChoices(newListChoiceEntity);
                        exerciseEntity.setAnswers(newLisstAnswerEntities);

                    }
                    exerciseEntities.add(exerciseRepo.save(exerciseEntity));
                    ids.remove(String.valueOf(exerciseEntity.getId()));
                }

                // Update


            }
            for(ExerciseRequet<String> exerciseRequet1: exerciseRequet){
                if(ids.contains(exerciseRequet1.getId())){
                    ExerciseEntity exerciseEntity = new ExerciseEntity();
                    exerciseEntity.setContent(exerciseRequet1.getQuestion());
                    exerciseEntity.setType(exerciseRequet1.getType());
                    exerciseEntity.setSolution(exerciseRequet1.getSolution());
                    List<ChoiceEntity> choiceEntityList =new ArrayList<>();
                    List<AnswerEntity> answerEntities =new ArrayList<>();
                    for(ChoiceRequest<String> choiceRequest: exerciseRequet1.getChoice()){
                        if(choiceRequest.getContent()!=null){
                            ChoiceEntity choiceEntity =new ChoiceEntity();
                            choiceEntity.setContent(choiceRequest.getContent());
                            ChoiceEntity choice= choiceRepo.save(choiceEntity);
                            choiceEntityList.add(choice);
                            if(choiceRequest.getRight()!=null && choiceRequest.getRight()==true){
                                AnswerEntity answer =new AnswerEntity();
                                answer.setChoice(choice);
                                AnswerEntity answerEntity= answerRepo.save(answer);
                                answerEntities.add(answerEntity);
                            }
                        }
                    }
                    exerciseEntity.setChoices(choiceEntityList);
                    exerciseEntity.setAnswers(answerEntities);
                    exerciseEntities.add(exerciseRepo.save(exerciseEntity));
                }

            }
        }else{
            for(ExerciseRequet<String> exerciseRequet1: exerciseRequet){
                ExerciseEntity exerciseEntity = new ExerciseEntity();
                exerciseEntity.setContent(exerciseRequet1.getQuestion());
                exerciseEntity.setType(exerciseRequet1.getType());
                exerciseEntity.setSolution(exerciseRequet1.getSolution());
                List<ChoiceEntity> choiceEntityList =new ArrayList<>();
                List<AnswerEntity> answerEntities =new ArrayList<>();
                for(ChoiceRequest<String> choiceRequest: exerciseRequet1.getChoice()){
                    if(choiceRequest.getContent()!=null){
                        ChoiceEntity choiceEntity =new ChoiceEntity();
                        choiceEntity.setContent(choiceRequest.getContent());
                        ChoiceEntity choice= choiceRepo.save(choiceEntity);
                        choiceEntityList.add(choice);
                        if(choiceRequest.getRight()!=null && choiceRequest.getRight()==true){
                            AnswerEntity answer =new AnswerEntity();
                            answer.setChoice(choice);
                            AnswerEntity answerEntity= answerRepo.save(answer);
                            answerEntities.add(answerEntity);
                        }
                    }
                }
                exerciseEntity.setChoices(choiceEntityList);
                exerciseEntity.setAnswers(answerEntities);
               exerciseEntities.add(exerciseRepo.save(exerciseEntity)) ;
            }
        }

        lectureEntity.setExercises(exerciseEntities);
        lectureRepo.save(lectureEntity);
        //Delete
    }

    @Override
    public List<ExerciseRequet<Long>> getExercises(Long idLecture) {
        LectureEntity lectureEntity = lectureRepo.findById(idLecture).get();
        List<ExerciseRequet<Long>> listReponse = new ArrayList<>();
        for(ExerciseEntity exerciseEntity: lectureEntity.getExercises()){
            ExerciseRequet<Long> exerciseRequet = new ExerciseRequet<Long>();
            exerciseRequet.setId(exerciseEntity.getId());
            exerciseRequet.setQuestion(exerciseEntity.getContent());
            exerciseRequet.setSolution(exerciseEntity.getSolution());
            exerciseRequet.setType(exerciseEntity.getType());

           List<ChoiceRequest<Long>>  listChoiceRequest = new ArrayList<>();
            for(ChoiceEntity choiceEntity : exerciseEntity.getChoices()){
                ChoiceRequest<Long> choiceRequest =new ChoiceRequest<Long>();

                choiceRequest.setId(choiceEntity.getId());
                choiceRequest.setContent(choiceEntity.getContent());
                int check = answerRepo.ChoiceIsCorrect(choiceEntity.getId());
                if(check>0){
                    choiceRequest.setRight(true);
                }
                listChoiceRequest.add(choiceRequest);
            }
            exerciseRequet.setChoice(listChoiceRequest);
            listReponse.add(exerciseRequet);
        }
        return listReponse;
    }

    @Override
    public List<ExerciseRequet<Long>> getExercisesNoAnswer(Long idLecture) {
        LectureEntity lectureEntity = lectureRepo.findById(idLecture).get();
        List<ExerciseRequet<Long>> listReponse = new ArrayList<>();
        for(ExerciseEntity exerciseEntity: lectureEntity.getExercises()){
            ExerciseRequet<Long> exerciseRequet = new ExerciseRequet<Long>();
            exerciseRequet.setId(exerciseEntity.getId());
            exerciseRequet.setQuestion(exerciseEntity.getContent());
            exerciseRequet.setSolution(exerciseEntity.getSolution());
            exerciseRequet.setType(exerciseEntity.getType());

            List<ChoiceRequest<Long>>  listChoiceRequest = new ArrayList<>();
            for(ChoiceEntity choiceEntity : exerciseEntity.getChoices()){
                ChoiceRequest<Long> choiceRequest =new ChoiceRequest<Long>();

                choiceRequest.setId(choiceEntity.getId());
                choiceRequest.setContent(choiceEntity.getContent());
                choiceRequest.setRight(false);
                listChoiceRequest.add(choiceRequest);
            }
            exerciseRequet.setChoice(listChoiceRequest);
            listReponse.add(exerciseRequet);
        }
        return listReponse;
    }
}
