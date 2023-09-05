package com.prashant.javashell.action;

import com.prashant.javashell.command.BasicCommand;
import com.prashant.javashell.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SpiderAction {
    @Autowired
    private Utils utils;
    @Autowired
    private DorkAction dorkAction;
    @Autowired
    private BasicCommand basicCommand;
    Pattern emailPattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    private Set<String> allLinks;
    private Set<String> allImages;
    private Set<String> allData;
    private Set<String> allEmails;
    private int counter=0;
    private String urlCheck="";


    public void getAllDistinctLink(String url) {
        String allDataFileName="";
        String imgFileName="";
        String linksFileName="";
        String emailsFileName="";
        try {
            if (utils.isValidURL(url)) {
                urlCheck = url;
                allLinks = new HashSet<>();
                allImages = new HashSet<>();
                allData = new HashSet<>();
                allEmails = new HashSet<>();
                counter = 0;
                File f1 = new File(System.getProperty("user.dir")+"/data");
                f1.mkdir();
                linksFileName = String.valueOf("data/links_" + Timestamp.from(Instant.now())) + ".txt";
                imgFileName = String.valueOf("data/images_" + Timestamp.from(Instant.now())) + ".txt";
                allDataFileName = String.valueOf("data/allData_" + Timestamp.from(Instant.now())) + ".txt";
                emailsFileName = String.valueOf("data/emails_" + Timestamp.from(Instant.now())) + ".txt";
                System.out.println("Writing links on file " + linksFileName);
                Set<String> linksResult = collectLinks(url);
                basicCommand.nano(linksFileName, utils.convertToString(linksResult));
                basicCommand.nano(imgFileName, utils.convertToString(allImages));
                basicCommand.nano(allDataFileName, utils.convertToString(allData));
                basicCommand.nano(emailsFileName, utils.convertToString(allEmails));
            }else {
                System.out.println("Error : Please enter a valid url");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Set<String> collectLinks(String url){
        counter++;
        String[] links = new String[0];
        try {
            if (utils.isValidURL(url)) {
                Document doc = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).get();
                Set<String> allResult=filterAllData(doc);
                allData.addAll(allResult);
                Set<String> linksResult = filterLinks(doc);
                allLinks.addAll(linksResult);
                Set<String> imgResult =filterImages(doc);
                allImages.addAll(imgResult);
                Set<String> emailsResult=filterEmails(doc);
                allEmails.addAll(emailsResult);
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

    public Set<String> filterAllData(Document doc){
        Elements elements = doc.getAllElements().select("a[href]");
        return elements.stream().map(it -> it.attr("href"))
                .filter(it -> it.startsWith("http"))
                .filter(it -> it.contains(urlCheck))
                .collect(Collectors.toSet());
    }

    public Set<String> filterEmails(Document doc){
        HashSet<String> emails=new HashSet<>();
        Matcher emailMatcher = emailPattern.matcher(doc.body().html());
        while(emailMatcher.find()){
            emails.add(emailMatcher.group());
        }
        return emails;
    }

}
