/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import controller.PacketHandler;
import controller.WMIC;
import controller.receive.ShutDownReceiver.TurnOffMode;

// TODO: Auto-generated Javadoc
/**
 * The Class USBDetectReceiver.
 */
public class USBDetectReceiver {
	
	/** The mode. */
	private static int mode;
	
	/** The worker. */
	private static ScheduledExecutorService worker=Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * Read config from socket.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return always false to always at readable state
	 * @throws NumberFormatException the number format exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel, int length) throws NumberFormatException, UnsupportedEncodingException, IOException {
		mode=Integer.parseInt(new String(PacketHandler.read2Array(socketChannel, length), "UTF-8"));
		on(socketChannel);
		return false;
	}
	
	/**
	 * Turn on worker to detect usb plugged.
	 *
	 * @param socketChannel the socket channel
	 */
	public static void on(SocketChannel socketChannel) {
		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Iterator<String> usb=new HashSet<String>(WMIC.getLines("path Win32_PnPEntity", "pnpclass='WPD'", "name,manufacturer,systemname")
							.map(l->l.trim().toLowerCase().replaceAll("\s{2,}", "|"))
							.collect(Collectors.toSet())).iterator();
					if(usb.hasNext()) {
						usb.next();
						usb.next();
						while(usb.hasNext()) {
							String name=usb.next().split("\\|")[1];
							switch (mode) {
							case 1:
								eject(name);
								break;
							case 2: 
								ShutDownReceiver.shutdown(TurnOffMode.SHUTDOWN);
								break;
							default:
								break;
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 8, TimeUnit.SECONDS);
	}
	
	/**
	 * Turn off worker.
	 *
	 * @return always false to always at readable state
	 */
	public static boolean off() {
		if(!worker.isShutdown()) worker.shutdown();
		return false;
	}
	
	/**
	 * Eject usb by name.
	 *
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void eject(String name) throws IOException {
		Runtime.getRuntime().exec("usbejector.exe -EjectName:\""+name+"\"");
	}
}
