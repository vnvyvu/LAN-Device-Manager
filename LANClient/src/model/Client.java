/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

import controller.Utils;

// TODO: Auto-generated Javadoc
/**
 * Class Client. It represents the client
 */
public class Client implements Runnable{
	
	/** The client socket channel. */
	private SocketChannel clientSocketChannel;
	
	/** The selector. The selector will select the existing channel's key*/
	private Selector selector;
	
	/** The server port to connect. */
	private int serverPort;
	
	/** The server address to connect. */
	private String serverAddress;
	
	/** The after connect map. Several actions need to be taken immediately after connecting, this map helps to detect if the client just connected successfully or not.*/
	private HashMap<SocketChannel, Boolean> afterConnect=new HashMap<SocketChannel, Boolean>();
	
	/**
	 * Instantiates a new client. Create a client channel and set it to non-blocking
	 * Initialize channel selector
	 *
	 * @param serverAddress -the server add
	 * @param serverPort -the server port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Client(String serverAddress, int serverPort) throws IOException {
		super();
		this.serverAddress=serverAddress;
		this.serverPort = serverPort;
		this.clientSocketChannel=SocketChannel.open();
		this.clientSocketChannel.configureBlocking(false);
		this.selector=Selector.open();
	}

	/**
	 * Run client.
	 * First, we always have a channel to connect to
	 * (can expand into a single client connecting to multiple servers). 
	 * connect() method helps client shake hands with server
	 * but not really connected in case the server has not responded. 
	 * An infinite loop, instead of the selector collecting active channels, 
	 * it collects channel's state keys every 1 second.
	 * Loop through keys and process depend on key state, Connectable or Writeable or Readable
	 */
	@Override
	public void run() {
		try {
			this.clientSocketChannel.register(this.selector, SelectionKey.OP_CONNECT);
			this.clientSocketChannel.connect(new InetSocketAddress(this.serverAddress, this.serverPort));
			while(!Thread.currentThread().isInterrupted()) {
				this.selector.select(1000);
				Iterator<SelectionKey> keys=this.selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key=keys.next();
					keys.remove();
					if(key.isValid()&&key.isConnectable()) {
						connect(key);
					}else {
						if(key.isValid()&&key.isWritable()) {
							write(key);
						}
						if(key.isValid()&&key.isReadable()) {
							read(key);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect. Check connection, if ready, call finishConnect() to finish
	 * then add the channel to the map
	 * 
	 * @param key the key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void connect(SelectionKey key) throws IOException {
		SocketChannel socketChannel=(SocketChannel) key.channel();
		socketChannel.configureBlocking(false);
		if(socketChannel.isConnectionPending()) {
			socketChannel.finishConnect();
			this.afterConnect.put(socketChannel, true);
		}
		socketChannel.register(selector, SelectionKey.OP_WRITE);
	}
	
	/**
	 * Write. Key's state is writable, so the channel attached to it is waiting for data to read
	 * if previous key's state is connectable then performs a different action than writeable state
	 *
	 * @param key the key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel=(SocketChannel) key.channel();
		if(this.afterConnect.get(socketChannel)!=null) {
			this.afterConnect.remove(socketChannel);
			Utils.selectFunction(selector, socketChannel, (byte)1, false);
		}else Utils.selectFunction(selector, socketChannel, Utils.readHead(socketChannel), false);
	}
	
	/**
	 * Read.
	 *
	 * @param key the key
	 */
	private void read(SelectionKey key) {
		System.out.println("READ");
	}
	
	/**
	 * Gets the server port.
	 *
	 * @return the server port
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Sets the server port.
	 *
	 * @param serverPort the new server port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Gets the server address.
	 *
	 * @return the server address
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Sets the server address.
	 *
	 * @param serverAdd the new server address
	 */
	public void setServerAddress(String serverAdd) {
		this.serverAddress = serverAdd;
	}
	
}
