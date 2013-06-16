package net.pechorina.kontempl.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class CustomApplicationContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(CustomApplicationContextListener.class);
	
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application has started");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application has stopped");
    }

}
