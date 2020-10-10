/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */

package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import controller.receive.DeviceRegisteredCollector;

// TODO: Auto-generated Javadoc
/**
 * A static class. It includes commonly used functions for easy reuse
 */
public class Utils {
	
	/**
	 * selectFunction will choose the appropriate function depend on server sent packet header.
	 *
	 * @param key      -channel's key
	 * @param head          -beginning of the received packet
	 * @param isRead        -to indicate whether next action is send or receive
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void selectFunction(SelectionKey key, byte head, boolean isRead) throws IOException {
		if(isRead) {
			switch (head) {
			case (byte)1:
				DeviceRegisteredCollector.read(key);
				break;
			case (byte)2:
				break;
			case (byte)3:
				break;
			case (byte)4:
				break;
			case (byte)5:
				break;
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
		}else {
			switch (head) {
			case (byte)1:
				break;
			case (byte)2:
				break;
			case (byte)3:
				break;
			case (byte)4:
				break;
			case (byte)5:
				break;
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
		}
	}
	
	/**
	 * readHead function will return packet header. It needs to be done before reading data from socket
	 *
	 * @param socket -to read/write data
	 * @return the byte is packet header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte readHead(SocketChannel socket) throws IOException {
		socket.configureBlocking(false);
		ByteBuffer buff=ByteBuffer.allocate(1);
		socket.read(buff);
		byte res=buff.array()[0];
		buff.clear();
		return res;
	}
	
	/**
	 * Read data from socket to array. Do not do this without taking packet header
	 *
	 * @param socket -to read/write data
	 * @return A new ByteArrayInputStream object is returned, which contains the data obtained
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ByteArrayInputStream read2Array(SocketChannel socket) throws IOException {
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		ByteBuffer buff=ByteBuffer.allocate(1024);
		while((socket.read(buff))>0) {
			bao.write(buff.array());
			buff.compact();
		}
		buff.clear();
		bao.close();
		return new ByteArrayInputStream(bao.toByteArray());
	}
	
	/**
	 * Read data from socket to file. Do not do this without taking packet header
	 *
	 * @param socket -to read/write data
	 * @param path   -file path you want to write to
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void read2File(SocketChannel socket, String path) throws IOException {
		FileOutputStream fo=new FileOutputStream(new File(path), true);
		ByteBuffer buff=ByteBuffer.allocate(1024);
		while((socket.read(buff))>0) {
			fo.write(buff.array());
			buff.compact();
		}
	}
	
	/**
	 * Write data to socket with header.
	 *
	 * @param socket     -to read/write data
	 * @param head       -packet header
	 * @param data       -the packet you want to send
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write2Socket(SocketChannel socket, byte head, byte[] data) throws IOException {
		ByteBuffer packet=ByteBuffer.wrap(new byte[data.length+1]);
		packet.put(head);
		packet.put(data);
		packet.flip();
		socket.write(packet);
		packet.clear();
	}
	
	/**
	 * Write data to socket without header.
	 *
	 * @param socket     -to read/write data
	 * @param data       -the packet you want to send
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write2Socket(SocketChannel socket, byte[] data) throws IOException {
		ByteBuffer packet=ByteBuffer.wrap(data);
		packet.flip();
		socket.write(packet);
		packet.clear();
	}
}
