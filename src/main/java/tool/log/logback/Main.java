package tool.log.logback;


import java.net.URL;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;


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
//		LoggerContext loggerContext = 
//		LoggerContext loggerContext = (LoggerContext)StaticLoggerBinder.getSingleton().getLoggerFactory();
//		loggerContext.reset();
		
		LoggerContext loggerContext =new LoggerContext();
		ContextInitializer a = new ContextInitializer(loggerContext);
		a.configureByResource(logpath);
		
		
		Logger log = loggerContext.getLogger("a");
		log.info("test");
	}

}
