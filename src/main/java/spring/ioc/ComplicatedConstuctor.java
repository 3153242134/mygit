package spring.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplicatedConstuctor {

	int a;
	char b;
	Bean bean1;
	String c;
	Bean bean2;
	
	
	Logger logger = LoggerFactory.getLogger(getClass());
	public ComplicatedConstuctor(int a,char b,Bean b1,String c,Bean bean2,int e) {
		logger.debug("int=>{},char=>{},bean1.id=>{},str=>{},bean2.id=>{},e=>{}",new Object[]{a,b,bean1,c,bean2,e});
	}
}
