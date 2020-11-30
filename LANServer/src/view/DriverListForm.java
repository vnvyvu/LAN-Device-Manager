/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.Events;
import controller.receive.DeviceRegisteredReceiver;
import controller.receive.DriverMonitorReceiver;
import controller.send.DriverMonitorSender;
import model.Device;
import model.Driver;

// TODO: Auto-generated Javadoc
/**
 * The Class DriverListForm.
 */
public class DriverListForm extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5649232292431647944L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The table. */
	private JTable table;
	
	/** The combo categories. */
	private JComboBox<String> comboCategories;
	
	/** The drivers. */
	private Map<String, List<Driver>> drivers;
	
	/**
	 * Create the frame.
	 *
	 * @param device the device
	 */
	public DriverListForm(Device device) {
		final SocketChannel socketChannel=DeviceRegisteredReceiver.sockets.get(device);
		try {
			DriverMonitorSender.on(socketChannel);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return;
		}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				try {
					DriverMonitorSender.off(socketChannel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		});
		
		while(DriverMonitorReceiver.drivers.get(socketChannel)==null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.drivers=DriverMonitorReceiver.drivers.get(socketChannel).stream().map(v->{
			String[] token=v.split("\\|");
			return new Driver(token[2], token[3], token[1], token[0]);
		}).collect(Collectors.groupingBy(t -> t.getCategory()));
		
		setTitle("Drivers of: "+device.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setAutoCreateRowSorter(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Category", "Manufacturer", "Description"
			}
		) {
			private static final long serialVersionUID = -5152527354661264079L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				Object.class, String.class, Object.class, Object.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		
		comboCategories = new JComboBox<String>();
		comboCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboCategories();
			}
		});
		//description|manufacturer|name|category
		comboCategories.setModel((ComboBoxModel<String>) new DefaultComboBoxModel<String>(this.drivers.keySet().toArray(new String[this.drivers.keySet().size()])));
		comboCategories.addItem("All");
		comboCategories.setSelectedItem("All");
		panel.add(comboCategories);
		
		handleComboCategories();
		
		Events.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName().equals("driver.injected")) {
					String[] token=evt.getNewValue().toString().split("\\|");
					Driver driver=new Driver(token[2], token[3], token[1], token[0]);
					if(!drivers.containsKey(driver.getCategory())) {
						drivers.put(driver.getCategory(), Arrays.asList(driver));
						comboCategories.addItem(driver.getCategory());
					}else {
						ArrayList<Driver> temp=new ArrayList<Driver>(drivers.get(driver.getCategory()));
						temp.add(driver);
						drivers.put(driver.getCategory(), temp);
					}
					handleComboCategories();
				}else if(evt.getPropertyName().equals("driver.ejected")) {
					String[] token=evt.getNewValue().toString().split("\\|");
					Driver driver=new Driver(token[2], token[3], token[1], token[0]);
					ArrayList<Driver> temp=new ArrayList<Driver>(drivers.get(driver.getCategory()));
					temp.remove(driver);
					drivers.put(driver.getCategory(), temp);
					handleComboCategories();
				}
			}
		});
	}
	
	/**
	 * Handle combo categories.
	 */
	protected void handleComboCategories() {
		// TODO Auto-generated method stub
		DefaultTableModel def=(DefaultTableModel) table.getModel();
		def.setRowCount(0);
		List<Driver> d;
		if(!this.comboCategories.getSelectedItem().equals("All")) {
			d=this.drivers.get(this.comboCategories.getSelectedItem().toString());
		}else {
			d=this.drivers.values().stream().flatMap(v->v.stream()).collect(Collectors.toList());
		}
		d.forEach(v->{
			def.addRow(new String[] {v.getName(), v.getCategory(), v.getManufacturer(), v.getDescription()});
		});
	}

}