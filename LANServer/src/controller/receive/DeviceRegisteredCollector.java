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
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import controller.Utils;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * Class DeviceRegisteredCollector. it includes the function to read Device object sent by client
 */
public class DeviceRegisteredCollector {
	
	/** The devices collection. */
	public static HashMap<SocketChannel, Device> devices=new HashMap<SocketChannel, Device>();
	
	/** The property change support. Use to fire an event when a value changes*/
	protected static PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport(new DeviceRegisteredCollector());
	
	/**
	 * Adds the property change listener.
	 *
	 * @param listener the listener
	 */
	public static void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

	/**
	 * Read from socket. Take Device object sent by client
	 * and fire event that Frame needs to capture it to handle
	 *
	 * @param selector the selector
	 * @param socketChannel the socket channel
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void read(Selector selector, SocketChannel socketChannel) throws IOException {
		Device device=null;
		ByteBuffer buff=ByteBuffer.allocate(1024);
		try {
			ByteArrayInputStream bin=Utils.read2Array(socketChannel);
			ObjectInputStream out=new ObjectInputStream(bin);
			device=(Device) out.readObject();
			out.close();
			bin.close();
			propertyChangeSupport.firePropertyChange("device.connected", null, device);
			devices.put(socketChannel, device);
			socketChannel.register(selector, SelectionKey.OP_WRITE);
		} catch (IOException | ClassNotFoundException e) {
			propertyChangeSupport.firePropertyChange("device.disconnected", devices.get(socketChannel), null);
			devices.remove(socketChannel);
			e.printStackTrace();
			socketChannel.close();
		} finally {
			buff.clear();
		}
	}
	
}
