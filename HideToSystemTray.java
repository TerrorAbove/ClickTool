package clickToolPackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 *
 * @author Mohammad Faisal, modified by Terror Above
 * ermohammadfaisal.blogspot.com
 * facebook.com/m.faisal6621
 *
 */

public class HideToSystemTray extends JFrame{
	private static final long serialVersionUID = 8496679537184992556L;
	final MenuItem PAUSE = new MenuItem("Pause");
	TrayIcon trayIcon;
	SystemTray tray;
	HideToSystemTray(){
		super();
		if(SystemTray.isSupported()){
			tray=SystemTray.getSystemTray();
			
			final MenuItem EXIT = new MenuItem("Exit");
			final MenuItem OPEN = new MenuItem("Open");

			Image image=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/cursor.gif"));
			PopupMenu popup=new PopupMenu();
			EXIT.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClickToolUtilities.save();
					System.exit(0);
				}
			});
			PAUSE.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClickToolMain.instance.toggleStopped();
				}
			});
			OPEN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fromTray();
				}
			});
			popup.add(OPEN);
			popup.add(PAUSE);
			popup.addSeparator();
			popup.add(EXIT);
			trayIcon=new TrayIcon(image, "Terror Above's ClickTool - V"+ClickToolMain.VERSION, popup);
			trayIcon.setImageAutoSize(true);
		}else{
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/cursor.gif")));
	}
	
	public void toTray()
	{
		try {
			tray.add(trayIcon);
			setVisible(false);
		} catch (AWTException ex) {
		}
	}
	
	public void fromTray()
	{
		setVisible(true);
		tray.remove(trayIcon);
		setExtendedState(JFrame.NORMAL);
	}
	
	public void switchPauseLabel()
	{
		PAUSE.setLabel(PAUSE.getLabel().equals("Pause") ? "Resume" : "Pause");
	}
}
