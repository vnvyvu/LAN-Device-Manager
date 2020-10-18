package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import controller.Utils;
import controller.send.ProcessConfigSender;
import model.Device;

public class ProcessManagerForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2363731655747416650L;
	private JPanel contentPane;
	private JButton btnOff;
	
	private int mode;
	private String blacklist;
	private YamlMapping config;
	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public ProcessManagerForm(HashMap<Device, SocketChannel> devices) {
		try{
			config=Utils.getConfig("process.yml");
			mode=config.integer("mode");
			blacklist=config.string("blacklist");
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "process.yml not found or invalid format", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		setTitle("Process Manager");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 618, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(10, 1, 0, 15));
		
		JLabel lblNewLabel = new JLabel("Blacklist>>>(Ex: nodepad|chorme|garena)");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("When a blacklisted process is detected:");
		panel.add(lblNewLabel_1);
		
		JComboBox<String> comboMode = new JComboBox<String>();
		comboMode.setModel(new DefaultComboBoxModel<String>(new String[] {"DO_NOTHING", "KILL_HIM", "TURN_OFF_PC"}));
		comboMode.setSelectedIndex(mode);
		panel.add(comboMode);
		
		JTextArea txtBacklist = new JTextArea();
		txtBacklist.setWrapStyleWord(true);
		txtBacklist.setLineWrap(true);
		txtBacklist.setText(blacklist);
		contentPane.add(txtBacklist);
		
		JButton btnUpdate = new JButton("Update/Enable");
		btnUpdate.setMultiClickThreshhold(1500);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOff.setEnabled(false);
				try {
					config=Yaml.createYamlInput("mode: "+comboMode.getSelectedIndex()+"\nblacklist: \""+txtBacklist.getText().toLowerCase()+"\"").readYamlMapping();
					Yaml.createYamlPrinter(new FileWriter("process.yml", false)).print(config);;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				devices.forEach((device, socketChannel)->{
					try {
						ProcessConfigSender.on(socketChannel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				btnOff.setEnabled(true);
			}
		});
		panel.add(btnUpdate);
		
		btnOff = new JButton("Disable");
		btnOff.setMultiClickThreshhold(1500);
		btnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				devices.forEach((device, socketChannel)->{
					try {
						ProcessConfigSender.off(socketChannel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
		});
		panel.add(btnOff);
	}

}
