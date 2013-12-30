package unit;

import java.util.Scanner;

public class InputRunnable implements Runnable
{
	private Scanner scanner = new Scanner(System.in);
	private DisplayRunnable displayRunnable;
	
	public InputRunnable(DisplayRunnable displayRunnable)
	{
		this.displayRunnable = displayRunnable;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			this.displayRunnable.display(this.scanner.next());
		}
	}

}
