package com.prashant.javashell.action;

import com.prashant.javashell.constant.Constant;
import jakarta.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DorkAction {

    public List<String> executeDork(String query,String type,int count)  {
        List<String> data= new ArrayList<>();
        List<String> nextSearchLinks=new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constant.GOOGLE_URL+"/search?q=" + query+" "+"ext:"+type).get();
            Elements elements = doc.getAllElements().select("a[href]");
            List<String> result = collectData(elements, type);
            data.addAll(result);
            for (int i=2;i<=count;i++) {
                List<String> links = collectNextLinks(elements, i);
                nextSearchLinks.addAll(links);
            }
            if(!nextSearchLinks.isEmpty()) {
                for (String link:nextSearchLinks){
                    Thread.sleep(1000);
                    result=callNextPage(link,type);
                    data.addAll(result);
                }
            }
            data.forEach(System.out::println);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }

    public List<String> collectData(Elements elements,String type){
        if(type!=null) {
            return elements.stream().map(it -> it.attr("href"))
                    .filter(it -> it.startsWith("http") && it.endsWith(type))
                    .collect(Collectors.toList());
        } return elements.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .collect(Collectors.toList());
    }

    private List<String> collectNextLinks(Elements elements,int count){
       return elements.stream()
                .map((@NotNull var it) -> it.attr("href"))
                .filter((@NotNull var it) -> it.contains("&start=" + count + "0&sa="))
                .map((@NotNull var it) -> Constant.GOOGLE_URL + it)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> callNextPage(String nextLinks,String type){
        List<String> data = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(nextLinks).get();
            Elements elements = doc.getAllElements().select("a[href]");
            data= collectData(elements,type);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }

    public List<String> executeDorkQuery(String query,int count)  {
        List<String> data= new ArrayList<>();
        List<String> nextSearchLinks=new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constant.GOOGLE_URL+"/search?q=" + query).get();
            Elements elements = doc.getAllElements().select("a[href]");
            List<String> result = collectData(elements, null);
            data.addAll(result);
            for (int i=2;i<=count;i++) {
                List<String> links = collectNextLinks(elements, i);
                nextSearchLinks.addAll(links);
            }
            if(!nextSearchLinks.isEmpty()) {
                for (String link:nextSearchLinks){
                    Thread.sleep(1000);
                    result=callNextPage(link,null);
                    data.addAll(result);
                }
            }
          //  data.forEach(System.out::println);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }
}
