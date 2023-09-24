package com.prashant.dorkshell.action;

import com.prashant.dorkshell.constant.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class SearchAction {

    public String search(String query) throws IOException {
        ArrayList<String> searchData= new ArrayList<>();
        Document doc = Jsoup.connect(Constant.GOOGLE_URL + "/search?q=" + query)
                .ignoreContentType(true).ignoreHttpErrors(true).get();
        Elements links = doc.getAllElements().select("a[href]");
        Elements result = doc.getAllElements().select("span");
        result.forEach(it->{
          if (it.text().toLowerCase().contains(query.toLowerCase())) {
             searchData.add(it.text());
          }
        });
        searchData.stream()
                .reduce((x,y)->x.length()>y.length()?x:y)
                .ifPresent(it->getData().apply(it));

        links.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .forEach(link->getDataFromWebsite().apply(link));
        return null;
    }

    Function<String, List<String>> getData(){
        return it->{
            System.out.println(it);
            return null;
        };
    }

    Function<String, List<String>> getDataFromWebsite(){
        return link->{
            try {
                Document doc = Jsoup.connect(link)
                        .ignoreContentType(true).ignoreHttpErrors(true).get();
                Elements result = doc.getAllElements().select("span");
                result.stream()
                        .map(Element::text)
                        .reduce((x,y)->x.length()>y.length()?x:y)
                        .ifPresent(System.out::println);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }
}
