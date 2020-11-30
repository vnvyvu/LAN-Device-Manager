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
 * The Class DriverMonitorSender.
 */
public class DriverMonitorSender {
	
	/**
	 * Turn on driver monitor.
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void on(SocketChannel socketChannel) throws IOException {
		PacketHandler.writeHead(socketChannel, (byte)11);
	}
	
	/**
	 * Turn off driver monitor.
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void off(SocketChannel socketChannel) throws IOException {
		PacketHandler.writeHead(socketChannel, (byte)12);
	}
}
