package com.prashant.dorkshell.command;

import com.prashant.dorkshell.action.PipelineAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellMethod;


public class PipelineCommand {
    @Autowired
    private PipelineAction pipelineAction;

    @ShellMethod
    public void pipeline() throws Exception {
        pipelineAction.compileData();
    }
}
