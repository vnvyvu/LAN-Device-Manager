package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.send.TurnOffSender;
import controller.send.TurnOffSender.TurnOffMode;
import model.Device;

public class TurnOffForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2553222153471850824L;
	
	private JPanel contentPane;
	private JTextField txtDelay;

	/**
	 * Create the frame.
	 */
	public TurnOffForm(HashMap<Device, SocketChannel> selectedDevices, JFrame deviceList) {
		setAlwaysOnTop(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				deviceList.setEnabled(true);
			}
		});
		
		setTitle("Turn Off Modes");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 140);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Delay (miliseconds)");
		contentPane.add(lblNewLabel);
		
		txtDelay = new JTextField();
		txtDelay.setText("0");
		contentPane.add(txtDelay);
		txtDelay.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Mode");
		contentPane.add(lblNewLabel_1);
		
		JComboBox<Object> comboOffMode = new JComboBox<Object>();
		comboOffMode.setModel(new DefaultComboBoxModel<Object>(TurnOffMode.values()));
		contentPane.add(comboOffMode);
		
		JLabel lblNewLabel_2 = new JLabel("");
		contentPane.add(lblNewLabel_2);
		
		JButton btnTurnOff = new JButton("Turn Off");
		btnTurnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				long delay;
				try{
					delay=Long.parseLong(txtDelay.getText());
				}catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Format error: "+txtDelay.getText());
					return;
				}
				selectedDevices.forEach((device, socketChannel)->{
					try {
						TurnOffSender.write(socketChannel, (TurnOffMode) comboOffMode.getSelectedItem(), delay);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
		});
		contentPane.add(btnTurnOff);
		
	}

}
