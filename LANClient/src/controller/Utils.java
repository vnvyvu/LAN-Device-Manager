/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */

package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import controller.receive.FileReceiver;
import controller.send.Register;

// TODO: Auto-generated Javadoc
/**
 * A static class. It includes commonly used functions for easy reuse
 */
public class Utils {
	
	/**
	 * selectFunction will choose the appropriate function depend on sent packet header.
	 *
	 * @param key           -channel's key
	 * @param head          -beginning of the received packet
	 * @return true, if next action is writeable
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean selectFunction(SelectionKey key, byte head) throws IOException {
		switch (head) {
			case (byte)1:
				return Register.write((SocketChannel) key.channel(), head);
			case (byte)2: 
				return FileReceiver.readInfo((SocketChannel) key.channel());
			case (byte)3: 
				return FileReceiver.read((SocketChannel) key.channel());
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
		return false;
	}
	
	/**
	 * Read 1 byte from socket, it's packet header
	 *
	 * @param socketChannel the socket channel
	 * @return the byte is packet header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte readHead(SocketChannel socketChannel) throws IOException {
		ByteBuffer buff=ByteBuffer.allocate(1);
		socketChannel.read(buff);
		buff.flip();
		byte res=buff.array()[0];
		return res;
	}
	
	/**
	 * Read size(byte) from socket to array. Estimates the buffer size based on the sender's function
	 * Do not do this without taking packet header
	 *
	 * @param socketChannel the socket channel
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
	 * Write data to socket with header. Buffer needs to be flip before writing. 
	 * I don't know why...
	 *
	 * @param socket     -to read/write data
	 * @param head       -packet header
	 * @param data       -the packet to send
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
}
