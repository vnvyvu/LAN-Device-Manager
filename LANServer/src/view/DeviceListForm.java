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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.Events;
import controller.receive.DeviceRegisteredReceiver;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceListView. First Frame is displayed when server started
 */

public class DeviceListForm extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3119650249390951418L;

	/** The content pane. */
	private JPanel contentPane;
	
	/**  Selected devices. */
	
	private HashMap<Device, SocketChannel> selectedDevices;
	
	/** The pane items. */
	private HashMap<Device, JPanel> paneItems;
	
	/** Amount of devices. */
	private JLabel lblAmount;
	
	/** The control. */
	private JPanel control;
	/**
	 * Create frame.
	 */
	public DeviceListForm() {
		this.selectedDevices=new HashMap<Device, SocketChannel>();
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
		initEvents(list);
		contentPane.add(list, BorderLayout.CENTER);
		
		control = new JPanel();
		control.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Control", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(control, BorderLayout.NORTH);
		control.setLayout(new GridLayout(0, 4, 15, 5));
		
		JButton btnSendFile = new JButton("Send file");
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendFileBtnEvent();
			}
		});
		control.add(btnSendFile);
		
		JButton btnTurnOff = new JButton("Turn off");
		btnTurnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				turnOffBtnEvent();
			}
		});
		control.add(btnTurnOff);
		
		JButton btnUSBDetect = new JButton("USB detect");
		btnUSBDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				usbDetectBtnEvent();
			}
		});
		control.add(btnUSBDetect);
		
		JButton btnProcessManager = new JButton("Process manager");
		btnProcessManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processManagerBtnEvent();
			}
		});
		control.add(btnProcessManager);
		
		JButton btnClipboardCollector = new JButton("Clipboard collector");
		btnClipboardCollector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clipboardDetectBtnEvent();
			}
		});
		control.add(btnClipboardCollector);
		
		JLabel label = new JLabel("");
		control.add(label);
		
		JLabel label_1 = new JLabel("");
		control.add(label_1);
		
		JLabel label_2 = new JLabel("");
		control.add(label_2);
		
		lblAmount = new JLabel("Amount: 0");
		lblAmount.setFont(new Font("Tahoma", Font.BOLD, 13));
		control.add(lblAmount);
		
		
		/*JPanel paneItem = new JPanel();
		paneItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
		paneItem.setBorder(new LineBorder(new Color(0, 0, 0)));
		paneItem.setToolTipText("IP");
		list.add(paneItem);
		
		JLabel lblStatus = new JLabel();
		lblStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		lblStatus.setIcon(new ImageIcon("resource/registered-device.png"));
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		JCheckBox chckbxDevice = new JCheckBox("000.000.000.000");
		chckbxDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
	private void initEvents(JPanel list) {
		// TODO Auto-generated method stub
		Events.addPropertyChangeListener(new PropertyChangeListener() {
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
		list.remove(this.paneItems.get(device));
		this.paneItems.remove(device);
		list.revalidate();
		list.repaint();
		lblAmount.setText("Amount: "+this.paneItems.size());
	}
	
	/**
	 * Adds the device.
	 *
	 * @param list the list
	 * @param device the device
	 */
	private void addDevice(JPanel list, Device device) {
		JPanel paneItem = new JPanel();
		paneItem.setBorder(new LineBorder(new Color(0, 0, 0)));
		paneItem.setToolTipText(device.getIp());
		list.add(paneItem);
		
		JLabel lblStatus = new JLabel();
		lblStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblStatus.setIcon(new ImageIcon("resource/registered-device.png"));
		lblStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				(new DeviceInfomationForm(device, lblStatus)).setVisible(true);
				lblStatus.removeMouseListener(this);
			}
		});
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		JCheckBox chckbxDevice = new JCheckBox(device.getName());
		chckbxDevice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(chckbxDevice.isSelected()) {
					selectedDevices.put(device, DeviceRegisteredReceiver.sockets.get(device));
				}else selectedDevices.remove(device);
			}
		});
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
		this.paneItems.put(device, paneItem);
		lblAmount.setText("Amount: "+this.paneItems.size());
	}
	
	/**
	 * Send file btn event.
	 */
	private void sendFileBtnEvent() {
		if(selectedDevices.size()==0) {
			JOptionPane.showMessageDialog(null, "You need to select at least 1 device", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		JFrame temp=new SendFileForm(selectedDevices);
		temp.setVisible(true);
	}
	
	/**
	 * Turn off btn event.
	 */
	private void turnOffBtnEvent() {
		if(selectedDevices.size()==0) {
			JOptionPane.showMessageDialog(null, "You need to select at least 1 device", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		JFrame temp=new ShutDownForm(selectedDevices);
		temp.setVisible(true);
	}
	
	/**
	 * Usb detect btn event.
	 */
	private void usbDetectBtnEvent() {
		JFrame temp=new USBDetectForm(DeviceRegisteredReceiver.sockets);
		temp.setVisible(true);
	}
	
	/**
	 * Process manager btn event.
	 */
	private void processManagerBtnEvent() {
		JFrame temp=new ProcessManagerForm(DeviceRegisteredReceiver.sockets);
		/*temp.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				control.setEnabled(true);
			}
		});*/
		temp.setVisible(true);
		//control.setEnabled(false);
	}
	
	/**
	 * ClipboardCollect.
	 */
	private void clipboardDetectBtnEvent() {
		JFrame temp=new ClipboardCollectForm(DeviceRegisteredReceiver.sockets);
		temp.setVisible(true);
	}
}
