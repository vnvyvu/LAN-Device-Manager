/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import controller.PacketHandler;
import controller.WMIC;
import controller.receive.ShutDownReceiver.TurnOffMode;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessConfigReceiver.
 */
public class ProcessConfigReceiver {
	
	/** The worker. */
	private static ScheduledExecutorService worker=Executors.newSingleThreadScheduledExecutor();
	
	/** The mode. */
	private static int mode;
	
	/** The blackist. */
	private static String blackist;
	
	/**
	 * Read config from server.
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel, int length) throws IOException {
		String data[]=new String(PacketHandler.read2Array(socketChannel, length)).split("\\?");
		mode=Integer.parseInt(data[0]);
		blackist=data[1];
		on(socketChannel);
		return false;
	}
	
	/**
	 * Turn on worker. Get all current process and kill matched
	 *
	 * @param socketChannel the socket channel
	 */
	public static void on(SocketChannel socketChannel) {
		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashSet<String> ps;
				try {
					ps = new HashSet<String>(WMIC.getLines("process", "name")
							.filter(l->l.matches(".+\\..+"))
							.map(l->l.trim().toLowerCase().substring(0, l.lastIndexOf('.')))
							.collect(Collectors.toSet()));
					ps.forEach(i->{
						if(blackist.contains(i)) {
							try {
								action(i);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}, 0, 8, TimeUnit.SECONDS);
	}
	
	/**
	 * Turn off worker.
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 */
	public static boolean off() {
		if(!worker.isShutdown()) worker.shutdown();
		return false;
	}
	
	/**
	 * Select next action by mode variable.
	 *
	 * @param pName the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void action(String pName) throws IOException {
		switch (mode) {
		case 1:
			Runtime.getRuntime().exec("taskkill.exe /F /IM \""+pName+".exe\"");
			break;
		case 2:
			ShutDownReceiver.shutdown(TurnOffMode.SHUTDOWN);
			break;
		default:
			break;
		}
	}
}
