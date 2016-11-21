package rbmq;


import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
	
	static final String QUEUE_NAME = "firstQueue";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		while(true) {
			
			
			String message = s.nextLine();
			if(message.equals("quit"))
				break;
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    
		    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		   
		    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
		    System.out.println(" [x] Sent '" + message + "'");
		    
		    channel.close();
		    connection.close();
		    
		}
		s.close();
	}

}
