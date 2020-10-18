/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.send;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import controller.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TurnOffSender.
 */
public class ShutDownSender {
	
	/**
	 * The Enum TurnOffMode.
	 */
	public enum TurnOffMode{
		
		/** The hibernate. */
		HIBERNATE, 
		
		/** The restart. */
		RESTART, 
		
		/** The shutdown. */
		SHUTDOWN,
		
		/** The log off. */
		LOG_OFF
	}
	
	/**
	 * Write request (head) to socket. Turn off client
	 *
	 * @param socketChannel the socket channel
	 * @param mode the turn off mode
	 * @param delay the delay
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write(SocketChannel socketChannel, TurnOffMode mode, long delay) throws IOException {
		String data=mode.name()+"?"+delay;
		Utils.write2Socket(socketChannel, (byte)4, data.getBytes());
	}
}
