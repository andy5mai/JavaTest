package sample;

import unit.DisplayRunnable;
import unit.InputRunnable;

public class CommandSample implements IExec
{
	@Override
	public void Exec() throws Exception
	{
		DisplayRunnable displayRunnable = new DisplayRunnable();
		Thread threadDisplay = new Thread(displayRunnable);
		Thread threadInput = new Thread(new InputRunnable(displayRunnable));
		

		threadInput.setDaemon(true);
		threadDisplay.setDaemon(true);
		
		threadInput.start();
		threadDisplay.start();
		
		while(true)
		{
			
		}
	}

}
