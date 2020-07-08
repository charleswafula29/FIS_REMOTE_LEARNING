package com.fairmontsintenational.fis_remote_learning.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String pickFirstName(String names){
        List<String> list = new ArrayList<String>(Arrays.asList(names.split(" ")));
        return list.get(0);
    }

    public static String convertCapitalText(String text){
        String str = text.toLowerCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
