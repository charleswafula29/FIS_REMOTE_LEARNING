package com.fis.fis_remote_learning.models;

import java.util.List;

public class LevelsResponseModel {
    private List<LevelsModel> Classes;

    public LevelsResponseModel(List<LevelsModel> classes) {
        Classes = classes;
    }

    public List<LevelsModel> getClasses() {
        return Classes;
    }

    @Override
    public String toString() {
        return "LevelsResponseModel{" +
                "Classes=" + Classes +
                '}';
    }
}
