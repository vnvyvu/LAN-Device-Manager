/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import controller.PacketHandler;
import controller.WMIC;
import controller.receive.ShutDownReceiver.TurnOffMode;
import model.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessConfigReceiver.
 */
public class ProcessConfigReceiver{
	
	/** The future of task, which must be scheduled. */
	private static ScheduledFuture<?> future;
	
	/** The mode. */
	private static int mode;
	
	/** The blackist. */
	private static String blackist;
	
	/**  The task must be scheduled. */
	private static Runnable task;
	
	/**
	 * Read config from server.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel, int length) throws IOException {
		String data[]=new String(PacketHandler.read2Array(socketChannel, length)).split("\\?");
		mode=Integer.parseInt(data[0]);
		blackist=data[1];
		initTask();
		on(socketChannel);
		return false;
	}
	
	/**
	 * Init the task. Get all process then compare with blacklist
	 */
	private static void initTask() {
		// TODO Auto-generated method stub
		if(task==null) {
			task=new Runnable() {
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
			};
		}
	}

	/**
	 * Turn on worker. Get all current process and kill matched
	 *
	 * @param socketChannel the socket channel
	 */
	public static void on(SocketChannel socketChannel) {
		future=Client.worker.scheduleAtFixedRate(task, 0, 7, TimeUnit.SECONDS);
	}
	
	/**
	 * Turn off worker.
	 *
	 * @return always false to always at readable state
	 */
	public static boolean off() {
		if(future!=null) {
			future.cancel(false);
			future=null;
		}
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
