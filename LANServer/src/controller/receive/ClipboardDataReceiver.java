/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import controller.Events;
import controller.PacketHandler;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class ClipboardDataReceiver.
 */
public class ClipboardDataReceiver {
	
	/**
	 * Read client clipboard from socket channel.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel, int length) throws IOException {
		String data=new String(PacketHandler.read2Array(socketChannel, length)).trim();
		Device device=DeviceRegisteredReceiver.devices.get(socketChannel);
		Events.event.firePropertyChange("clipboard.on.received", "", data);
		FileWriter w=new FileWriter(new File(PacketHandler.getConfig("config.yml").string("clipboard-output-file")), true);
		w.write("["+device.getIp()+":"+device.getName()+"]"+data+"\n");
		w.close();
		return false;
	}
}
