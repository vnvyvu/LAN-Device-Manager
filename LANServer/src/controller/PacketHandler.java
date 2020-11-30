/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */

package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import controller.receive.ClipboardDataReceiver;
import controller.receive.DeviceRegisteredReceiver;
import controller.receive.DriverMonitorReceiver;
import controller.send.FileSender;

// TODO: Auto-generated Javadoc
/**
 * A static class. It includes commonly used functions for easy reuse
 */
public class PacketHandler {
	
	/** The worker. */
	public static ExecutorService worker= Executors.newFixedThreadPool(50);
	
	/** The bandwidth. */
	public volatile static HashMap<SocketChannel, Integer> bw=new HashMap<SocketChannel, Integer>();
	
	/**
	 * selectFunction will choose the appropriate function depend on sent packet header.
	 *
	 * @param key           -channel's key
	 * @param head          -beginning of the received packet
	 * @return true, if next action is writeable
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean selectFunction(SelectionKey key, byte head) throws IOException {
		SocketChannel socketChannel=(SocketChannel) key.channel();
		int length=PacketHandler.readLength(socketChannel);
		switch (head) {
			case (byte)1:
				return DeviceRegisteredReceiver.read(socketChannel, length);
			case (byte)2: 
				return FileSender.write(socketChannel);
			case (byte)3:
				return ClipboardDataReceiver.read(socketChannel, length);
			case (byte)4:
				return DriverMonitorReceiver.readDriverInjected(socketChannel, length);
			case (byte)5:
				return DriverMonitorReceiver.readDriverEjected(socketChannel, length);
			case (byte)6:
				break;
			case (byte)7:
				break;
			case (byte)8:
				break;
			case (byte)9:
				break;
			default:
				break;
		}
		return false;	
	}
	
	/**
	 * Read 1 byte from socket, it's packet header.
	 *
	 * @param socketChannel the socket channel
	 * @return the byte head
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte readHead(SocketChannel socketChannel) throws IOException {
		ByteBuffer buff=ByteBuffer.allocate(1);
		socketChannel.read(buff);
		buff.flip();
		return buff.array()[0];
	}
	
	/**
	 * Read data length, read 2 bytes from socket.
	 *
	 * @param socketChannel the socket channel
	 * @return the length
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int readLength(SocketChannel socketChannel) throws IOException {
		ByteBuffer buff=ByteBuffer.allocate(2);
		socketChannel.read(buff);
		buff.flip();
		return new BigInteger(buff.array()).intValue();
	}
	
	/**
	 * Read size(byte) from socket to array. Estimates the buffer size based on the sender's function
	 * Do not do this without taking packet header
	 *
	 * @param socketChannel the socket channel
	 * @param size the size
	 * @return A byte array read from socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] read2Array(SocketChannel socketChannel, int size) throws IOException {
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		ByteBuffer buff=ByteBuffer.allocate(size);
		socketChannel.read(buff);
		buff.flip();
		bao.write(buff.array());
		bao.close();
		return bao.toByteArray();
	}
	
	/**
	 * Write header to socket. Usually used to request receiver to perform a function
	 *
	 * @param socketChannel socket channel
	 * @param head header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeHead(SocketChannel socketChannel, byte head) throws IOException {
		socketChannel.write(ByteBuffer.wrap(new byte[] {head}));
	}
	
	/**
	 * I give a lot of work to my worker, 
	 * but he will not write more than 1500 bytes to socket at the same time
	 * Write data to socket with header and length. 
	 * Need to inverse if length's size is 1
	 * Buffer needs to be flip before writing.
	 *  
	 * I don't know why...
	 *
	 * @param socket     -to read/write data
	 * @param head       -packet header
	 * @param data       -the packet to send
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write2Socket(SocketChannel socket, byte head, byte[] data) throws IOException {
		if(!bw.containsKey(socket)) {
			bw.put(socket, 1450);
		}
		worker.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					if(bw.get(socket)>data.length) {
						byte[] temp=new BigInteger(""+data.length).toByteArray();
						byte[] length=new byte[2];
						if(temp.length==1) length[1]=temp[0];
						else length=temp;
						
						ByteBuffer packet=ByteBuffer.wrap(new byte[data.length+3]);
						packet.put(head);
						packet.put(length);
						packet.put(data);
						packet.flip();
						
						bw.put(socket, bw.get(socket)-data.length);
						socket.write(packet);
						packet.clear();
						bw.put(socket, bw.get(socket)+data.length);
						synchronized (data) {
							data.notifyAll();
						}
					}else {
						synchronized (data) {
							data.wait(5000);
						}
					}
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Gets the config.
	 *
	 * @param file the config file
	 * @return the config file
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static YamlMapping getConfig(String file) throws FileNotFoundException, IOException {
		return Yaml.createYamlInput(new File(file)).readYamlMapping();
	}
}
