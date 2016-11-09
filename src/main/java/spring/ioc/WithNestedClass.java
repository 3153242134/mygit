package spring.ioc;

public class WithNestedClass {

	
	public static class NestedClass {
		public NestedClass() {
			System.out.println("spring init the nested class : " + this.getClass());
		}
	}
}
