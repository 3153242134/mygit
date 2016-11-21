package jdk.nio.chat;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatPorcessor implements Runnable{
	
	
	private final Map<SocketChannel,SocketChannel> chatCouples = new ConcurrentHashMap<>();
	
	private final MsgMap<SocketChannel> msgs = new MsgMap<>();
	
	public static final int MaxCouplesCount = 2 << 10;

	private volatile int maxMsgSize = 2 << 10;
	
	private CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
	
	private Selector recvSelector;
	
	private Selector sendSelector;
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				recvSelector.select(100);
				Set<SelectionKey> keys = recvSelector.selectedKeys();
				for(SelectionKey key : keys) {
					SocketChannel srcCh = (SocketChannel) key.channel();
					SocketChannel destCh = chatCouples.get(srcCh);
					storeMessag(srcCh,destCh);
				}
				
				sendSelector.select();
				Set<SelectionKey> sendKeys = sendSelector.keys();
				for(SelectionKey key : sendKeys) {
					SocketChannel destCh = (SocketChannel) key.channel();
					List<String> msgList = msgs.get(destCh);
					Iterator<String> itr = msgList.iterator();
					while(itr.hasNext()){
						String str = itr.next();
						ByteBuffer temp = ByteBuffer.wrap(str.getBytes("utf-8"));
						destCh.write(temp);
						itr.remove();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ByteBuffer buff = ByteBuffer.allocate(maxMsgSize);
	private void storeMessag(SocketChannel src,SocketChannel dest) throws IOException {
		int count = src.read(buff);
		if(count != -1) {
			buff.flip();
			CharBuffer charBuff= decoder.decode(buff);
			String message = new String(charBuff.array());
			System.out.println("start transfer msg : " + message);
			msgs.put(dest, message);
			List<String> s = msgs.get(null);
		}
		else {
			System.err.println("warn,empty channel!");
			return;
		}
		
		
	}
	
	public void summitNewChat(SocketChannel src,SocketChannel dest) throws ClosedChannelException {
		src.register(recvSelector, SelectionKey.OP_READ);
		dest.register(sendSelector, SelectionKey.OP_WRITE);
	}
	
	public void removeChat() {
		
	}
	
	private static class MsgMap<T extends Channel> extends HashMap<T,List<String>> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void put(T key,String msg) {
			List<String> msgs = this.get(key) == null ? new ArrayList<String>() : this.get(key);
			msgs.add(msg);
		}
	}
}
