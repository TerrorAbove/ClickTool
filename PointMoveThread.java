package clickToolPackage;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class PointMoveThread extends Thread
{
	private ActionPoint targetPoint;
	
	private int releaseMask;
	private boolean end;
	
	public PointMoveThread(ActionPoint targetPoint)
	{
		super();
		this.targetPoint = targetPoint;
		this.releaseMask = -1;
		this.end = false;
	}
	
	@Override
	public void run()
	{
		Robot robot;
		
		try
		{
			robot = new Robot();
		}
		catch(AWTException ae)
		{
			end = true;//this line isn't necessary but I want to emphasize that the Thread is "ended" at this point
			return;
		}
		
		long lastMovement = 0;
		Point currentPoint;

		while(!end)
		{
			currentPoint = MouseInfo.getPointerInfo().getLocation();
			
			try
			{
				int speed = ClickToolUtilities.getSpeed();

				long msDelay = Math.min(5, Math.max(1, 5 - speed));

				if(releaseMask > -1)
				{
					if(lastMovement + 10 <= System.currentTimeMillis())
					{
						robot.mouseRelease(releaseMask);
						break;
					}
				}
				else if(lastMovement + msDelay <= System.currentTimeMillis())
				{
					if(!ClickToolMain.instance.isStopped())
					{
					    if(currentPoint.equals(targetPoint.point))
					    {
					    	final int MASK = targetPoint.actionType == 0 ? 0x400 : targetPoint.actionType == 1 ? 0x1000 : -1;
							
					    	if(MASK == -1)
					    		break;
					    	
							robot.mousePress(MASK);
							releaseMask = MASK;
					    }
					    else
					    {
					    	double dx = currentPoint.getX() - targetPoint.point.getX();
					    	double dy = currentPoint.getY() - targetPoint.point.getY();
					    	
					    	double speedFactor = 1.0;
					    	
					    	if(speed == 5)//if the speed is 5 (fastest), scale the movement based on distance
					    		speedFactor = Math.max(1.0, 1.0 + (int)(currentPoint.distance(targetPoint.point) / 100));

					    	if(Math.abs(dy) <= Math.abs(dx) || Math.random() >= 0.5)
					    	{
					    		if(currentPoint.getX() < targetPoint.point.getX())
					    		{
					    			currentPoint.translate((int)(speedFactor * ((int)(Math.random() * 2) + 1)), 0);
					    		}
					    		else if(currentPoint.getX() > targetPoint.point.getX())
					    		{
					    			currentPoint.translate((int)(speedFactor * (-(int)(Math.random() * 2) - 1)), 0);
					    		}
					    	}

					    	if(Math.abs(dx) <= Math.abs(dy) || Math.random() >= 0.5)
					    	{
					    		if(currentPoint.getY() < targetPoint.point.getY())
					    		{
					    			currentPoint.translate(0, (int)(speedFactor * ((int)(Math.random() * 2) + 1)));
					    		}
					    		else if(currentPoint.getY() > targetPoint.point.getY())
					    		{
					    			currentPoint.translate(0, (int)(speedFactor * (-(int)(Math.random() * 2) - 1)));
					    		}
					    	}
					    	
					    	robot.mouseMove((int)currentPoint.getX(), (int)currentPoint.getY());
					    }
					}
					lastMovement = System.currentTimeMillis();
				}
			}
			catch(Exception someEx) {}
		}
	}
	
	public void end()
	{
		end = true;
	}
}
	
