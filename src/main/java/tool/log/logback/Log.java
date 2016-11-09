package tool.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

	
	public static Logger logger(Class<?> c) {
		return LoggerFactory.getLogger(c);
	}
	
}
