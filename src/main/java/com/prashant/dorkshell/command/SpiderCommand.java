package com.prashant.dorkshell.command;

import com.prashant.dorkshell.action.SpiderAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class SpiderCommand {

    @Autowired
    private SpiderAction spiderAction;
    @ShellMethod(key = "spider",value = "spider command will work as crawler to retrieve website data")
    public void spider(@ShellOption(value = {"-u","--url"}) String url){
          spiderAction.getAllDistinctLink(url);
    }
}
