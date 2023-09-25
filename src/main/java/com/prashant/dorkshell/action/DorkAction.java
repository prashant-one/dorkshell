package com.prashant.dorkshell.action;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prashant.dorkshell.constant.Constant;
import com.prashant.dorkshell.utils.Utils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.prashant.dorkshell.constant.Constant.ANSI_RED;
import static com.prashant.dorkshell.constant.Constant.ANSI_RESET;
import static com.prashant.dorkshell.utils.Utils.withCounter;

@Service
@Slf4j
public class DorkAction {
    @Value("classpath:db/dork-url-list.json")
    private Resource dorkUrlList;
    @Value("classpath:db/dork-db")
    private Resource dorkDB;
    @Autowired
    private Utils utils;
    @Autowired
    @Lazy
    private LineReader lineReader;


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
                .map(( var it) -> it.attr("href"))
                .filter(( var it) -> it.contains("&start=" + count + "0&sa="))
                .map(( var it) -> Constant.GOOGLE_URL + it)
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
                data.forEach(log::info);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return links;
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

    public void dorkFileSearch(String key, String ext,int count, int sleepTime) {
        List<String> dork = getDork(key, ext);
        dork.forEach(it->{
            try {
                Document doc = Jsoup.connect(Constant.GOOGLE_URL + "/search?q=" + it)
                       .ignoreContentType(true).ignoreHttpErrors(true).get();
                Elements elements = doc.getAllElements().select("a[href]");
                List<String> result = collectData(elements, ext);
                System.out.println(utils.convertToString(result));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<String> getDork(String key,String ext){
        List<String> storeDorkUrls = new ArrayList<>();

        try{
            byte[] dataArr = FileCopyUtils.copyToByteArray(dorkUrlList.getInputStream());
            String dorkFile = new String(dataArr, StandardCharsets.UTF_8);
            JsonObject dorkUrlJson = JsonParser.parseString(dorkFile).getAsJsonObject();
            Set<String> keys = dorkUrlJson.keySet();
            keys.forEach(it->{
                String result = dorkUrlJson.get(it).getAsJsonObject().get("dork").getAsString();
                result=key!=null ? result.replace("{key}",key): result.replace("{key}","");
                result=ext!=null ?result.replace("{ext}",ext):result.replace("{ext}","pdf");
                storeDorkUrls.add(result);
            });
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return storeDorkUrls;
    }

    public void getDorkByFile(String filePath) throws IOException {
        List<String> dorkData = utils.readFile(filePath);
        dorkData.forEach(it->{
            System.out.println(utils.convertToString(executeDorkQuery(it,1,"No",1000)));
        });
    }

    public void getAllDork() throws  IOException {
        File[] fileList = (new File(Objects.requireNonNull(
                getClass().getResource("/db/dork-db")).getPath())).listFiles();
        List<File> dorkList = Arrays.stream(fileList).collect(Collectors.toList());
        System.out.println(ANSI_RED+"Please enter number to execute dork"+ANSI_RESET);
        dorkList.forEach(withCounter((i,dork)->{
            System.out.println((ANSI_RED+"["+i+"]: "
                    +ANSI_RESET+ Arrays.stream(dork.getName().split(".txt")).findFirst().get()));
        }));
        System.out.println();
        System.out.println(ANSI_RED+"Please enter number:"+ANSI_RESET);
        int value = Integer.parseInt(this.lineReader.readLine());
        getDorkByFile(dorkList.get(value).getPath());

    }

}
