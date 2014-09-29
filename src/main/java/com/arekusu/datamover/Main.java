package com.arekusu.datamover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    DataMover importDataMover;

    @Autowired
    DataMover exportDataMover;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/application-context.xml");
        Main dm = ctx.getBean(Main.class);
        dm.start(args);
    }

    public void start(String[] args) {
        if (args.length == 2) {
            if (args[0].equals("export")) {
                exportFile();
            } else if (args[0].equals("import")) {
                importFile();
            }
        } else {
            logger.error("Incorrect command!!");
            logger.error("Usage: export <modelFile>");
            logger.error("Usage: import <modelFile>");
        }

    }

    private void importFile() {
        importDataMover.execute();
    }

    private void exportFile() {
        exportDataMover.execute();
    }

}
