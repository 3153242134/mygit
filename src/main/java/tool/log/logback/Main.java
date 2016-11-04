package tool.log.logback;


import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;


public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		loggerSystemInit();
	}
	
	
	public static void loggerSystemInit() throws Exception {
		/*
		 * 指定路径启动日志系统
		 */
		URL logpath = Main.class.getResource("logback.xml");
	//	LoggerContext loggerContext = new LoggerContext();
		LoggerContext loggerContext = (LoggerContext)StaticLoggerBinder.getSingleton().getLoggerFactory();
		loggerContext.reset();
		
		new ContextInitializer(loggerContext).configureByResource(logpath);
		Logger log = LoggerFactory.getLogger("c");
		log.info("test1111111111111");
	}

}
