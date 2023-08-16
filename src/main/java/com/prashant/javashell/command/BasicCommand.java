package com.prashant.javashell.command;

import com.prashant.javashell.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@ShellComponent
public class BasicCommand {
    @Autowired
    private Utils dorkUtils;

    @ShellMethod("Download a file using command download --url https://url.file.ext")
    public void download(@ShellOption(value = {"-u","--url"}) String stringUrl,
                         @ShellOption(value = {"-p","--filePath"})String filePath) throws IOException{
        try {
            if (dorkUtils.isValidURL(stringUrl)) {
                String fileName = stringUrl;
                URL url =new URL(stringUrl);
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                try (InputStream in = url.openStream()) {
                    System.out.println("===================Downloading started==============");
                    Files.copy(in, Paths.get(fileName));
                    System.out.println("Downloaded ==> " + fileName);
                    System.out.println("=============Download Completed==============");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }else {
                System.out.println("Error: invalid url "+stringUrl);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * 
     */
    @ShellMethod("List all files using ls command")
    public void ls() throws IOException{

        try(Stream<Path> walk = Files.walk(Paths.get(System.getProperty("user.dir")))){
            System.out.println(System.getProperty("user.dir"));
           walk.filter(Files::isRegularFile).map(it->it.getFileName()).forEach(System.out::println);

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
    public void nano(@ShellOption(value = {"--fileName","-f"})String fileName,String data) {
      try{
         Files.write(Paths.get(System.getProperty("user.dir")+"/"+fileName) , 
         data.getBytes(StandardCharsets.UTF_8));
      }catch(Exception e){
        System.out.println(e.getMessage());
      }
    }

    @ShellMethod(key="l",value = "execute system command")
    public void localCommand(@ShellOption(value = {"--command","-c"},help = "l 'system command'")String command) {
        Process process = null;
        try{
            if(dorkUtils.detectOS().toLowerCase().contains("mac") || dorkUtils.detectOS().toLowerCase().contains("nux")) {
                process = Runtime.getRuntime().exec(command);
            } if(dorkUtils.detectOS().toLowerCase().contains("win")) {
                process = Runtime.getRuntime().exec("cmd /c "+command);
            }
            assert process != null;
            process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line=reader.readLine())!=null){
                    System.out.println(line);
                }
                process.destroy();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            assert process != null;
            process.destroy();
        }
    }

}
