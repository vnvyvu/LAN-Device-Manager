/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	/** The btn send. */
	private JButton btnSend;
	
	/** The file. */
	private File file;
	private JLabel lblNewLabel_1;
	private JTextField txtClientDirectory;
	public static JProgressBar progressSendFile;

	/**
	 * Create the frame.
	 *
	 * @param paneItems the pane items
	 * @param btnSendFile the btn send file
	 */
	public SendFileForm(HashMap<Device, SocketChannel> selectedDevices) {
		setResizable(false);
		
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
		
		lblNewLabel_1 = new JLabel("Client directory (Default: D:)");
		contentPane.add(lblNewLabel_1);
		
		txtClientDirectory = new JTextField();
		contentPane.add(txtClientDirectory);
		txtClientDirectory.setColumns(10);
		
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
		
		progressSendFile = new JProgressBar();
		progressSendFile.setStringPainted(true);
		progressSendFile.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(progressSendFile.getValue()==100) {
					btnSend.setEnabled(true);
					btnSend.setText("Send");
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
			}
		});
		contentPane.add(progressSendFile);
		
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
	
}
