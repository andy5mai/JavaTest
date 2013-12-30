package unit;

import java.util.Scanner;

public class DisplayRunnable implements Runnable
{
	private String str;
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				Thread.currentThread().sleep(1 * 1000);
				System.out.println(this.str);
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
	}

	public void display(String str)
	{
		this.str = str;
	}
}
