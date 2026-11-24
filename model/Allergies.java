package com.example.oopproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Allergies implements Serializable{
    private List<String> allergies;

    public Allergies() {
        allergies = new ArrayList<>();
        allergies.add("Dairy");
        allergies.add("Nuts");
        allergies.add("Gluten");
    }

    public List<String> getAllergies() {
        return allergies;
    }
}
