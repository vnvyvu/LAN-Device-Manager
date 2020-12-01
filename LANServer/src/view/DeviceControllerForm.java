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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.rendering.ColorDepth;

import model.Device;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceControllerForm.
 */
public class DeviceControllerForm extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9064179861453119270L;

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
	public DeviceControllerForm(Device device) {
		//Stop session when close
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				if(client!=null&&client.isRunning()) client.stop();
			}
		});
		
		//Init Vernacular config
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
		
		setTitle("Device controller: "+device.getName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel(){
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
		mouseEvent();
		keyEvent();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		client.start(device.getIp(), 5900);
	}
	
	/**
	 * Mouse event. Server's mouse event will be sent to client performing the same action
	 */
	private void mouseEvent() {
		// TODO Auto-generated method stub
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(client!=null&&client.isRunning()) client.moveMouse(getMouseX(e.getX()), getMouseY(e.getY()));
            }
		});
		contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(client!=null&&client.isRunning()) client.updateMouseButton(e.getButton(), true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(client!=null&&client.isRunning()) client.updateMouseButton(e.getButton(), false);
            }
        });
		contentPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(client!=null&&client.isRunning()) {
                    int notches=e.getWheelRotation();
                    if(notches<0) client.scrollUp();
                    else client.scrollDown();
                }
            }
        });
	}
	
	//Mouse Utils
	
	/**
	 * Compute client-side diaphragm.
	 *
	 * @param x the x
	 * @return the diaphragm
	 */
	private int getMouseX(int x) {
        if(frame == null) return x;
        return (int) (x*((double) frame.getWidth(null)/contentPane.getWidth()));
    }
	
	/**
	 * Compute client-side pitch.
	 *
	 * @param y the y
	 * @return the pitch
	 */
	private int getMouseY(int y) {
        if (frame == null) return y;
        return (int) (y*((double) frame.getHeight(null)/contentPane.getHeight()));
    }
	
	/**
	 * Key event.
	 */
	private void keyEvent() {
        setFocusTraversalKeysEnabled(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(client!=null&&client.isRunning()) client.handleKeyEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(client!=null&&client.isRunning()) client.handleKeyEvent(e);
            }
        });
    }
	
	/**
	 * Resize window.
	 *
	 * @param img the img
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
