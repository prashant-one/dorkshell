package com.prashant.javashell.utils;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

@Component
public class Utils {

    public String convertToString(List<String> listData){
        StringBuilder stringBuilder=new StringBuilder();
        for(String link:listData){
            stringBuilder.append(link);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }
    public String convertToString(Set<String> listData){
        StringBuilder stringBuilder=new StringBuilder();
        for(String link:listData){
            stringBuilder.append(link);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }

    public String detectOS(){
       return System.getProperty("os.name");
    }

    public boolean isValidURL(String url) throws MalformedURLException, URISyntaxException {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    public String getDomain(String url) {
        try {
            return new URI(url).getHost();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
