package com.prashant.javashell.action;

import com.prashant.javashell.command.BasicCommand;
import com.prashant.javashell.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SpiderAction {
    @Autowired
    private Utils utils;
    @Autowired
    private DorkAction dorkAction;
    @Autowired
    private BasicCommand basicCommand;
    private Set<String> allLinks;
    private Set<String> allImages;
    private Set<String> allData;
    private int counter=0;
    private String urlCheck="";
    private String linksFileName="";
    private String imgFileName="";
    private String allDataFileName="";


    public String getSpiderData(String url,String searchCommand)  {
        try {
            List<String> links = getSpiderLinks(url, null);

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

//    public List<String> getAllDistinctLink(String url){
//        getSpiderLinks(url,null);
//    }

    public List<String> getSpiderLinks(String url,String type)  {
        List<String> data =null;
        try {
            if (utils.isValidURL(url)) {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.getAllElements().select("a[href]");
                data = dorkAction.collectData(elements, type);
                //data.forEach(System.out::println);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return data;
    }

    public void getAllDistinctLink(String url) {
        try {
            if (utils.isValidURL(url)) {
                urlCheck = url;
                allLinks = new HashSet<>();
                allImages = new HashSet<>();
                allData = new HashSet<>();
                counter = 0;
                linksFileName = String.valueOf("links_" + Timestamp.from(Instant.now())) + ".txt";
                imgFileName = String.valueOf("images_" + Timestamp.from(Instant.now())) + ".txt";
                allDataFileName = String.valueOf("allData_" + Timestamp.from(Instant.now())) + ".txt";
                System.out.println("Writing links on file " + linksFileName);
                Set<String> linksResult = collectLinks(url);
                basicCommand.nano(linksFileName, utils.convertToString(linksResult));
                basicCommand.nano(imgFileName, utils.convertToString(allImages));
                basicCommand.nano(allDataFileName, utils.convertToString(allData));
            }
            System.out.println("Error : Please enter a valid url");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Set<String> collectLinks(String url){
        counter++;
        String[] links = new String[0];
        try {
            if (utils.isValidURL(url)) {
                Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                //Elements elements = doc.getAllElements().select("a[href]");
                Set<String> allResult=filterAllData(doc);
                allData.addAll(allResult);
                Set<String> linksResult = filterLinks(doc);
                allLinks.addAll(linksResult);
                Set<String> imgResult =filterImages(doc);
                allImages.addAll(imgResult);
                links = allLinks.toArray(new String[allLinks.size()]);
                System.out.println("Total links crawled ==> "+ links.length);
            }
            while (counter<links.length) {
                collectLinks(links[counter]);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allLinks;
    }
    public Set<String> filterAllData(Document doc){
        Elements elements = doc.getAllElements().select("a[href]");
        return elements.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .filter(it -> it.contains(urlCheck))
                .collect(Collectors.toSet());
    }
    public Set<String> filterLinks(Document doc){
        Elements elements = doc.getAllElements().select("a[href]");
        return elements.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .filter(it -> it.contains(urlCheck))
                .filter(it->it.substring(it.lastIndexOf("/")+1).isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<String> filterImages(Document doc){
        Elements elements = doc.getAllElements().select("img[src]");
        return elements.stream().map(it -> it.attr("src"))
                .filter(it -> it.startsWith("http"))
                .filter(it -> it.contains(urlCheck))
                .collect(Collectors.toSet());
    }

}
