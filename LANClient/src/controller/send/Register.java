/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.send;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import controller.Utils;
import model.Device;
import oshi.SystemInfo;

// TODO: Auto-generated Javadoc
/**
 * Static class Register. This class includes the function of registering with server, that it is a device on the LAN and can be managed
 */
public class Register {
	
	/**
	 * Write/Send information of the internal of this device to channel.
	 *
	 * @param selector -to register a new key
	 * @param socketChannel -to send/receive data
	 * @param head -packet/data header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write(Selector selector, SocketChannel socketChannel, byte head) throws IOException{
		SystemInfo si=new SystemInfo();
		String token[]=si.getHardware().getComputerSystem().toString().split("(, )|=");
		Device device=new Device(getCorrectLocalIP(), si.getOperatingSystem().toString()+" "+
				si.getOperatingSystem().getBitness()+" bits", token[1], token[3], true);
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		ObjectOutputStream out;
		out = new ObjectOutputStream(bao);
		out.writeObject(device);
		try{
			Utils.write2Socket(socketChannel, head, bao.toByteArray());
		}catch (IOException e) {
			// TODO: handle exception
			socketChannel.close();
		}
		out.close();
		bao.close();
	}
	
	/**
	 * Gets the correct local IP.
	 *
	 * @return the correct IP of this machine or null if UDP error
	 * @throws SocketException the socket exception
	 */
	public static InetAddress getCorrectLocalIP() throws SocketException {
		InetAddress i=null;
		try{
			DatagramSocket socket=new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 80);
			i=socket.getLocalAddress();
			socket.close();
		} catch (SocketException | UnknownHostException e) {
		}
		return i;
	}
}
