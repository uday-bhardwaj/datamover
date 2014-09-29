package com.arekusu.datamover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);


    Map<String, DataMover> dataMoversMap;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/application-context.xml");
        Main dm = ctx.getBean(Main.class);
        dm.start(args);
    }

    public void start(String[] args) {
        if (args.length == 2) {
            if (dataMoversMap.keySet().contains(args[0])) {
                dataMoversMap.get(args[0]).execute();
            }
        } else {
            logger.error("Incorrect command!!");
            for (String command : dataMoversMap.keySet()) {
                logger.error("Usage: " + command + " <modelFile>");
            }
        }

    }

    public Map<String, DataMover> getDataMoversMap() {
        return dataMoversMap;
    }

    public void setDataMoversMap(Map<String, DataMover> dataMoversMap) {
        this.dataMoversMap = dataMoversMap;
    }

}
