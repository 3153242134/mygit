package spring.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Factory {
	
	static Logger logger = LoggerFactory.getLogger(Factory.class);

	public FactoryBean create() {
		logger.debug("factory creating bean!");
		return new FactoryBean();
	}
}
