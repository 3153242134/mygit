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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
	
	Map<Long,Connection> customers = new ConcurrentHashMap<>(); 	
	Map<SocketChannel,Long> channels = new ConcurrentHashMap<>();
	
	ExecutorService pool = Executors.newCachedThreadPool();
	
	BlockingQueue<Connection> newCustomers = new ArrayBlockingQueue<>(1024);
	
	final Map<Long,List<String>> mailBox ;
	
	ServerSocketChannel listenCh;
	
	Recv reciever;
	
	Sender sender;
	
	Listener listener;
	
	Linker linker;
	
	public static void main(String[] args) throws IOException {
		Server s = new Server();
		s.start();
	}
	
	
	
	public Server() throws IOException {
		mailBox = new ConcurrentHashMap<>();
		listenCh = ServerSocketChannel.open();
		
		reciever = new Recv();
		
		sender = new Sender();
		
		listener = new Listener();
		
		linker = new Linker();
	}
	
	public void start () throws IOException{
		listenCh = ServerSocketChannel.open();
		listenCh.socket().bind(new InetSocketAddress(9999));
		
		
		new Thread(listener).start();;
		new Thread(linker).start();;
		new Thread(reciever).start();;
		new Thread(sender).start();;
	}
	
	class Recv implements Runnable{
		Selector selector;
		@Override
		public void run() {
			try {
				selector = Selector.open();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true) {
				try {
					if(selector.select(10)> 0 ) {
						Set<SelectionKey> keys = selector.keys();
						for(SelectionKey key: keys) {
							doRecieve(key);
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		ByteBuffer buff = ByteBuffer.allocate(1024);
		String regex = "\\s*to\\s*([0-9]+)\\s*;(.*)";
		private void doRecieve(SelectionKey key) throws IOException {
			SocketChannel ch = (SocketChannel) key.channel();
			List<Byte> msg = new ArrayList<>();
			while(ch.read(buff) >= 0 ) {
				buff.flip();
				while(buff.hasRemaining()) {
					msg.add(buff.get());
				}
				buff.clear();
			}
			byte[] msgBin = new byte[msg.size()];
			int i = 0;
			for(byte b : msg) {
				msgBin[i++] = b;
			}
			String msgStr = new String(msgBin,"utf-8");
			System.out.println("from client");
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(msgStr);
			
			if(!m.matches()) {
				System.err.println("illegle msg");
				return;
			}
			
			long toId = Integer.parseInt(m.group(1) );
			if(!customers.containsKey(toId)) {
				System.err.println("user not exists");
				return;
			}
			mailBox.get(toId).add( m.group(2));
			
		}
		
		public SelectionKey regist(SocketChannel ch) throws ClosedChannelException{
			if(selector == null)
				throw new RuntimeException();
			return ch.register(selector, SelectionKey.OP_READ);
		}
		
	}
	

	class Sender implements Runnable {
		Selector selector;
		@Override
		public void run() {
			try {
				selector = Selector.open();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true) {
				try {
					if(selector.select(10)> 0 ) {
						Set<SelectionKey> keys = selector.keys();
						for(SelectionKey key: keys) {
							doSend(key);
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		ByteBuffer buff = ByteBuffer.allocate(1024);
		private void doSend(SelectionKey key) throws IOException {
			SocketChannel ch = (SocketChannel) key.channel();
			long id = channels.get(ch);
			List<String> messages = mailBox.get(id);
			if(messages.size() == 0) {
				return;
			}
			ByteBuffer buff = ByteBuffer.allocate(1024);
			for(String msg : messages) {
				buff.put(msg.getBytes());
				buff.flip();
				ch.write(buff);
				buff.clear();
			}
			messages.clear();
		}
		
		public SelectionKey regist(SocketChannel ch) throws ClosedChannelException{
			if(selector == null)
				throw new RuntimeException();
			return ch.register(selector, SelectionKey.OP_WRITE);
		}
		
	}

	class Listener implements Runnable {

		
		@Override
		public void run() {
			while(!Thread.interrupted()) {
				try {
					while(true) {
						SocketChannel ch = listenCh.accept();
						System.out.println("accept");
						Customer newCus = new Customer();
						newCustomers.add(new Connection(newCus,ch));
					}
					
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	class Linker implements Runnable {

		@Override
		public void run() {
			while(!Thread.interrupted()) {
				while(true) {
					try {
						ByteBuffer buff  = ByteBuffer.allocate(1 << 10);
						Connection conn = newCustomers.take();
						System.out.println("link new coversation");
						SocketChannel ch = conn.ch; 
						ch.configureBlocking(false);
						buff.put(("your id is " + conn.customer.getId()).getBytes("utf-8"));
						buff.flip();
						ch.write(buff);
						customers.put(conn.customer.getId(), conn);
						mailBox.put(conn.customer.getId(), new ArrayList<String>());
						channels.put(ch, conn.customer.getId());
						/*
						 * 注册selector
						 */
						reciever.regist(ch);
						sender.regist(ch);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
}
