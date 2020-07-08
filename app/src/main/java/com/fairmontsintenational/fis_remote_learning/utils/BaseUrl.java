package com.fairmontsintenational.fis_remote_learning.utils;

import io.paperdb.Paper;

public class BaseUrl {
    public static final String GET_MAIN_URL = "https://www.fairmontsinternationalschool.co.ke/fairmontsAPI/url.php";
    private static String ss = Paper.book().read("Main_url").toString();
}
