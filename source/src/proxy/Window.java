package proxy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Window extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static final String APPNAME = "AutoProxy";
	public static final String ICONPATH_ON = "iconON.png";
	public static final String ICONPATH_OFF = "iconOFF.png";
	
	public final static SystemTray systemTray = SystemTray.getSystemTray();
    public static TrayIcon trayIcon = new TrayIcon(getImage(ICONPATH_OFF),"Click to open "+APPNAME);
    
    static JFrame jf = new JFrame(APPNAME);
	static JLabel input_desc = new JLabel("SSID: ");
	static JLabel success = new JLabel("");
	static JTextField input = new JTextField(10);
	static JButton ok = new JButton("Save");
	static JButton tray = new JButton("Hide");
	
	public static String ssid_input;
	public static String ssid;
	public static boolean proxyOn;

	public static void main(String[] args) {
		ssid_input="";
		ssid="";
		
		Save.read();
		
		JPanel jp = new JPanel();
		JPanel jp_top = new JPanel();
		JPanel jp_center = new JPanel();
		JPanel jp_bottom = new JPanel();
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setIconImage(Toolkit.getDefaultToolkit().getImage(ICONPATH_OFF));
		
		jp.setBorder(new EmptyBorder(20, 30, 20, 30));
		jf.add(jp);
		jp.setLayout(new BorderLayout());
		jp.add(jp_top, BorderLayout.NORTH);
		jp.add(jp_center, BorderLayout.CENTER);
		jp.add(jp_bottom, BorderLayout.SOUTH);
		
		jp_top.setLayout(new FlowLayout(1));
		jp_top.add(input_desc);
		jp_top.add(input);
		
		jp_center.setLayout(new FlowLayout());
		jp_center.add(success);
		
		jp_bottom.setLayout(new FlowLayout());
		jp_bottom.add(ok);
		jp_bottom.add(tray);
		
		Window al = new Window();
		ok.addActionListener(al);
		tray.addActionListener(al);
		
		Timer t = new Timer();
		t.schedule(new TimerTask() {
		    @Override
		    public void run() {
		    	System.out.println("CHECK");
		    	try {
					if(ssid_input!=null) {
						CheckProxy.isChange();
					}
		    	} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}, 0, 5000);
		
		jf.pack();
		jf.setResizable(false);
		
		if (SystemTray.isSupported()) { 
            // Create SystemTray and TrayIcon (TrayIcon : It is icon that
            // display in SystemTray)
            trayIcon.setImageAutoSize(true);// Autosize icon base on space
                                            // available on
                                            // tray
 
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	jf.setVisible(true);
                    System.out.println("System Tray Click");
                    // This will display small popup message from System Tray
                    /*trayIcon.displayMessage("Omt TrayIcon Demo",
                            "This is an info message from TrayIcon omt demo",
                            TrayIcon.MessageType.INFO);*/
                }
            };
 
            trayIcon.addMouseListener(mouseAdapter);
 
            try {
                systemTray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            trayIcon.displayMessage(APPNAME, "Successfully started in System Tray.", TrayIcon.MessageType.NONE);
 
        }else {
        	jf.setVisible(true);
        }
 
    }
 
    public static Image getImage(String path) {
        ImageIcon icon = new ImageIcon(path, "omt");
        return icon.getImage();
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==ok) {
			ssid_input=input.getText();	
			Save.write();
		}else if(arg0.getSource()==tray) {
			if (SystemTray.isSupported()) {
				success.setText("");
				trayIcon.displayMessage(APPNAME, "Successfully minimized to System Tray.", TrayIcon.MessageType.NONE);
				jf.setVisible(false);
			}else {
				success.setText("Error");
				trayIcon.displayMessage(APPNAME, "Can't minimize to System Tray.", TrayIcon.MessageType.NONE);
			}
		}
	}
}
