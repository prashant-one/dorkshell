package com.prashant.dorkshell.command;

import com.prashant.dorkshell.action.DorkAction;
import com.prashant.dorkshell.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.net.URISyntaxException;

@ShellComponent
public class DorkCommand {
    @Autowired
    private DorkAction dorkAction;

    @Autowired
    private Utils dorkUtils;


    @ShellMethod(key = "dork-site", value = "dork-sit will execute dork on a specific site, Example: dork-site -u example.com -k doc_name -e pdf")
    public String dork(@ShellOption({"-u", "--url"}) String query,
                       @ShellOption(value = {"-e", "--extension"}) String type,
                       @ShellOption(value = {"-c", "--count"}, defaultValue = "1") int count,
                       @ShellOption(value = {"-k", "--keyword"}, defaultValue = "") String key) {
        return dorkUtils.convertToString(dorkAction.executeDork(query, type, count, key));

    }

    @ShellMethod(key = "dork", value = "dork will execute you query, Example: dork -q your_dork_query -c 2")
    public String dorkQuery(@ShellOption(value = {"-q", "--query"}, help = "enter you search string") String query,
                            @ShellOption(value = {"-c", "--count"}, defaultValue = "1", help = "for number of google pages search, Example: -c 5 ") int count,
                            @ShellOption(value = {"-t", "--text"}, defaultValue = "__NO__", help = "for text result, Example: -t yes ") String text,
                            @ShellOption(value = {"-s", "--sleep"}, defaultValue = "1000", help = "enter sleep time in millisecond for next search, Example -s 3000") int sleepTime) {
        return dorkUtils.convertToString(dorkAction.executeDorkQuery(query, count, text, sleepTime));

    }

    @ShellMethod(key = "dork-image", value = "dork-image will find your image file link, Example: dork-image -k flowers")
    public void dorkImage(@ShellOption(value = {"-k", "--keyword"}) String key,
                            @ShellOption(value = {"-c", "--count"}, defaultValue = "1") int count,
                            @ShellOption(value = {"-s", "--sleep"}, defaultValue = "1000") int sleep) {
            dorkAction.dorkImageSearch(key, count, sleep);

    }

    @ShellMethod(key = "dork-file", value = "dork-file will find your file with extension in google , Example: dork-file -k nodejs -e pdf -c 2 -s 1000")
    public void dorkFile(@ShellOption(value = {"-k", "--keyword"},defaultValue = ShellOption.NULL) String key,
                          @ShellOption(value = {"-e","--extension"},defaultValue = ShellOption.NULL) String ext,
                          @ShellOption(value = {"-c", "--count"}, defaultValue = "1") int count,
                          @ShellOption(value = {"-s", "--sleep"}, defaultValue = "1000") int sleep) {
            dorkAction.dorkFileSearch(key,ext, count, sleep);
    }

    @ShellMethod(key = "run-dork-file", value = "This command will execute you local dork text file, Example: run-dork-file -f /home/user/dork/dorkfile.txt")
    public void dorkFiles(@ShellOption(value = {"-f", "--filePath"}) String filePath) throws IOException {
        dorkAction.getDorkByFile(filePath);
    }

    @ShellMethod(key = "dork-all", value = "Dork Google to find files with given extension, Example: dork-all")
    public void dorkAll() throws IOException {
        dorkAction.getAllDork();
    }

}
