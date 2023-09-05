package com.prashant.javashell.action;

import com.prashant.javashell.command.BasicCommand;
import com.prashant.javashell.command.DorkCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class PipelineAction {

    @Autowired
    private BasicCommand basicCommand;
    @Autowired
    private DorkCommand dorkCommand;

    public HashMap<String, String> compilePipeline() throws URISyntaxException, IOException {
        String filePath = "/Users/prashantkushwaha/IdeaProjects/javashell/src/main/resources/db/grammer.txt";
        Path file = Paths.get(filePath);
        HashMap<String, String> command = new HashMap<>();
        List<String> lines = Files.readAllLines(file);
        lines.forEach(it -> {
            String[] tokens = it.split("=");
            command.put(tokens[0], tokens[1]);
        });
        command.forEach((k, v) -> {
            System.out.println(k + "  " + v);
        });
        return command;
    }

    public void compileData() throws Exception{
        HashMap<String,String> variableWithData= new HashMap<>();
        HashMap<String, String> data = compilePipeline();
        data.forEach((variable,command)->{
           String cmd[] =command.split("\\s+");
            if(cmd[0].equalsIgnoreCase("dork")){
                try {
             //       variableWithData.put(variable,dorkCommand.dorkQuery(cmd[1],Integer.valueOf(cmd[2])));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        variableWithData.forEach((k,v)->{
            System.out.println(k + "  " + v);
        });
    }

    public void executeCommand(String... command){
        String cm= Arrays.toString(command);

    }

    public HashMap<String,String> getCommandWithVariables(String command){
        HashMap<String,String> map = new HashMap<>();
        if(command.contains("$")){
            String[] data = command.split("$");
            for(int i=0;i<data.length;i++){
                map.put(data[0],data[i]);
            }
        }else {
            map.put(command,null);
        }
        return map;
    }


}

