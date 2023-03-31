package com.prashant.javashell.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class DorkConfig {

    public List<String> nextSearchURL(Document document,String type,int count) throws IOException{
        Elements nextLinks = document.getAllElements().select("a[href]");
        List<String> nextSearch = nextLinks.stream()
        .map(it->it.attr("href"))
        .filter(it->it.contains("&start="+count+"0&sa="))
        .map(it->"https://www.google.com"+it)
        .collect(Collectors.toList());
        Document nextDoc = Jsoup.connect(nextSearch.get(0)).get(); 
        return extractData(nextDoc, type);
    }

    public List<String> extractData(Document searElements,String type){
        Elements nextLinks = searElements.getAllElements().select("a[href]");
        return nextLinks.stream().map(it->it.attr("href"))
        .filter(it->it.startsWith("http") && it.endsWith(type))
        .collect(Collectors.toList());
    }

    public List<String> getData(Document seacElements,String type,int count) throws IOException, InterruptedException{
        ArrayList<String> searchData= new ArrayList<>();
        System.out.println("=========Extracting page: =========");
         List<String> firstData = extractData(seacElements, type);
         searchData.addAll(firstData);
        for(int i=0; i<count-1;i++){
            Thread.sleep(5000);
            System.out.println("=========Extracting page: =========");
          List<String> data = nextSearchURL(seacElements,type,i+2);
          searchData.addAll(data);
        }
     return searchData;
    }
    
}
