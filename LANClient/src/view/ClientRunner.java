package view;

import java.io.IOException;

import controller.VNC;
import model.Client;

public class ClientRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client;
		try {
			if(!VNC.isInstalled()) VNC.install();
			client = new Client("localhost", 1333);
			Thread thread=new Thread(client);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
