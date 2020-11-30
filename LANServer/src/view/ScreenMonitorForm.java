/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.rendering.ColorDepth;

import model.Device;

// TODO: Auto-generated Javadoc
/**
 * The Class ScreenMonitorForm.
 */
public class ScreenMonitorForm extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1537403729607083938L;
	
	/** The content pane. */
	private JPanel contentPane;
	
	/** The config. */
	private VernacularConfig config;
	
	/** The client. */
	private VernacularClient client;
	
	/** The frame. */
	private Image frame;
	
	/**
	 * Create the frame.
	 *
	 * @param device the device
	 */
	public ScreenMonitorForm(Device device) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(client.isRunning()&&client!=null) client.stop();
			}
		});
		
		config=new VernacularConfig();
		config.setColorDepth(ColorDepth.BPP_8_INDEXED);
		config.setErrorListener(e -> {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        });
		config.setPasswordSupplier(()->"LDMpsw@1501");
		config.setScreenUpdateListener(img->{
			if(frame == null || frame.getWidth(null) != img.getWidth(null) || frame.getHeight(null) != img.getHeight(null)) {
				resizeWindow(img);
			}
			frame=img;
			repaint();
		});
		//beep() after connection
		config.setBellListener(v->getToolkit().beep());
		client=new VernacularClient(config);
		
		setTitle("Screen on: "+device.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 3322809489729138863L;
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				if(frame!=null) {
					Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(frame, 0, 0, getContentPane().getWidth(), getContentPane().getHeight(), null);
				}
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		client.start(device.getIp(), 5900);
	}

	/**
	 * Resize window.
	 *
	 * @param img the image
	 */
	private void resizeWindow(Image img) {
		// TODO Auto-generated method stub
		int remoteWidth = img.getWidth(null);
        int remoteHeight = img.getHeight(null);
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int paddingTop = getHeight() - getContentPane().getHeight();
        int paddingSides = getWidth() - getContentPane().getWidth();
        int maxWidth = (int) screenSize.getWidth() - paddingSides;
        int maxHeight = (int) screenSize.getHeight() - paddingTop;
        if (remoteWidth <= maxWidth && remoteHeight < maxHeight) {
        	getContentPane().setPreferredSize(new Dimension(remoteWidth, remoteHeight));
        } else {
            double scale = Math.min((double) maxWidth / remoteWidth, (double) maxHeight / remoteHeight);
            int scaledWidth = (int) (remoteWidth * scale);
            int scaledHeight = (int) (remoteHeight * scale);
            getContentPane().setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        }
        pack();
        setLocationRelativeTo(null);
	}

}
