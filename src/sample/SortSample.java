package sample;

import java.util.SortedSet;
import java.util.TreeSet;

public class SortSample implements IExec
{
	private TreeSet<String> treeSetStrs = new TreeSet<String>(); 
	public SortSample()
	{
//		for(int j = 1; j < 4; j++)
//		{
//			for(int i = 0; i < 30; i++)
//			{
//				this.treeSetStrs.add((j * (long)Math.pow(10, 16) + i) + "");
//			}
//		}
		
		for(int i = 1; i < 4; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				this.treeSetStrs.add((i * 10 + j) + "");
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
		
//		SortedSet<String> sortedSetStrs = this.treeSetStrs.subSet(2 * (long)Math.pow(10, 16) + "", 3 * (long)Math.pow(10, 16) + "");
		//sortedSetStrs.remove(sortedSetStrs.first());
		
		SortedSet<String> sortedSetStrs = this.treeSetStrs.subSet("20", "30");
		
		for(String str : sortedSetStrs)
		{
			System.out.println(str);
		}
		
		
	}

}
