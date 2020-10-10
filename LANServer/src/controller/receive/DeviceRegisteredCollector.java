/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import controller.Utils;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * Class DeviceRegisteredCollector. it includes the function to read Device object sent by client
 * Device-SocketChannel relationship is one-one, but java has no data structure
 * that can implement that, i have to use two HashMap, also BiMap
 * but i'm limited to using external library
 */
public class DeviceRegisteredCollector {
	
	/** The devices collection. */
	public static HashMap<SocketChannel, Device> devices=new HashMap<SocketChannel, Device>();
	
	/** The socket collection */
	public static HashMap<Device, SocketChannel> sockets=new HashMap<Device, SocketChannel>();
	
	/** The property change support. Use to fire an event when a value changes*/
	protected static PropertyChangeSupport deviceEvents=new PropertyChangeSupport(new DeviceRegisteredCollector());
	
	/**
	 * Adds the property change listener.
	 *
	 * @param listener the listener
	 */
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
        deviceEvents.addPropertyChangeListener(listener);
    }

	/**
	 * Read from socket. Take Device object sent by client
	 * and fire event that Frame needs to capture it to handle
	 *
	 * @param key -channel's key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel=(SocketChannel) key.channel();
		socketChannel.configureBlocking(false);
		Device device=null;
		ByteBuffer buff=ByteBuffer.allocate(1024);
		try {
			ByteArrayInputStream bin=Utils.read2Array(socketChannel);
			ObjectInputStream out=new ObjectInputStream(bin);
			device=(Device) out.readObject();
			out.close();
			bin.close();
			deviceEvents.firePropertyChange("device.connected", null, device);
			devices.put(socketChannel, device);
			sockets.put(device, socketChannel);
			socketChannel.register(key.selector(), SelectionKey.OP_WRITE);
		} catch (ClassNotFoundException e) {
			close(socketChannel);
		} finally {
			buff.clear();
		}
	}
	
	/**
	 * Close connection. Simply close the connection 
	 * do something to process when the error has occurred
	 *
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void close(SocketChannel socketChannel) throws IOException {
		deviceEvents.firePropertyChange("device.disconnected", devices.get(socketChannel), null);
		sockets.remove(devices.get(socketChannel));
		devices.remove(socketChannel);
		socketChannel.close();
	}
}
