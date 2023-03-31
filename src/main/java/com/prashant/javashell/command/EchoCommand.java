package com.prashant.javashell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class EchoCommand {
    @ShellMethod("Displays greeting message to the user whose name is supplied")
    public String echo(@ShellOption({"-N", "--name"}) String name) {
        System.out.println(name);
        return String.format("Hello %s! You are running spring shell cli-demo.", name);
    }
}
