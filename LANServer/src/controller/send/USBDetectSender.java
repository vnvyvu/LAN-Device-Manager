/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.send;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;

import com.amihaiemil.eoyaml.YamlMapping;

import controller.PacketHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class USBDetectSender.
 */
public class USBDetectSender {
	
	/**
	 * Turn on USB detect function in client side.
	 *
	 * @param socketChannel the socket channel
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void on(SocketChannel socketChannel) throws UnsupportedEncodingException, IOException {
		YamlMapping config=PacketHandler.getConfig("config.yml");
		String data=config.string("usbmode");
		PacketHandler.write2Socket(socketChannel, (byte)7, data.getBytes("UTF-8"));
	}
	
	/**
	 * Turn off USB detect function in client side.
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void off(SocketChannel socketChannel) throws IOException {
		PacketHandler.writeHead(socketChannel, (byte)8);
	}
}
