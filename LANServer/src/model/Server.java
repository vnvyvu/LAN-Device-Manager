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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import controller.PacketHandler;
import controller.receive.DeviceRegisteredReceiver;
import controller.send.ProcessConfigSender;
import controller.send.USBDetectSender;

// TODO: Auto-generated Javadoc
/**
 * Class Server. It represents the server
 */
public class Server implements Runnable{
	
	/** The server channel. */
	private ServerSocketChannel serverChannel;
	
	/** The selector. The selector will select the existing channel's key. */
	private Selector selector;
	
	/** The port will be opened. */
	private int port;
	
	
	/**
	 * Instantiates a new server. First, initialize server channel and bind with port
	 * Initialize channel selector and set server channel to non-blocking
	 * 
	 * @param port -port will be opened
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Server(int port) throws IOException {
		super();
		this.port = port;
		this.serverChannel=ServerSocketChannel.open();
		this.serverChannel.bind(new InetSocketAddress(port));
		this.selector=Selector.open();
		serverChannel.configureBlocking(false);
	}

	/**
	 * Run server. 
	 * First, we always have a channel to listen new connection(multiple client)
	 * An infinite loop, instead of the selector collecting active channels, 
	 * it collects channel's state keys every 1 second.
	 * If a client requests a connection, server channel hears it and selector chooses server channel in next loop.
	 * Loop through keys and process depend on key state, Connectable or Writeable or Readable
	 */
	@Override
	public void run() {
		try {
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			while(!Thread.currentThread().isInterrupted()) {
				selector.select(5000);
				Iterator<SelectionKey> keys=selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key=keys.next();
					keys.remove();
					try{
						if(key.isAcceptable()) {
							accept(key);
						}
						if(key.isReadable()) {
							SocketChannel socketChannel=(SocketChannel) key.channel();
							if(PacketHandler.selectFunction(key, PacketHandler.readHead(socketChannel))) {
								socketChannel.register(selector, SelectionKey.OP_WRITE);
							}else socketChannel.register(selector, SelectionKey.OP_READ);
						}
						if(key.isWritable()) {
							SocketChannel socketChannel=(SocketChannel) key.channel();
							if(PacketHandler.selectFunction(key, PacketHandler.readHead(socketChannel))) {
								socketChannel.register(selector, SelectionKey.OP_WRITE);
							}else socketChannel.register(selector, SelectionKey.OP_READ);
						}
					}catch (Exception e) {
						// TODO: handle exception
						DeviceRegisteredReceiver.close((SocketChannel) key.channel());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Accept a connection.
	 *
	 * @param key the key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel=(ServerSocketChannel) key.channel();
		SocketChannel socketChannel=serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ProcessConfigSender.on(socketChannel);
		USBDetectSender.on(socketChannel);
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
}
