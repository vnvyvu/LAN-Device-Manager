/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import controller.Events;
import controller.PacketHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class DriverMonitorReceiver.
 */
public class DriverMonitorReceiver {
	
	/** The drivers. */
	public volatile static HashMap<SocketChannel, ArrayList<String>> drivers=new HashMap<SocketChannel, ArrayList<String>>();
	
	/**
	 * Read driver injected.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean readDriverInjected(SocketChannel socketChannel, int length) throws IOException {
		String driverInfo=new String(PacketHandler.read2Array(socketChannel, length));
		if(drivers.containsKey(socketChannel)) {
			ArrayList<String> temp=drivers.get(socketChannel);
			temp.add(driverInfo);
			drivers.put(socketChannel, temp);
		}else {
			drivers.put(socketChannel, new ArrayList<String>(Arrays.asList(driverInfo)));
		}
		Events.event.firePropertyChange("driver.injected", socketChannel, driverInfo);
		return false;
	}
	
	/**
	 * Read driver ejected.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean readDriverEjected(SocketChannel socketChannel, int length) throws IOException {
		String driverInfo=new String(PacketHandler.read2Array(socketChannel, length));
		if(drivers.containsKey(socketChannel)) {
			ArrayList<String> temp=drivers.get(socketChannel);
			temp.remove(driverInfo);
			drivers.put(socketChannel, temp);
		}
		Events.event.firePropertyChange("driver.ejected", socketChannel, driverInfo);
		return false;
	}
}
