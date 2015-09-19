package clickToolPackage;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jnativehook.keyboard.NativeKeyEvent;

public class ClickToolUtilities
{
    public static int currentConfigIndex = -1;
    
    private static Point lastPoint;
    private static ActionPoint[] pointData;
    
    public static final int REPLAY_1_INDEX = 0;
    public static final int REPLAY_2_INDEX = 1;
    public static final int REPLAY_3_INDEX = 2;
    public static final int REPLAY_4_INDEX = 3;
    public static final int REPLAY_5_INDEX = 4;
    
    public static final int SPEED_INDEX = 5;
    
    private static final int DEFAULT_SPEED = 3;
    
    private static final int[] DEFAULT_CONFIGS = new int[]{
        NativeKeyEvent.VC_KP_1,
        NativeKeyEvent.VC_KP_2,
        NativeKeyEvent.VC_KP_3,
        NativeKeyEvent.VC_KP_4,
        NativeKeyEvent.VC_KP_5,
        DEFAULT_SPEED,
    };
    
	private static int[] configData;
	
	private static final String CFG_FILE_NAME = "ClickToolConfigs";
	private static final String POINT_DATA_FILE_NAME = "SavedPointData";
	
    private static void writeConfigFile(int[] data) throws IOException
	{
		File f = new File(CFG_FILE_NAME);
		try
		{
			f.createNewFile();
		}catch(Exception exc){}
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		for(int i = 0; i < data.length; i++)
		{
			writer.write(data[i] + "");
			writer.newLine();
		}
		writer.close();
	}
	
	private static int[] readConfigFile() throws IOException
	{
		File f = new File(CFG_FILE_NAME);
		ArrayList<Integer> list = new ArrayList<Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		String curr = null;
		
		while((curr = reader.readLine()) != null)
		{
			try
			{
				list.add(Integer.parseInt(curr));
			}
			catch(Exception ex){}
		}
		
		reader.close();
		Object[] ints = list.toArray();
		int[] toReturn = new int[ints.length];
		for(int i = 0; i < toReturn.length; i++)
			toReturn[i] = ((Integer)ints[i]).intValue();
		return toReturn;
	}
	
	private static void writePointDataFile() throws IOException
	{
		File f = new File(POINT_DATA_FILE_NAME);
		PrintWriter out = new PrintWriter(new FileWriter(f));
		
		for(int i = 0; i < pointData.length; i++)
		{
			ActionPoint ap = pointData[i];
			
			if(ap == null)
				out.println("-");
			else
				out.println(ap.point.x+","+ap.point.y+","+ap.actionType);
		}
		
		out.close();
	}
	
	private static ActionPoint[] readPointDataFile() throws IOException
	{
		ActionPoint[] data = new ActionPoint[5];
		
		File f = new File(POINT_DATA_FILE_NAME);
		BufferedReader in = new BufferedReader(new FileReader(f));
		
		for(int i = 0; i < data.length; i++)
		{
			String line = in.readLine();
			
			if(line != null && !line.startsWith("-"))
			{
				String[] arguments = line.split(",");
				
				int x = -1;
				int y = -1;
				int type = -1;
				
				try
				{
					x = Integer.parseInt(arguments[0]);
					y = Integer.parseInt(arguments[1]);
					type = Integer.parseInt(arguments[2]);
					
					data[i] = new ActionPoint(new Point(x, y), type);
				}
				catch(NumberFormatException nfe) {}
				catch(IndexOutOfBoundsException ioobe) {}
			}
		}
		
		in.close();
		
		return data;
	}
	
	public static void load()
	{
		try
		{
			int[] temp = readConfigFile();
			if(temp.length == DEFAULT_CONFIGS.length)
				configData = temp;
			else
				setArrayToDefaults();
		}
		catch(IOException ex)
		{
			setArrayToDefaults();
		}
		
		try
		{
			pointData = readPointDataFile();
		}
		catch(Exception e)
		{
			initialize();
		}
	}
	
	public static void save()
	{
		try
		{
			writeConfigFile(configData);
			writePointDataFile();
		}
		catch(IOException ex){}
	}
	
	public static void setArrayToDefaults()
	{
		configData = new int[DEFAULT_CONFIGS.length];
		for(int i = 0; i < configData.length; i++)
			configData[i] = DEFAULT_CONFIGS[i];
	}
	
	public static void setCFGArray(int keyCode)
	{
		configData[currentConfigIndex] = keyCode;
	}
	
	public static Object getDataForKeyCode(int keyCode)
	{
	    for(int i = REPLAY_1_INDEX; i <= REPLAY_5_INDEX; i++)
	    {
	        if(configData[i] == keyCode)
	        {
	        	if(pointData[i] == null)
	        		return Boolean.FALSE;
	        	
	            return pointData[i];
	        }
	    }
	    
	    return null;
	}
	
	public static int indexForKeyCode(int keyCode)
	{
	    for(int i = 0; i < 5; i++)
	    {
	        if(configData[i] == keyCode)
	        {
	            return i;
	        }
	    }
	    
	    return -1;
	}
	
	public static int getConfig(int index)
	{
		return configData[index];
	}
	
	public static void setPoint(int index, ActionPoint p) throws IndexOutOfBoundsException
	{
		pointData[index] = p;
	}
	
	public static boolean isNull(int index)
	{
		try
		{
			return pointData[index] == null;
		}
		catch(IndexOutOfBoundsException e)
		{
			return true;
		}
	}
	
	public static int getSpeed()
	{
	    return configData[SPEED_INDEX];
	}
	
	public static void initialize()
	{
		pointData = new ActionPoint[5];
	}
	
	public static Point getLastPoint()
	{
		return lastPoint;
	}
	
	public static void setLastPoint(Point p)
	{
		lastPoint = p;
	}
	
	public static void updateSpeed(int speed)
	{
		configData[SPEED_INDEX] = speed;
	}
}
