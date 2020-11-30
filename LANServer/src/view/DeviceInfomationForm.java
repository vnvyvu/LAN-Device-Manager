/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Device;

public class DeviceInfomationForm extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtOS;
	private JTextField txtModel;
	private JTextField txtAddress;
	private JTextField txtManufacturer;
	private JButton btnDisplayScreen;
	private JButton btnControlDevice;
	private JButton btnDrivers;

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
		setTitle(device.getName());
		setBounds(100, 100, 540, 260);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(6, 2, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Operating System");
		contentPane.add(lblNewLabel);
		
		txtOS = new JTextField();
		txtOS.setText(device.getOs());
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
		txtAddress.setText(device.getIp());
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
		
		btnDisplayScreen = new JButton("Display device's screen");
		btnDisplayScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ScreenMonitorForm smf=new ScreenMonitorForm(device);
				smf.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						btnDisplayScreen.setEnabled(true);
						btnControlDevice.setEnabled(true);
					}
				});
				btnDisplayScreen.setEnabled(false);
				btnControlDevice.setEnabled(false);
				smf.setVisible(true);
			}
		});
		contentPane.add(btnDisplayScreen);
		
		btnControlDevice = new JButton("Control this device");
		btnControlDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeviceControllerForm dcf=new DeviceControllerForm(device);
				dcf.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						btnDisplayScreen.setEnabled(true);
						btnControlDevice.setEnabled(true);
					}
				});
				btnDisplayScreen.setEnabled(false);
				btnControlDevice.setEnabled(false);
				dcf.setVisible(true);
			}
		});
		contentPane.add(btnControlDevice);
		
		btnDrivers = new JButton("Drivers");
		btnDrivers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DriverListForm dlf=new DriverListForm(device);
				dlf.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						btnDrivers.setEnabled(true);
					}
				});
				btnDrivers.setEnabled(false);
				dlf.setVisible(true);
			}
		});
		contentPane.add(btnDrivers);

		setLocationRelativeTo(null);
	}

}
