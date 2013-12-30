package unit;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ThreadObject extends Thread
{
//	private ThreadLocal<Integer> nSN;
	private TreeSet<Integer> set = new TreeSet<Integer>(); 
	
	public ThreadObject(int i, ThreadLocal<Integer> nSN)
	{
		this.setName(i + "");
		this.set.add(Integer.parseInt(this.getName()) + 1);
//		this.nSN = nSN;
//		this.nSN.set(new Integer(1));
	}
	
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				this.set.add(this.set.last() + 1);
				Thread.currentThread().sleep(1 * 1000);
//				System.out.println(this.getName() + "_" + this.nSN.get());
				
				for(Integer i : this.set)
				{
					System.out.println(this.getName() + "_" + i);
				}
//				this.nSN.set(this.nSN.get() + 1);
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
	}
}
