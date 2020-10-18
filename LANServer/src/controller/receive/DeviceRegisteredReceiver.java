/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import controller.Events;
import controller.Utils;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * Class DeviceRegisteredCollector. it includes the function to read Device object sent by client
 * Device-SocketChannel relationship is one-one, but java has no data structure
 * that can implement that, i have to use two HashMap, also BiMap
 * but i'm limited to using external library
 */
public class DeviceRegisteredReceiver {
	
	/** The devices collection. */
	public static HashMap<SocketChannel, Device> devices=new HashMap<>();
	
	/** The devices collection. */
	public static HashMap<Device, SocketChannel> sockets=new HashMap<>();

	/**
	 * Read from socket. Take Device object sent by client
	 * and fire event that Frame needs to capture it to handle
	 * I estimate size of Device object to be less than 1024 bytes
	 * if that's fine, device.connected event will be fired
	 *
	 * @param key -channel's key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel) throws IOException {
		Device device=null;
		try {
			ByteArrayInputStream bin=new ByteArrayInputStream(Utils.read2Array(socketChannel, 256));
			ObjectInputStream out=new ObjectInputStream(bin);
			device=(Device) out.readObject();
			out.close();
			bin.close();
			Events.event.firePropertyChange("device.connected", null, device);
			devices.put(socketChannel, device);
			sockets.put(device, socketChannel);
		} catch (ClassNotFoundException e) {
			close(socketChannel);
		}
		return false;
	}
	
	/**
	 * Close connection. Simply close the connection 
	 * do something to process when the error has occurred
	 * device.disconnected event will be fired
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void close(SocketChannel socketChannel) throws IOException {
		Events.event.firePropertyChange("device.disconnected", devices.get(socketChannel), null);
		sockets.remove(devices.get(socketChannel));
		devices.remove(socketChannel);
		socketChannel.close();
	}
}
