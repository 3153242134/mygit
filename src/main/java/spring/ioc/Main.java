package spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class Main {


	public static void main(String[] args) {
		
		ApplicationContext a = new ClassPathXmlApplicationContext("context.xml");
		//Bean b =  (Bean) a.getBean("facBean");
		Object bean = a.getBean("complicatedBean");
		
		WebApplicationContext context;
		org.springframework.web.context.ContextLoaderListener listener;
	}

}
