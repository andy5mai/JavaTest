package sample;

import unit.ThreadObject;

public class MultiThreadSample implements IExec
{
	@Override
	public void Exec() throws Exception
	{
//		Thread thread;
		ThreadLocal<Integer> sn = new ThreadLocal<Integer>()
				{
					@Override
					public Integer initialValue()
					{
						return 0;
					}
				
				};
		sn.set(1);
		for(int i = 0; i < 10; i++)
		{
			Thread thread = new ThreadObject(i, sn);
			thread.setDaemon(true);
			thread.start();
		}
		
		while(true)
		{
		}
	}
}
