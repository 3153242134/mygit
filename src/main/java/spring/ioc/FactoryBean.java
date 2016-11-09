package spring.ioc;

public class FactoryBean {

	FactoryBean(){
		
	}
	
	public static FactoryBean createInstance() {
		System.out.println("factory creating bean! : ");
		return new FactoryBean();
	}
}
