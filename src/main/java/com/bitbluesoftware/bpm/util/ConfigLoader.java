package com.bitbluesoftware.bpm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    Logger log = LoggerFactory.getLogger(ConfigLoader.class);
    public ConfigLoader(){

    }

    public Map<String,String> readConfig() {
        Map<String,String> config = new HashMap<>();
        log.error(System.getProperty("user.dir")+"/config.properties");
        File file = new File(System.getProperty("user.dir")+File.separator+"config.properties");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                config.put(st.substring(0, st.indexOf('=')),st.substring(st.indexOf('=') + 1));
//                log.error("Key: " + st.substring(0, st.indexOf('=')) + "\tValue: " + st.substring(st.indexOf('=') + 1));
            }
        }catch (IOException e){
            log.error("Error parsing config: "+e.getMessage());
        }
        return config;
    }
}
