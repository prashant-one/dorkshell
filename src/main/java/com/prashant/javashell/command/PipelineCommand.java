package com.prashant.javashell.command;

import com.prashant.javashell.action.PipelineAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.net.URISyntaxException;

@ShellComponent
public class PipelineCommand {
    @Autowired
    private PipelineAction pipelineAction;

    @ShellMethod
    public void pipeline() throws URISyntaxException, IOException {
        pipelineAction.compileData();
    }
}
