package com.prashant.dorkshell.command;

import com.prashant.dorkshell.action.SearchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
public class SearchCommand {
    @Autowired
    private SearchAction searchAction;

    @ShellMethod(key = "search", value = "search command help to find information using google dork")
    public String dorkFiles(@ShellOption(value = {"-s", "--search"}) String search) throws IOException {
        return searchAction.search(search.replace(","," "));
    }
}
