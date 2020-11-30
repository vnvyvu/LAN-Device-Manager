/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import controller.PacketHandler;
import controller.WMIC;
import model.Client;

// TODO: Auto-generated Javadoc
/**
 * The Class DriverMonitorReceiver.
 */
public class DriverMonitorReceiver {
	
	/** The drivers. */
	public static Set<String> baseDrivers;
	
	/** The future of task, which must be scheduled. */
	private static ScheduledFuture<?> future;
	
	/**
	 * On. Initiative drivers and send to server if drivers is null
	 * If any driver status is changed, worker will notify to server
	 * Driver status can be either injected or ejected
	 *
	 * @param socketChannel the socket channel
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean on(SocketChannel socketChannel) throws IOException {
		if(baseDrivers==null) {
			baseDrivers=WMIC.getLines("path Win32_PnPEntity","name,manufacturer,pnpclass,description").map(l->l.trim().replaceAll("\s{2,}", "|")).collect(Collectors.toSet());
			writeDrivers(socketChannel, baseDrivers, (byte)4);
		}
		future=Client.worker.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					Set<String> currentDrivers=WMIC.getLines("path Win32_PnPEntity", "name,manufacturer,pnpclass,description").map(l->l.trim().replaceAll("\s{2,}", "|")).collect(Collectors.toSet());
					if(baseDrivers.size()<currentDrivers.size()) {
						currentDrivers.removeAll(baseDrivers);
						writeDrivers(socketChannel, currentDrivers, (byte)4);
						baseDrivers.addAll(currentDrivers);
					}else if(baseDrivers.size()>currentDrivers.size()) {
						Set<String> temp=baseDrivers.stream().collect(Collectors.toSet());
						temp.removeAll(currentDrivers);
						writeDrivers(socketChannel, temp, (byte)5);
						baseDrivers.removeAll(temp);
					}
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}, 0, 3500, TimeUnit.MILLISECONDS);
		return false;
	}
	
	/**
	 * Off.
	 *
	 * @param socketChannel the socket channel
	 * @return true, if successful
	 */
	public static boolean off(SocketChannel socketChannel) {
		if(future!=null) {
			future.cancel(false);
			future=null;
		}
		return false;
	}
	
	/**
	 * Write drivers.
	 *
	 * @param socketChannel the socket channel
	 * @param d the d
	 * @param head the head
	 */
	private static void writeDrivers(SocketChannel socketChannel, Set<String> d, byte head) {
		d.forEach(v->{
			try {
				int c=0;
				for(int i=0;i<v.length();i++) {
					if(v.charAt(i)=='|') c++;
				}
				if(c==3) {
					PacketHandler.write2Socket(socketChannel, head, v.getBytes());
				}else if(c==2) {
					v=v.replaceFirst("\\|", "| |");
					PacketHandler.write2Socket(socketChannel, head, v.getBytes());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
