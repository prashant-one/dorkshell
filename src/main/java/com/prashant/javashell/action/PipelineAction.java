package com.prashant.javashell.action;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class PipelineAction {

    public HashMap<String, String> compilePipeline() throws URISyntaxException, IOException {
        String filePath = "/Users/prashantkushwaha/IdeaProjects/javashell/src/main/resources/db/grammer.txt";
        Path file = Paths.get(filePath);
        HashMap<String, String> command = new HashMap<>();
        List<String> lines = Files.readAllLines(file);
        lines.forEach(it -> {
            String[] variables = it.split("=");
            command.put(variables[0], variables[1]);

        });
        command.forEach((k, v) -> {
            System.out.println(k + "  " + v);
        });
        return command;
    }

    public void compileData() throws URISyntaxException, IOException{
        HashMap<String,List<String>> variableWithData= new HashMap<>();
        HashMap<String, String> data = compilePipeline();
        data.forEach((variable,command)->{

            Class c = null;
            try {
                c = Class.forName("com.prashant.javashell.action.Test");
                Object obj = c.newInstance();
//                String[] commands=command.split("$");
                HashMap<String, String> cmd = getCommandWithVariables(command);
//                for(String com:cmd) {
//                    Method method = c.getDeclaredMethod(commands[0], null);
//                method.setAccessible(true);
//                String result =(String) method.invoke(obj, null);
//                variableWithData.put(variable, Collections.singletonList(result));
//                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
              catch (Exception e){
            throw new RuntimeException(e);

        }
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

