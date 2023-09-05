package com.prashant.javashell.command;

import com.prashant.javashell.action.DorkAction;
import com.prashant.javashell.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DorkCommand {
    @Autowired
    private DorkAction dorkAction;

    @Autowired
    private Utils dorkUtils;


    @ShellMethod(key = "dork-site", value = "Dork Google to find files with given extension, Example: dork-type java pdf")
    public String dork(@ShellOption({"-u", "--url"}) String query,
                       @ShellOption(value = {"-e", "--extension"}) String type,
                       @ShellOption(value = {"-c", "--count"}, defaultValue = "1") int count,
                       @ShellOption(value = {"-k", "--keyword"}, defaultValue = "") String key) {
        return dorkUtils.convertToString(dorkAction.executeDork(query, type, count, key));

    }

    @ShellMethod(key = "dork", value = "Dork Google to find urls, Example: dork your query")
    public String dorkQuery(@ShellOption(value = {"-q", "--query"}, help = "enter you search string") String query,
                            @ShellOption(value = {"-c", "--count"}, defaultValue = "1", help = "for number of google pages search, Example: -c 5 ") int count,
                            @ShellOption(value = {"-t", "--text"}, defaultValue = "__NO__", help = "for text result, Example: -t yes ") String text,
                            @ShellOption(value = {"-s", "--sleep"}, defaultValue = "1000", help = "enter sleep time in millisecond for next search, Example -s 3000") int sleepTime) {
        return dorkUtils.convertToString(dorkAction.executeDorkQuery(query, count, text, sleepTime));

    }

    @ShellMethod(key = "dork-image", value = "Dork Google to find files with given extension, Example: dork-type java pdf")
    public void dorkImage(@ShellOption(value = {"-k", "--keyword"}) String key,
                            @ShellOption(value = {"-c", "--count"}, defaultValue = "1") int count,
                            @ShellOption(value = {"-s", "--sleep"}, defaultValue = "1000") int sleep) {
            dorkAction.dorkImageSearch(key, count, sleep);

    }

}
