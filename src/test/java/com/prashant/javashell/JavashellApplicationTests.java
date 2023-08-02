package com.prashant.javashell;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class JavashellApplicationTests {
@Test
    public void executeDork()  {
        try {
            String query="java ext:pdf";
            Document doc = Jsoup.connect("https://www.google.com/search?q=" + query).get();
            Elements nextLinks = doc.getAllElements().select("a[href]");
            System.out.println(nextLinks);

            System.out.println(nextLinks.stream().map(it->it.attr("href")).collect(Collectors.toList()));
//                    .filter(it->it.startsWith("http") && it.endsWith("pdf"))
//                    .collect(Collectors.toList());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
