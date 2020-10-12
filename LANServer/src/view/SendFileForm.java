/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Events;
import controller.send.FileSender;
import model.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class SendFileForm.
 */
public class SendFileForm extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7251864898377198197L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The txt file name. */
	private JTextField txtFileName;
	
	/** The lbl progress. */
	private JLabel lblProgress;
	
	/** The btn send. */
	private JButton btnSend;
	
	/** The file. */
	private File file;
	private JLabel lblNewLabel_1;
	private JTextField txtClientDirectory;

	/**
	 * Create the frame.
	 *
	 * @param paneItems the pane items
	 * @param btnSendFile the btn send file
	 */
	public SendFileForm(HashMap<Device, SocketChannel> selectedDevices, JFrame deviceList) {
		setAlwaysOnTop(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				deviceList.setEnabled(true);
			}
		});
		
		setTitle("File Sender");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 135);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 2, 0, 0));
		
		JButton btnChooser = new JButton("Choose file");
		btnChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleFileAction();
			}
		});
		contentPane.add(btnChooser);
		
		txtFileName = new JTextField();
		txtFileName.setEditable(false);
		contentPane.add(txtFileName);
		txtFileName.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Client directory(D:)");
		contentPane.add(lblNewLabel_1);
		
		txtClientDirectory = new JTextField();
		contentPane.add(txtClientDirectory);
		txtClientDirectory.setColumns(10);
		
		lblProgress = new JLabel("Progress: ");
		contentPane.add(lblProgress);
		
		btnSend = new JButton("Send");
		btnSend.setMultiClickThreshhold(1000);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSend.setEnabled(false);
				btnSend.setText("Sending");
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				if(file==null) {
					JOptionPane.showMessageDialog(null, "You must choose a file to send", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				selectedDevices.forEach((device, socketChannel)->{
					try {
						FileSender.writeInfo(socketChannel, "D:"+txtClientDirectory.getText(), file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
			}
		});
		contentPane.add(btnSend);
		initEvents();
		
		setLocationRelativeTo(null);
	}
	
	/**
	 * Handle file action.
	 */
	private void handleFileAction() {
		JFileChooser fileChooser=new JFileChooser(new File("user.home"));
		if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			this.file=fileChooser.getSelectedFile();
			this.txtFileName.setText(this.file.getPath());
		}
	}
	
	private void initEvents() {
		Events.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName().equals("progress")) {
					lblProgress.setText("Progress: "+(int)evt.getNewValue()+"%");
					if((int)evt.getNewValue()==100) {
						setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						btnSend.setEnabled(true);
						btnSend.setText("Send");
					}
				}
			}
		});
	}
}
