package view;

import java.io.IOException;

import model.Server;

public class ServerRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		java.awt.EventQueue.invokeLater(new Runnable() {
	        public void run() {
	        	try {
					Server server=new Server(1333);
					Thread thread=new Thread(server);
					thread.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            DeviceListForm view=new DeviceListForm();
	            view.setLocationRelativeTo(null);
	            view.setVisible(true);
	        }
	    });
	}

}
