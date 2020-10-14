/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import controller.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class TurnOffReceiver.
 */
public class TurnOffReceiver {
	
	/**
	 * The Enum TurnOffMode.
	 */
	enum TurnOffMode{
		
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
	 * Read request(head) from socket and response.
	 *
	 * @param socketChannel the socket channel
	 * @return false for always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel) throws IOException {
		String info[]=new String(Utils.read2Array(socketChannel, 20)).split("\\?");
		TurnOffMode mode=TurnOffMode.valueOf(info[0]);
		long delay=Long.parseLong(info[1]);
		Executors.newScheduledThreadPool(1).schedule(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (mode) {
				case HIBERNATE:
					try {
						Runtime.getRuntime().exec("shutdown -h -f");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case RESTART:
					try {
						Runtime.getRuntime().exec("shutdown -r -f");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case SHUTDOWN:
					try {
						Runtime.getRuntime().exec("shutdown -p -f");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case LOG_OFF:
					try {
						Runtime.getRuntime().exec("shutdown -l -f");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}, delay, TimeUnit.MILLISECONDS);
		return false;
	}
}