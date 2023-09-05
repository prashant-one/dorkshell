package com.prashant.javashell.utils;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Set<String> filterImages(Document doc){
        Elements elements = doc.getAllElements().select("img[src]");
        return elements.stream().map(it -> it.attr("src"))
                .filter(it -> it.startsWith("http"))
                .filter(it->it.contains(".jpg")
                        || it.contains(".WEBP")
                        || it.contains(".JPG")
                        || it.contains("JPEG")
                        || it.contains("jpeg"))
                .collect(Collectors.toSet());
    }
}
