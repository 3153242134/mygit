package jdk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Test {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel server = ServerSocketChannel.open();
	//	server.configureBlocking(false);
		server.bind(new InetSocketAddress(9999));
		
		SocketChannel sc = server.accept();
		
		ByteBuffer buff = ByteBuffer.allocate(1 << 10);
		
		sc.read(buff);
		buff.flip();
		String msg = new String(buff.array(),"utf-8");
		System.out.println("msg = " + msg);
	}

}
