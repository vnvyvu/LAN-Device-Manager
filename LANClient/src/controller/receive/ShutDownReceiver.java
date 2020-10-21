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

import controller.PacketHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class TurnOffReceiver.
 */
public class ShutDownReceiver {
	
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
	 * Read request(head) from socket and response. Shut down this pc following mode
	 *
	 * @param socketChannel the socket channel
	 * @return false for always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel, int length) throws IOException {
		String info[]=new String(PacketHandler.read2Array(socketChannel, length)).split("\\?");
		TurnOffMode mode=TurnOffMode.valueOf(info[0]);
		long delay=Long.parseLong(info[1].trim());
		Executors.newScheduledThreadPool(1).schedule(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					shutdown(mode);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, delay, TimeUnit.MILLISECONDS);
		return false;
	}
	
	public static void shutdown(TurnOffMode mode) throws IOException {
		switch (mode) {
		case HIBERNATE:
			Runtime.getRuntime().exec("shutdown.exe -h -f");
			break;
		case RESTART:
			Runtime.getRuntime().exec("shutdown.exe -r -f");
			break;
		case SHUTDOWN:
			Runtime.getRuntime().exec("shutdown.exe -p -f");
			break;
		case LOG_OFF:
			Runtime.getRuntime().exec("shutdown.exe -l -f");
		default:
			break;
		}
	}
}