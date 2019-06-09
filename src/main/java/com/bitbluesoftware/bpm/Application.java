package com.bitbluesoftware.bpm;

import java.util.Arrays;

import com.bitbluesoftware.bpm.util.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sun.rmi.runtime.Log;

@SpringBootApplication
public class Application {
    static Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        log.info("#################################################");
        log.info("#################################################");
        log.info("#####             BitBlue Software          #####");
        log.info("#####              Budget Manager           #####");
        log.info("#################################################");
        log.info("#####               v0.0.1-alpha            #####");
        log.info("#################################################");
        log.info("#################################################");
        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
        log.info("Maximum memory (MB): " + (maxMemory == Long.MAX_VALUE ? "no limit" :  (double)Math.round((maxMemory*0.000001)*100)/100));
        log.info("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
    }

}
