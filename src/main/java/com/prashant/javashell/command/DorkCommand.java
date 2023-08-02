package com.prashant.javashell.command;

import com.prashant.javashell.action.DorkAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DorkCommand {
    @Autowired
    private DorkAction dorkAction;

    @ShellMethod("Dork Google to find files with given extension Example: dork java pdf")
    public void dork(@ShellOption({"-q","--query"}) String query,
                         @ShellOption(value = {"-t","--type"}) String type,
                         @ShellOption(value = {"-c","--count"},defaultValue = "1") int count){
        dorkAction.executeDork(query,type,count);

    }

    @ShellMethod("Dork Google to find files with given extension Example: dork query ")
    public void dorkquery(@ShellOption(value={"-q","--query"},help="provide query in double quests") String query,
                     @ShellOption(value = {"-c","--count"},defaultValue = "1") int count){
        dorkAction.executeDorkQuery(query,count);

    }
}
