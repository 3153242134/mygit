package jdk.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	
	
	public Executor pool = Executors.newFixedThreadPool(2);
	
	ServerSocketChannel server;
	
	List<SocketChannel> channels = new ArrayList<>();
	
	ChatPorcessor processor = new ChatPorcessor();

	int port = 9999;
	
	
	public void start() throws IOException {
		final Selector matcher = Selector.open();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try{
					ByteBuffer buff = ByteBuffer.allocate(1024);
					while(true) {
						matcher.select();
			        	Set<SelectionKey> keys = matcher.keys();
			        	for(SelectionKey key : keys) {
			        		SocketChannel ch = (SocketChannel) key.channel();
			        		ch.read(buff);
			        		String num = new String(buff.array());
			        		int index = Integer.parseInt(num);
			        		
			        		processor.summitNewChat(ch, channels.get(index));
			        	}
					}
				}catch(Exception e) {
					e.printStackTrace();
					System.exit(0);
				}

			}
			
		});
		
		
		server = ServerSocketChannel.open();  //定义一个 ServerSocketChannel通道 
		server.socket().bind(new InetSocketAddress(port));  //ServerSocketChannel绑定端口   
        server.configureBlocking(false);   //配置通道使用非阻塞模式 
        
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        
        
        while(true) {
        	selector.select();
        	Set<SelectionKey> keys = selector.keys();
        	for(SelectionKey key : keys) {
        		ServerSocketChannel ch = (ServerSocketChannel) key.channel();
        		SocketChannel channel = ch.accept();//类似于io的socket，ServerSocketChannel的accept函数返回 SocketChannel 
                channel.configureBlocking(false);   //设置非阻塞模式    
                channels.add(channel);
                channel.register(matcher, SelectionKey.OP_READ);
        	}
        		
        }
	}
	
	public static void main(String[] args) {
		
	}

}
