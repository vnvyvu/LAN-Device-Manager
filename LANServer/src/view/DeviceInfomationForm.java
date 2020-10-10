package view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Device;

public class DeviceInfomationForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtOS;
	private JTextField txtModel;
	private JTextField txtAddress;
	private JTextField txtManufacturer;

	/**
	 * Create the frame.
	 */
	public DeviceInfomationForm(Device device, JLabel lblStatus) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				lblStatus.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						(new DeviceInfomationForm(device, lblStatus)).setVisible(true);
						lblStatus.removeMouseListener(this);
					}
				});
			}
		});
		setTitle(device.getAddress().getHostName());
		setBounds(100, 100, 540, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Operating System");
		contentPane.add(lblNewLabel);
		
		txtOS = new JTextField();
		txtOS.setText(device.getOS());
		txtOS.setEditable(false);
		contentPane.add(txtOS);
		txtOS.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Manufacturer");
		contentPane.add(lblNewLabel_2);
		
		txtManufacturer = new JTextField();
		txtManufacturer.setText(device.getManufacturer());
		txtManufacturer.setEditable(false);
		contentPane.add(txtManufacturer);
		txtManufacturer.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Address");
		contentPane.add(lblNewLabel_1);
		
		txtAddress = new JTextField();
		txtAddress.setText(device.getAddress().getHostAddress());
		txtAddress.setEditable(false);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Model");
		contentPane.add(lblNewLabel_3);
		
		txtModel = new JTextField();
		txtModel.setText(device.getModel());
		txtModel.setEditable(false);
		contentPane.add(txtModel);
		txtModel.setColumns(10);
	}

}
