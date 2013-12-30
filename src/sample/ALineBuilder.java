package sample;

import java.util.ArrayList;
import java.util.List;

import util.CommonUtil;

public abstract class ALineBuilder<T>
{
	protected List<String> listTitles = new ArrayList<String>();
	protected List<String> listContents = new ArrayList<String>();
	protected List<Character> listPatchChars = new ArrayList<Character>();
	protected List<Integer> listExpectedSize = new ArrayList<Integer>();
	
	protected int nTotalExpectedSize;
	
	protected char cSeparateSymbol = '=';
	
//	protected String strText;
//	protected String strSymbol;
//	protected int nExpectedSize;
	
	public ALineBuilder(String strTitle, char cPatchChar, int nExpectedSize)
	{
		this.addField(strTitle, cPatchChar, nExpectedSize);
	}
	
	public void addField(String strTitle, char cPatchChar, int nExpectedSize)
	{
		this.listTitles.add(strTitle);
		this.listPatchChars.add(cPatchChar);
		this.listExpectedSize.add(nExpectedSize);
		
		this.nTotalExpectedSize = this.nTotalExpectedSize + nExpectedSize;
	}
	
	public void fillContent(String strContent)
	{
		this.listContents.add(strContent);
	}
	
	public void printTitle()
	{
		this.print(this.listTitles);
	}
	
	public void printSeparate()
	{
		System.out.println(new String(CommonUtil.getFillCharArr(this.cSeparateSymbol, this.nTotalExpectedSize)));
	}
	
	public void printContent()
	{
		this.print(this.listContents);
		this.listContents.clear();
	}
	
	private void print(List<String> list)
	{
		boolean bLeftString = false;
		String str;
		int nExpectedSize;
		for(int i = 0; i < list.size(); i++)
		{
			str = list.get(i);
			nExpectedSize = this.listExpectedSize.get(i);
			if (str.length() > nExpectedSize)
			{
				list.set(i, str.substring(nExpectedSize, str.length()));
				str = str.substring(0, nExpectedSize);
				bLeftString = true;
			}
			else
			{
				list.set(i, "");
			}
			
			System.out.print(CommonUtil.getFixedDistinctStr(str, nExpectedSize, 2, this.listPatchChars.get(i)));
		}
		
		System.out.println();
		if (bLeftString) this.print(list);
	}
}
