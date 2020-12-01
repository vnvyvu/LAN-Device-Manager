/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.send;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.PacketHandler;
import view.SendFileForm;

public class FileSender {
	
	/** The byte count. Store amount of data sent*/
	public static HashMap<SocketChannel, Long> byteCount=new HashMap<SocketChannel, Long>();
	
	/** The file. */
	public static RandomAccessFile f;
	
	/** Progress 1%*/
	
	public static long count=0;
	
	/**
	 * Write info including path, file name, length
	 *
	 * @param socketChannel the socket channel
	 * @param clientPath the file path in client
	 * @param file the file will be send
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeInfo(SocketChannel socketChannel, File file) throws IOException {
		String temp=file.getName()+"?"+file.length();
		f=new RandomAccessFile(file, "r");
		byteCount.put(socketChannel, (long)0);
		ScheduledExecutorService worker=Executors.newSingleThreadScheduledExecutor();
		worker.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					SendFileForm.progressSendFile.setValue((int)(count*100/f.length()));
					if(count==f.length()) {
						worker.shutdown();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 1, 5, TimeUnit.SECONDS);
		PacketHandler.write2Socket(socketChannel, (byte)2, temp.getBytes("UTF-8"));
	}
	
	/**
	 * Write 1kb/request. Fire event when done
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean write(SocketChannel socketChannel) throws IOException {
		byte[] data;
		count=byteCount.get(socketChannel);
		f.seek(count);
		if(count+1024<f.length()) {
			data=new byte[1024];
			byteCount.put(socketChannel, count+data.length);
		}else{
			data=new byte[(int) (f.length()-count)];
			count=f.length();
			byteCount.remove(socketChannel);
		}
		f.read(data);
		PacketHandler.write2Socket(socketChannel, (byte)3, data);
		return false;
	}
}
