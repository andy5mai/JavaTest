package sample;

import java.util.SortedSet;
import java.util.TreeSet;

public class SortSample implements IExec
{
	private TreeSet<String> treeSetStrs = new TreeSet<String>(); 
	public SortSample()
	{
		for(int j = 1; j < 4; j++)
		{
			for(int i = 0; i < 30; i++)
			{
				this.treeSetStrs.add((j * (long)Math.pow(10, 16) + i) + "");
			}
		}
	}
	
	@Override
	public void Exec()
	{
//		for(String str : this.treeSetStrs)
//		{
//			System.out.println(str);
//		}
		
		SortedSet<String> sortedSetStrs = this.treeSetStrs.subSet(2 * (long)Math.pow(10, 16) + "", 3 * (long)Math.pow(10, 16) + "");
		for(String str : sortedSetStrs)
		{
			System.out.println(str);
		}
	}

}
