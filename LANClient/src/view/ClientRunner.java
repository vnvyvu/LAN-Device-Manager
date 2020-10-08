package view;

import java.io.IOException;

import model.Client;

public class ClientRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client;
		try {
			client = new Client("127.0.0.1", 1333);
			Thread thread=new Thread(client);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
