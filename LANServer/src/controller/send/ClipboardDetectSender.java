/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.send;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import controller.PacketHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ClipboarDetectSender.
 */
public class ClipboardDetectSender {
	
	/**
	 * On function.
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void on(SocketChannel socketChannel) throws IOException{
		PacketHandler.writeHead(socketChannel, (byte)9);
	}
	
	/**
	 * Off function.
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void off(SocketChannel socketChannel) throws IOException {
		PacketHandler.writeHead(socketChannel, (byte)10);
	}
}
