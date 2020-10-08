/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.receive.DeviceRegisteredCollector;
import model.Device;


// TODO: Auto-generated Javadoc
/**
 * The Class DeviceListView. First Frame is displayed when server started
 */
public class DeviceListView extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The pane items. */
	private HashMap<Device, JPanel> paneItems;
	/**
	 * Create frame.
	 */
	public DeviceListView() {

		this.paneItems=new HashMap<Device, JPanel>();
		
		setTitle("Device LAN Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 712, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel list = new JPanel();
		list.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Devices on LAN", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) list.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		initProtocols(list);
		contentPane.add(list, BorderLayout.CENTER);
		
		JPanel control = new JPanel();
		control.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Control", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(control, BorderLayout.NORTH);
		control.setLayout(new GridLayout(0, 4, 0, 0));
		
		JButton btnSendFile = new JButton("Send file");
		control.add(btnSendFile);
		
		JButton btnTurnOff = new JButton("Turn off");
		control.add(btnTurnOff);
		
		JButton btnSystemInfomation = new JButton("System infomation");
		control.add(btnSystemInfomation);
		
		JButton btnProcessRunning = new JButton("Process running");
		control.add(btnProcessRunning);
		
		JLabel label = new JLabel("");
		control.add(label);
		
		JLabel label_1 = new JLabel("");
		control.add(label_1);
		
		JLabel label_2 = new JLabel("");
		control.add(label_2);
		
		JLabel label_3 = new JLabel("");
		control.add(label_3);
		
		JLabel lblAmount = new JLabel("Amount: 0");
		lblAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		control.add(lblAmount);
		
		/*
		JPanel paneItem = new JPanel();
		paneItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
		paneItem.setBorder(new LineBorder(new Color(0, 0, 0)));
		paneItem.setToolTipText("IP");
		list.add(paneItem);
		
		JLabel lblStatus = new JLabel();
		if(false) lblStatus.setIcon(new ImageIcon("resource/registered-device.png"));
		else lblStatus.setIcon(new ImageIcon("resource/unregistered-device.png"));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		JCheckBox chckbxDevice = new JCheckBox("000.000.000.000");
		GroupLayout gl_paneIteam = new GroupLayout(paneItem);
		gl_paneIteam.setHorizontalGroup(
			gl_paneIteam.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_paneIteam.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_paneIteam.createParallelGroup(Alignment.TRAILING)
						.addComponent(chckbxDevice, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
						.addComponent(lblStatus, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_paneIteam.setVerticalGroup(
			gl_paneIteam.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_paneIteam.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxDevice)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		paneItem.setLayout(gl_paneIteam);*/
		
	}
	
	/**
	 * Inits the protocols.
	 *
	 * @param list the list
	 */
	private void initProtocols(JPanel list) {
		// TODO Auto-generated method stub
		DeviceRegisteredCollector.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getPropertyName().equalsIgnoreCase("device.connected")) {
					addDevice(list, (Device) arg0.getNewValue());
				}
				if(arg0.getPropertyName().equalsIgnoreCase("device.disconnected")) {
					removeDevice(list, (Device) arg0.getOldValue());
				}
			}
		});
	}
	
	/**
	 * Removes the device.
	 *
	 * @param list the list
	 * @param device the device
	 */
	private void removeDevice(JPanel list, Device device) {
		if(device==null) return;
		System.out.println("OK");
		list.remove(this.paneItems.get(device));
		this.paneItems.remove(device);
		list.getRootPane().revalidate();
		list.getRootPane().repaint();
	}
	
	/**
	 * Adds the device.
	 *
	 * @param list the list
	 * @param device the device
	 */
	private void addDevice(JPanel list, Device device) {
		JPanel paneItem = new JPanel();
		paneItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
		paneItem.setBorder(new LineBorder(new Color(0, 0, 0)));
		paneItem.setToolTipText(device.getAddress().getHostAddress());
		list.add(paneItem);
		
		JLabel lblStatus = new JLabel();
		if(device.isRegistered()) lblStatus.setIcon(new ImageIcon("resource/registered-device.png"));
		else lblStatus.setIcon(new ImageIcon("resource/unregistered-device.png"));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		JCheckBox chckbxDevice = new JCheckBox(device.getAddress().getHostName());
		chckbxDevice.setEnabled(device.isRegistered());
		GroupLayout gl_paneIteam = new GroupLayout(paneItem);
		gl_paneIteam.setHorizontalGroup(
			gl_paneIteam.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_paneIteam.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_paneIteam.createParallelGroup(Alignment.TRAILING)
						.addComponent(chckbxDevice, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
						.addComponent(lblStatus, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_paneIteam.setVerticalGroup(
			gl_paneIteam.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_paneIteam.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxDevice)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		paneItem.setLayout(gl_paneIteam);
		this.paneItems.put(device, list);
	}
}
