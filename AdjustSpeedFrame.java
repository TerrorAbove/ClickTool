package clickToolPackage;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdjustSpeedFrame extends JFrame implements ChangeListener, WindowListener
{
	private static final long serialVersionUID = -5428598744593778498L;
	
	private JPanel panel;
	private JSlider slider;
	
	public AdjustSpeedFrame(int startValueSlider)
	{
		panel = new JPanel();
		slider = new JSlider(SwingConstants.HORIZONTAL);
		slider.addChangeListener((ChangeListener)this);
		slider.setMaximum(5);
		slider.setMinimum(0);
		slider.setValue(startValueSlider);
		
		panel.add(slider);
		
		this.setTitle("Adjust Speed");
		this.setSize(320, 70);
		this.setMinimumSize(getSize());
		this.addWindowListener((WindowListener)this);
		this.add(panel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent event)
	{
		ClickToolUtilities.updateSpeed(slider.getValue());
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		dispose();
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
