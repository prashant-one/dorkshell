package com.prashant.javashell.action;

import com.prashant.javashell.constant.Constant;
import com.prashant.javashell.utils.Utils;
import jakarta.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DorkAction {

    @Autowired
    private Utils utils;

    public List<String> executeDorkQuery(String query, int count, String text, int sleepTime) {
        List<String> data = new ArrayList<>();
        List<String> nextSearchLinks = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constant.GOOGLE_URL + "/search?q=" + query).get();
            if (text.equalsIgnoreCase("yes")) {
                getTextResult(doc);
            } else {

                Elements elements = doc.getAllElements().select("a[href]");
                List<String> result = collectData(elements, null);
                data.addAll(result);
                for (int i = 2; i <= count; i++) {
                    List<String> links = collectNextLinks(elements, i);
                    nextSearchLinks.addAll(links);
                }
                if (!nextSearchLinks.isEmpty()) {
                    for (String link : nextSearchLinks) {
                        Thread.sleep(sleepTime);
                        result = callNextPage(link, null);
                        data.addAll(result);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public List<String> executeDork(String query, String type, int count,String key) {
        List<String> data = new ArrayList<>();
        List<String> nextSearchLinks = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constant.GOOGLE_URL + "/search?q=site:" + query + " " + "ext:" + type +" "+ key).get();
            Elements elements = doc.getAllElements().select("a[href]");
            List<String> result = collectData(elements, type);
            data.addAll(result);
            for (int i = 2; i <= count; i++) {
                List<String> links = collectNextLinks(elements, i);
                nextSearchLinks.addAll(links);
            }
            if (!nextSearchLinks.isEmpty()) {
                for (String link : nextSearchLinks) {
                    Thread.sleep(1000);
                    result = callNextPage(link, type);
                    data.addAll(result);
                }
            }
            data.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public List<String> collectData(Elements elements, String type) {
        if (type != null) {
            return elements.stream().map(it -> it.attr("href"))
                    .filter(it -> it.startsWith("http") && it.endsWith(type))
                    .collect(Collectors.toList());
        }
        return elements.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .collect(Collectors.toList());
    }

    private List<String> collectNextLinks(Elements elements, int count) {
        return elements.stream()
                .map((@NotNull var it) -> it.attr("href"))
                .filter((@NotNull var it) -> it.contains("&start=" + count + "0&sa="))
                .map((@NotNull var it) -> Constant.GOOGLE_URL + it)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> callNextPage(String nextLinks, String type) {
        List<String> data = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(nextLinks).get();
            Elements elements = doc.getAllElements().select("a[href]");
            data = collectData(elements, type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    private String getTextResult(Document doc) {
        Elements data = doc.getAllElements().select("p");
        System.out.println(data.text());
        return null;
    }

    public List<String> dorkImageSearch(String query, int count, int sleepTime) {
        List<String> data = new ArrayList<>();
        List<String> nextSearchLinks = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constant.GOOGLE_URL + "/search?q=" + query+"&espv=2&biw=1366&bih=667&site=webhp&source=lnms&tbm=isch&sa=X&ei=XosDVaCXD8TasATItgE&ved=0CAcQ_AUoAg")
                    .ignoreContentType(true).ignoreHttpErrors(true).get();

                Elements elements = doc.getAllElements().select("a[href]");
                List<String> result = collectData(elements, null);
                data.addAll(result);
                for (int i = 2; i <= count; i++) {
                    List<String> links = collectNextLinks(elements, i);
                    nextSearchLinks.addAll(links);
                }
                if (!nextSearchLinks.isEmpty()) {
                    for (String link : nextSearchLinks) {
                        Thread.sleep(sleepTime);
                        result = callNextPage(link, null);
                        data.addAll(result);
                    }
                }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return getImageLinks(data);
      //  return data;
    }

    public List<String> getImageLinks(List<String> urls){
        List<String> links = new ArrayList<>();
        urls.forEach(it->{
            try {
                Document doc = Jsoup.connect(it)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10 * 1000)
                        .get();
               // links.addAll();
                Set<String> data = utils.filterImages(doc);
                data.forEach(System.out::println);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return links;
    }


}
