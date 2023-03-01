package com.example.a1a22ikeraadefinitiva;

public class Tools {

    public Tools(){}


    public String formatPath(String externarDir , String customPath){
        if (customPath.startsWith("/"))  customPath = customPath.substring(1);
        if (customPath.endsWith("/")) customPath = customPath.substring(0 , (customPath.length()-1));
        return externarDir + "/" + customPath + "/";
    }
}
