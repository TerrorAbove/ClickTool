package clickToolPackage;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class ClickToolMain implements NativeKeyListener, NativeMouseListener, WindowListener, ActionListener
{
	public static final String VERSION = "2.3";
	
	private static final Dimension BUTTON_DIMENSION = new Dimension(540, 20);
	private static final String NORMAL_TITLE = "Terror Above's Multi-Click-Tool, V"+VERSION;
	private static final Object[] POINT_OPTIONS = new Object[]{
		"Left-Click",
		"Right-Click",
		"Do Nothing"
	};
	
    public static ClickToolMain instance;
    
    private static PointMoveThread moveThread;
    
    private static HideToSystemTray frame;
	private static JPanel panel;
	
	private static JButton record1;
	private static JButton record2;
	private static JButton record3;
	private static JButton record4;
	private static JButton record5;
	
	private static JComboBox<String> adjustKeyBinds;
	private static JComboBox<String> otherOptions;
	private static JLabel indicator;
	
	private static boolean recordingKeyBind;
	
	private static boolean recordingPoint1;
	private static boolean recordingPoint2;
	private static boolean recordingPoint3;
	private static boolean recordingPoint4;
	private static boolean recordingPoint5;
	
	private static boolean stop;
	
	public static void main(String[] a)
	{
		ClickToolUtilities.load();
		
	    try
		{
            GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex)
		{
            System.exit(1);
		}
		
		instance = new ClickToolMain();

		//Construct the object and initialize native hook.
		GlobalScreen.addNativeKeyListener(instance);
		GlobalScreen.addNativeMouseListener(instance);
		
		recordingKeyBind = false;
		recordingPoint1 = false;
		recordingPoint2 = false;
		recordingPoint3 = false;
		recordingPoint4 = false;
		recordingPoint5 = false;
		
		stop = false;
		
		record1 = new JButton();
		record2 = new JButton();
		record3 = new JButton();
		record4 = new JButton();
		record5 = new JButton();
		
		indicator = new JLabel("Recording Key Bind -- Press any key");
		indicator.setFocusable(false);
		indicator.setVisible(false);
		
		record1.setPreferredSize(BUTTON_DIMENSION);
		record2.setPreferredSize(BUTTON_DIMENSION);
		record3.setPreferredSize(BUTTON_DIMENSION);
		record4.setPreferredSize(BUTTON_DIMENSION);
		record5.setPreferredSize(BUTTON_DIMENSION);
		
		record1.setFocusable(false);
		record2.setFocusable(false);
		record3.setFocusable(false);
		record4.setFocusable(false);
		record5.setFocusable(false);
		
		adjustKeyBinds = new JComboBox<String>();
		adjustKeyBinds.addItem("Adjust Key Binds");
		adjustKeyBinds.addItem("Set \"Point 1 Play\" Button");
		adjustKeyBinds.addItem("Set \"Point 2 Play\" Button");
		adjustKeyBinds.addItem("Set \"Point 3 Play\" Button");
		adjustKeyBinds.addItem("Set \"Point 4 Play\" Button");
		adjustKeyBinds.addItem("Set \"Point 5 Play\" Button");
		adjustKeyBinds.setPreferredSize(BUTTON_DIMENSION);
		adjustKeyBinds.addActionListener((ActionListener)instance);
		adjustKeyBinds.setFocusable(false);
		
		otherOptions = new JComboBox<String>();
		otherOptions.addItem("Other Options...");
		otherOptions.addItem("Adjust speed of replay mouse movement...");
		otherOptions.addItem("Check which points are currently set...");
		otherOptions.addItem("Reset all points");
		otherOptions.setPreferredSize(BUTTON_DIMENSION);
		otherOptions.addActionListener((ActionListener)instance);
		otherOptions.setFocusable(false);
		
		record1.setText("Record Point 1");
		record2.setText("Record Point 2");
		record3.setText("Record Point 3");
		record4.setText("Record Point 4");
		record5.setText("Record Point 5");
		
		record1.addActionListener((ActionListener)instance);
		record2.addActionListener((ActionListener)instance);
		record3.addActionListener((ActionListener)instance);
		record4.addActionListener((ActionListener)instance);
		record5.addActionListener((ActionListener)instance);
		
		frame = new HideToSystemTray();
		panel = new JPanel();
		
		panel.add(record1);
		panel.add(record2);
		panel.add(record3);
		panel.add(record4);
		panel.add(record5);
		
		panel.add(adjustKeyBinds);
		panel.add(otherOptions);
		
		panel.add(indicator);
		
		panel.setPreferredSize(new Dimension(550, 200));
		frame.add(panel);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle(NORMAL_TITLE);
		frame.addWindowListener((WindowListener)instance);
		
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
	    if(e.getSource() == record1)
	    {
	    	recordingPoint1 = true;
	    	frame.setState(Frame.ICONIFIED);
	    }
	    else if(e.getSource() == record2)
	    {
	    	recordingPoint2 = true;
	    	frame.setState(Frame.ICONIFIED);
	    }
	    else if(e.getSource() == record3)
	    {
	    	recordingPoint3 = true;
	    	frame.setState(Frame.ICONIFIED);
	    }
	    else if(e.getSource() == record4)
	    {
	    	recordingPoint4 = true;
	    	frame.setState(Frame.ICONIFIED);
	    }
	    else if(e.getSource() == record5)
	    {
	    	recordingPoint5 = true;
	    	frame.setState(Frame.ICONIFIED);
	    }
	    else if(e.getSource() == adjustKeyBinds)
	    {
	    	onAdjustKeyBindsDropdownClick(adjustKeyBinds.getSelectedIndex());
	    	adjustKeyBinds.setSelectedIndex(0);
	    }
	    else//otherOptions
	    {
	    	onOtherOptionsDropdownClick(otherOptions.getSelectedIndex());
	    	otherOptions.setSelectedIndex(0);
	    }
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0)
	{
		if(recordingPoint1)
		{
			frame.fromTray();
			
			ClickToolUtilities.setLastPoint(arg0.getPoint());
			ClickToolUtilities.setPoint(0, new ActionPoint(arg0.getPoint(), JOptionPane.showOptionDialog(panel, "Select the behavior at this point.", "Point 1", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, POINT_OPTIONS, POINT_OPTIONS[0])));
			JOptionPane.showMessageDialog(null, "Point 1 has been set.");
			recordingPoint1 = false;
			
			try
			{
				Point p = ClickToolUtilities.getLastPoint();
				ClickToolUtilities.setLastPoint(null);
				new Robot().mouseMove(p.x, p.y);
			}
			catch(Exception ex) {}
		}
		else if(recordingPoint2)
		{
			frame.fromTray();
			
			ClickToolUtilities.setLastPoint(arg0.getPoint());
			ClickToolUtilities.setPoint(1, new ActionPoint(arg0.getPoint(), JOptionPane.showOptionDialog(panel, "Select the behavior at this point.", "Point 2", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, POINT_OPTIONS, POINT_OPTIONS[0])));
			JOptionPane.showMessageDialog(null, "Point 2 has been set.");
			recordingPoint2 = false;
			
			try
			{
				Point p = ClickToolUtilities.getLastPoint();
				ClickToolUtilities.setLastPoint(null);
				new Robot().mouseMove(p.x, p.y);
			}
			catch(Exception ex) {}
		}
		else if(recordingPoint3)
		{
			frame.fromTray();
			
			ClickToolUtilities.setLastPoint(arg0.getPoint());
			ClickToolUtilities.setPoint(2, new ActionPoint(arg0.getPoint(), JOptionPane.showOptionDialog(panel, "Select the behavior at this point.", "Point 3", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, POINT_OPTIONS, POINT_OPTIONS[0])));
			JOptionPane.showMessageDialog(null, "Point 3 has been set.");
			recordingPoint3 = false;
			
			try
			{
				Point p = ClickToolUtilities.getLastPoint();
				ClickToolUtilities.setLastPoint(null);
				new Robot().mouseMove(p.x, p.y);
			}
			catch(Exception ex) {}
		}
		else if(recordingPoint4)
		{
			frame.fromTray();
			
			ClickToolUtilities.setLastPoint(arg0.getPoint());
			ClickToolUtilities.setPoint(3, new ActionPoint(arg0.getPoint(), JOptionPane.showOptionDialog(panel, "Select the behavior at this point.", "Point 4", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, POINT_OPTIONS, POINT_OPTIONS[0])));
			JOptionPane.showMessageDialog(null, "Point 4 has been set.");
			recordingPoint4 = false;

			try
			{
				Point p = ClickToolUtilities.getLastPoint();
				ClickToolUtilities.setLastPoint(null);
				new Robot().mouseMove(p.x, p.y);
			}
			catch(Exception ex) {}
		}
		else if(recordingPoint5)
		{
			frame.fromTray();
			
			ClickToolUtilities.setLastPoint(arg0.getPoint());
			ClickToolUtilities.setPoint(4, new ActionPoint(arg0.getPoint(), JOptionPane.showOptionDialog(panel, "Select the behavior at this point.", "Point 5", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, POINT_OPTIONS, POINT_OPTIONS[0])));
			JOptionPane.showMessageDialog(null, "Point 5 has been set.");
			recordingPoint5 = false;
			
			try
			{
				Point p = ClickToolUtilities.getLastPoint();
				ClickToolUtilities.setLastPoint(null);
				new Robot().mouseMove(p.x, p.y);
			}
			catch(Exception ex) {}
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0)
	{
		if(arg0.getKeyCode() == NativeKeyEvent.VC_ESCAPE)
		{
			recordingKeyBind = false;
			if(stop)
			{
				frame.setTitle(NORMAL_TITLE);

				record1.setEnabled(true);
				record2.setEnabled(true);
				record3.setEnabled(true);
				record4.setEnabled(true);
				record5.setEnabled(true);
				
				adjustKeyBinds.setEnabled(true);
				otherOptions.setEnabled(true);
				
				indicator.setEnabled(true);
				
				GlobalScreen.addNativeMouseListener(instance);
			}
			else
			{
				frame.setTitle("Terror Above's Multi-Click-Tool - STOPPED... ESC to continue.");
				
				record1.setEnabled(false);
				record2.setEnabled(false);
				record3.setEnabled(false);
				record4.setEnabled(false);
				record5.setEnabled(false);
				
				adjustKeyBinds.setEnabled(false);
				otherOptions.setEnabled(false);
				
				indicator.setEnabled(false);
				
				GlobalScreen.removeNativeMouseListener(instance);
			}
			toggleStopped();
			return;
		}
		
		if(!stop)
		{
			if(recordingKeyBind)
			{
				ClickToolUtilities.setCFGArray(arg0.getKeyCode());
				indicator.setVisible(false);
				recordingKeyBind = false;
			}
			else
			{
				Object data = ClickToolUtilities.getDataForKeyCode(arg0.getKeyCode());

				if(data != null)
				{
					if(data instanceof Boolean)
						JOptionPane.showMessageDialog(frame, "This point is not currently set.");
					else if(data instanceof ActionPoint)
					{
						ActionPoint point = (ActionPoint)data;
						
						frame.setState(Frame.ICONIFIED);
						if(moveThread != null)
							moveThread.end();
						moveThread = new PointMoveThread(point);
						moveThread.start();
					}
				}
			}
		}
	}
	
	private static void onOtherOptionsDropdownClick(int index)
	{
		switch(index)
		{
		case 1://handle speed adjust
			new AdjustSpeedFrame(ClickToolUtilities.getSpeed());
			break;
		case 2://check which points are set
			final JFrame tempFrame = new JFrame();
			tempFrame.setLayout(new BoxLayout(tempFrame.getContentPane(), BoxLayout.X_AXIS));
			
			JPanel point1Panel = new JPanel();
			JPanel point2Panel = new JPanel();
			JPanel point3Panel = new JPanel();
			JPanel point4Panel = new JPanel();
			JPanel point5Panel = new JPanel();
			
			JLabel point1Info = new JLabel("Point 1");
			JLabel point2Info = new JLabel("Point 2");
			JLabel point3Info = new JLabel("Point 3");
			JLabel point4Info = new JLabel("Point 4");
			JLabel point5Info = new JLabel("Point 5");
			
			JCheckBox point1Box = new JCheckBox();
			JCheckBox point2Box = new JCheckBox();
			JCheckBox point3Box = new JCheckBox();
			JCheckBox point4Box = new JCheckBox();
			JCheckBox point5Box = new JCheckBox();
			
			//point1Box.setDisabledSelectedIcon(icon);
			point1Box.setSelected(!ClickToolUtilities.isNull(0));
			point1Box.setEnabled(false);
			//point2Box.setDisabledSelectedIcon(icon);
			point2Box.setSelected(!ClickToolUtilities.isNull(1));
			point2Box.setEnabled(false);
			//point3Box.setDisabledSelectedIcon(icon);
			point3Box.setSelected(!ClickToolUtilities.isNull(2));
			point3Box.setEnabled(false);
			//point4Box.setDisabledSelectedIcon(icon);
			point4Box.setSelected(!ClickToolUtilities.isNull(3));
			point4Box.setEnabled(false);
			//point5Box.setDisabledSelectedIcon(icon);
			point5Box.setSelected(!ClickToolUtilities.isNull(4));
			point5Box.setEnabled(false);
			
			point1Panel.add(point1Info);
			point1Panel.add(point1Box);
			
			point2Panel.add(point2Info);
			point2Panel.add(point2Box);
			
			point3Panel.add(point3Info);
			point3Panel.add(point3Box);
			
			point4Panel.add(point4Info);
			point4Panel.add(point4Box);
			
			point5Panel.add(point5Info);
			point5Panel.add(point5Box);
			
			
			
			tempFrame.setTitle("Current Status of the Points: ");
			tempFrame.setSize(500, 80);
			tempFrame.setMinimumSize(tempFrame.getSize());
			tempFrame.setLocationRelativeTo(frame);
			tempFrame.addWindowListener(new WindowListener()
			{
				public void windowActivated(WindowEvent arg0) {}
				public void windowClosed(WindowEvent arg0) {}
				public void windowClosing(WindowEvent arg0) {}
				public void windowDeiconified(WindowEvent arg0) {}
				public void windowIconified(WindowEvent arg0) {}
				public void windowOpened(WindowEvent arg0) {}
				public void windowDeactivated(WindowEvent arg0)
				{
					tempFrame.dispose();
				}
			});
			tempFrame.add(point1Panel);
			tempFrame.add(point2Panel);
			tempFrame.add(point3Panel);
			tempFrame.add(point4Panel);
			tempFrame.add(point5Panel);
			tempFrame.setVisible(true);
			break;
		case 3:
			ClickToolUtilities.initialize();
			JOptionPane.showMessageDialog(frame, "All points have been reset.");
			break;
		}
	}
	
	private static void onAdjustKeyBindsDropdownClick(int index)
	{
		switch(index)
		{
		case 1:
			ClickToolUtilities.currentConfigIndex = ClickToolUtilities.REPLAY_1_INDEX;
			indicator.setVisible(true);
	        recordingKeyBind = true;
			break;
		case 2:
			ClickToolUtilities.currentConfigIndex = ClickToolUtilities.REPLAY_2_INDEX;
			indicator.setVisible(true);
	        recordingKeyBind = true;
			break;
		case 3:
			ClickToolUtilities.currentConfigIndex = ClickToolUtilities.REPLAY_3_INDEX;
			indicator.setVisible(true);
	        recordingKeyBind = true;
			break;
		case 4:
			ClickToolUtilities.currentConfigIndex = ClickToolUtilities.REPLAY_4_INDEX;
			indicator.setVisible(true);
	        recordingKeyBind = true;
			break;
		case 5:
			ClickToolUtilities.currentConfigIndex = ClickToolUtilities.REPLAY_5_INDEX;
			indicator.setVisible(true);
	        recordingKeyBind = true;
			break;
		}
	}
	
	public boolean isStopped()
	{
		return stop;
	}
	
	public void toggleStopped()
	{
		stop = !stop;
		frame.switchPauseLabel();
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		ClickToolUtilities.save();
		System.exit(0);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {}
	
	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {}
	
	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e)
	{
		frame.toTray();
	}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}
