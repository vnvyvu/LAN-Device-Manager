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

import controller.Utils;
import controller.receive.ShutDownReceiver.TurnOffMode;
import oshi.SystemInfo;

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
	public static boolean read(SocketChannel socketChannel) throws IOException {
		String data[]=new String(Utils.read2Array(socketChannel, 512)).split("\\?");
		mode=Integer.parseInt(data[0]);
		blackist=data[1];
		on(socketChannel);
		return false;
	}
	
	/**
	 * Turn on worker.
	 *
	 * @param socketChannel the socket channel
	 */
	public static void on(SocketChannel socketChannel) {
		SystemInfo si=new SystemInfo();
		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashSet<String> ps=new HashSet<String>(
						si.getOperatingSystem().getProcesses()
						.stream().map(o->o.getName().toLowerCase()).collect(Collectors.toSet()));
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
			}
		}, 0, 7, TimeUnit.SECONDS);
	}
	
	/**
	 * Turn off worker.
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 */
	public static boolean off(SocketChannel socketChannel) {
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
