package clickToolPackage;

import java.awt.Point;
import java.io.Serializable;

public class ActionPoint implements Serializable
{
	private static final long serialVersionUID = 5365053838761567173L;
	
	public Point point;
	public int actionType;//0: left-click, 1: right-click, 2: no action
	
	public ActionPoint(Point point, int actionType)
	{
		this.point = point;
		this.actionType = actionType;
	}
}
