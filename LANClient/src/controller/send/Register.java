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
import java.nio.channels.SocketChannel;

import controller.PacketHandler;
import controller.WMIC;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * Static class Register. This class includes the function of registering with server, that it is a device on the LAN and can be managed
 */
public class Register {
	
	/**
	 * Write(Send) information this device to channel.
	 *
	 * @param key -channel's key
	 * @param head -packet/data header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean write(SocketChannel socketChannel, byte head) throws IOException{
		String token[]=WMIC.get("computersystem", "Manufacturer,Model").split("\s{2,}");
		InetAddress address=getCorrectLocalIP();
		Device device=new Device(address.getHostName(), address.getHostAddress(), 
				WMIC.get("os", "Caption,Version,OSArchitecture").replaceAll("\s{2,}", "|"), 
				token[0], token[1]);
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		ObjectOutputStream out;
		out = new ObjectOutputStream(bao);
		out.writeObject(device);
		PacketHandler.write2Socket(socketChannel, head, bao.toByteArray());
		out.close();
		bao.close();
		return false;
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
