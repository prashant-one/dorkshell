package com.prashant.javashell.command;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.prashant.javashell.config.DorkConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@ShellComponent
public class BasicCommand {

    @Autowired
    private DorkConfig dorkConfig;

    @ShellMethod("Download a file using command download --url https://url.file.ext")
    public void download(@ShellOption("--url") URL url) throws IOException{
        String fileName = url.toString();
        fileName = fileName.substring(fileName.lastIndexOf('/')+1);
        System.out.println(fileName);
        try (InputStream in = url.openStream()) {
            System.out.println("===================Downloading started==============");
            Files.copy(in, Paths.get(fileName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("=============Download Completed==============");
        
    }

    /**
     * 
     */
    @ShellMethod("List all files using ll command")
    public void ll() throws IOException{

        try(Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir")))){
            System.out.println(System.getProperty("user.dir"));
            walk.filter(Files::isRegularFile).map(it->it.toString()).forEach(System.out::println);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod("Read file using cat command Example: cat /full/path.filename.txt")
    public void cat(String file) throws IOException{

        try(Stream<String> lines =Files.lines(Paths.get(file))){
            lines.forEach(System.out::println);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @ShellMethod("Create a file using nano command Example: nano --fileName fileName.txt Example data to write on file")
    public void nano(@ShellOption("--fileName")String fileName,String data) {
      try{
         Files.write(Paths.get(System.getProperty("user.dir")+"/"+fileName) , 
         data.getBytes(StandardCharsets.UTF_8));
      }catch(Exception e){
        System.out.println(e);
      }
    }

    @ShellMethod("Dork Google to find files with given extension Example: dork java pdf")
    public void dork(
        @ShellOption({"--search","-s"})String search,
        @ShellOption({"--type","-t"}) String type) throws IOException{
        try{
        System.out.println("Search: "+search +" type: "+type);
        Document doc = Jsoup.connect("https://www.google.com/search?q="+search+" "+"ext:"+type).get();  
        List<String> result = dorkConfig.getData(doc, type,2);
        result.forEach(System.out::println); 
        } catch (Exception e) {
            e.printStackTrace();
      }  
    }   
     
    
}
