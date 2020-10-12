/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import controller.Utils;

public class FileReceiver {
	
	/** The file receive. */
	public static File f;
	
	/** The file receive length. */
	public static long fileLength=0;
	
	/**
	 * Read file information including file name, length. Initial file path.
	 * And request sender to send file
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean readInfo(SocketChannel socketChannel) throws IOException {
		String temp[]=new String(Utils.read2Array(socketChannel, 1024), "UTF-8").split("\\?");
		fileLength=Long.parseLong(temp[1].trim());
		f=new File(temp[0]);
		if(f.exists()) f.delete();
		Utils.writeHead(socketChannel, (byte)2);
		return false;
	}
	
	/**
	 * Read a part of file(8kb) and append to file. 
	 * If not the last part of file, ask sender to continue sending
	 *
	 * @param socketChannel the socket channel
	 * @return always false to always at readable state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean read(SocketChannel socketChannel) throws IOException {
		ByteBuffer buff=ByteBuffer.allocate(32768);
		socketChannel.read(buff);
		FileOutputStream fo=new FileOutputStream(f, true);
		fo.write(buff.array());
		buff.clear();
		fo.close();
		fileLength-=buff.array().length;
		if(fileLength>0) Utils.writeHead(socketChannel, (byte)2);
		return false;
	}
}
