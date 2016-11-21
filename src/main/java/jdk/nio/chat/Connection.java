package jdk.nio.chat;

import java.nio.channels.*;
import java.util.Set;

public class Connection {
	
	Customer customer;
	
	SocketChannel ch;

	public Connection(	Customer customer,SocketChannel ch) {
		this.ch = ch;
		this.customer = customer;
	}

}
