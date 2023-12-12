package com.example.demo.SlopeOne;

import com.example.demo.Entity.CourseEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map.Entry;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SlopeOne {

    private static Map<Item, Map<Item, Double>> diff = new HashMap<>();
    private static Map<Item, Map<Item, Integer>> freq = new HashMap<>();
    private static Map<User, HashMap<Item, Double>> data;
    private static Map<User, HashMap<Item, Double>> outputData = new HashMap<>();

    private final InputData inputData;
    private final CourseRepo courseRepo;

    public List<CourseEntity> slopeOne(UserEntity user) {
        data = inputData.initializeData(user);
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(data, user);
        System.out.println("\nSlope One - With Predictions\n");
        predict(data, user);
        User user1 = new User("User " + user.getId());

        if (outputData.containsKey(user1)) {
            List<CourseEntity> listCourse = new ArrayList<>();
            LinkedHashMap<Item, Double> listItem = HashMapSorting(outputData.get(user1));
            Set<Entry<Item, Double>> sortedEntries = listItem.entrySet();
            int i = 0;
            for (Entry<Item, Double> mapping : sortedEntries) {
                if (i == 3) return listCourse;
                listCourse.add(courseRepo.findById((long) Integer.parseInt(mapping.getKey().getItemName())).get());
                i++;
            }
        }
        return null;
    }


    private static void buildDifferencesMatrix(Map<User, HashMap<Item, Double>> data, UserEntity userEntity) {
        for (HashMap<Item, Double> user : data.values()) {
            for (Map.Entry<Item, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Item, Double>());
                    freq.put(e.getKey(), new HashMap<Item, Integer>());
                }
                for (Map.Entry<Item, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Item j : diff.keySet()) {
            for (Item i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data, userEntity);
    }


    private static void predict(Map<User, HashMap<Item, Double>> data, UserEntity userEntity) {
        HashMap<Item, Double> uPred = new HashMap<Item, Double>();
        HashMap<Item, Integer> uFreq = new HashMap<Item, Integer>();
        for (Item j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Map.Entry<User, HashMap<Item, Double>> e : data.entrySet()) {
            for (Item j : e.getValue().keySet()) {
                for (Item k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Item, Double> clean = new HashMap<Item, Double>();
            for (Item j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (Item j : InputData.items) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
        printData(outputData, userEntity);
    }

    private static void printData(Map<User, HashMap<Item, Double>> data, UserEntity userEntity) {
        User user = new User("User " + userEntity.getId());
        if (data.containsKey(user)) {
            System.out.println(user.getUsername() + ":");
            print(data.get(user));
        } else {
            System.out.println("Not Found : " + user.getUsername() + ":");
        }
    }

    private static void print(HashMap<Item, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Item j : hashMap.keySet()) {
            System.out.println(" " + j.getItemName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

    private static LinkedHashMap<Item, Double> HashMapSorting(HashMap<Item, Double> map) {
        Set<Entry<Item, Double>> entries = map.entrySet();

        System.out.println("----- Before sorting, random order -----");
        for (Entry<Item, Double> entry : entries) {
            System.out.println(entry.getKey().getItemName() + " ==>> " + entry.getValue().doubleValue());
        }

        // Tạo custom Comparator
        Comparator<Entry<Item, Double>> comparator = new Comparator<Entry<Item, Double>>() {
            @Override
            public int compare(Entry<Item, Double> e1, Entry<Item, Double> e2) {
                double v1 = e1.getValue().doubleValue();
                double v2 = e2.getValue().doubleValue();
                if (v1 > v2) {

                    return -1;
                } else if (v1 < v2) {

                    return 1;
                } else {

                    return 0;
                }
            }


        };

        // Convert Set thành List
        List<Entry<Item, Double>> listEntries = new ArrayList<>(entries);

        // Sắp xếp List
        Collections.sort(listEntries, comparator);

        // Tạo một LinkedHashMap và put các entry từ List đã sắp xếp sang
        LinkedHashMap<Item, Double> sortedMap = new LinkedHashMap<>(listEntries.size());
        for (Entry<Item, Double> entry : listEntries) {
            sortedMap.put(entry.getKey(), entry.getValue().doubleValue());
        }

        System.out.println("----- After sorting by values -----");
        Set<Entry<Item, Double>> sortedEntries = sortedMap.entrySet();
        for (Entry<Item, Double> mapping : sortedEntries) {
            System.out.println(mapping.getKey().getItemName() + " ==>> " + mapping.getValue().doubleValue());
        }
        return sortedMap;
    }

}
