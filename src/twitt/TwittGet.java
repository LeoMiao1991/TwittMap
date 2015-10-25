package twitt;

public class TwittGet 
{
	public static void startCollect() 
	{
		TwittGetTask[] tasks = new TwittGetTask[4];
		for(int i = 0; i < 4; i++)
		{
			tasks[i] = new TwittGetTask(i);
			tasks[i].start();
			try 
			{
				Thread.sleep(5000);
			} 
			catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
}
