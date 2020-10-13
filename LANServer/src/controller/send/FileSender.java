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

import controller.Events;
import controller.Utils;

public class FileSender {
	
	/** The byte count. Store amount of data sent*/
	public static HashMap<SocketChannel, Long> byteCount=new HashMap<SocketChannel, Long>();
	
	/** The file. */
	public static RandomAccessFile f;
	
	/**
	 * Write info including path, file name, length
	 *
	 * @param socketChannel the socket channel
	 * @param clientPath the file path in client
	 * @param file the file will be send
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeInfo(SocketChannel socketChannel, String clientPath, File file) throws IOException {
		String temp=clientPath+"\\"+file.getName()+"?"+file.length();
		f=new RandomAccessFile(file, "r");
		byteCount.put(socketChannel, (long)0);
		Utils.write2Socket(socketChannel, (byte)2, temp.getBytes("UTF-8"));
	}
	
	/**
	 * Write 32kb/request. Fire event when done
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean write(SocketChannel socketChannel) throws IOException {
		byte[] data;
		long count=byteCount.get(socketChannel);
		f.seek(count);
		if(count+1024<f.length()) {
			data=new byte[1024];
			byteCount.put(socketChannel, count+data.length);
			if(count%9015==0) Events.event.firePropertyChange("progress", 0, (int)(count*100/f.length()));
		}else{
			data=new byte[(int) (f.length()-count)];
			byteCount.remove(socketChannel);
			Events.event.firePropertyChange("progress", 0, 100);
		}
		f.read(data);
		Utils.write2Socket(socketChannel, (byte)3, data);
		return false;
	}
}
