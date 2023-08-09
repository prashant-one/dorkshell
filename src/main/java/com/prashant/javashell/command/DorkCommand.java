package com.prashant.javashell.command;

import com.prashant.javashell.action.DorkAction;
import com.prashant.javashell.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ShellComponent
public class DorkCommand {
    @Autowired
    private DorkAction dorkAction;

    @Autowired
    private Utils dorkUtils;

    @Value("classpath:db/dork-command-list.txt")
    private Resource resource;

    @ShellMethod(key = "dork-type",value = "Dork Google to find files with given extension, Example: dork-type java pdf")
    public String dork(@ShellOption({"-q","--query"}) String query,
                                       @ShellOption(value = {"-t","--type"}) String type,
                                       @ShellOption(value = {"-c","--count"},defaultValue = "1") int count){
        return dorkUtils.convertToString(dorkAction.executeDork(query,type,count));

    }

    @ShellMethod(key = "dork",value = "Dork Google to find urls, Example: dork your query")
    public String dorkQuery(@ShellOption(value={"-q","--query"},help="provide query in double quests") String query,
                                            @ShellOption(value = {"-c","--count"},defaultValue = "1") int count){
        return dorkUtils.convertToString(dorkAction.executeDorkQuery(query, count));

    }

    @ShellMethod(key="list",value = "Find available dork query in the system ")
    public String dorkList() {
        try {
            Path filepath = resource.getFile().toPath();
           Files.lines(filepath).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ShellMethod(key="os",value = "Find available dork query in the system ")
    public String checkOs() {
        String os="";
        try {
           os=dorkUtils.detectOS();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return os;
    }
}
