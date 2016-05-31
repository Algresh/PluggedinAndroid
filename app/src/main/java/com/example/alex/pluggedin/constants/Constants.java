package com.example.alex.pluggedin.constants;


public  class Constants {

    public static final String MY_TAG = "my_tag";

    public static final String DOMAIN = "http://pluggedin.ru";

    public static final int FIRST_PAGE = 1;

    public static final int UPDATE_PAGE = -1;

    public static final String DATE_FORMAT = "yyyy.MM.dd";

    public static final String URL_IMAGES = "http://pluggedin.ru/images";

    public static final String URL_TEXT_ARTICLE = "http://pluggedin.ru/api/text/article/";

    public static final int TAB_REVIEW = 0;
    public static final int TAB_NEWS = 1;
    public static final int TAB_INTERESTING = 2;
    public static final int TAB_ARTICLE = 3;
    public static final int TAB_MEDIA = 4;

    public static final int TYPE_NEWS = 3;
    public static final int TYPE_INTERESTING = 1;
    public static final int TYPE_ARTICLE = 4;
    public static final int TYPE_MEDIA = 2;

    public static void convertHexStringToInt(String hexStr) {
        System.out.println(Integer.decode(hexStr));
    }


}
