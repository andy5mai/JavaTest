package util;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CommonUtil
{
	public enum Status
	{
		N((short)0),
		Y((short)1);
		
		private short sStatus;
		private Status(short sStatus)
		{
			this.sStatus = sStatus;
		}
		
		public short getValue()
		{
			return this.sStatus;
		}
	}
	
	public static String getFixedStr(String str, int nSize, char cPatchChar)
	{
		StringBuffer sb = new StringBuffer("");
		sb.append(str);
		
		int nLength = nSize - sb.length();
		if (nLength < 0) nLength = 0;
		
		sb.append(getFillCharArr(cPatchChar, nLength));
		
		return sb.toString();
	}
	
	public static String getFixedDistinctStr(String str, int nSize, int nDistinctSize, char cPatchChar)
	{
		return getFixedStr(str, nSize + nDistinctSize, cPatchChar); 
	}
	
	public static char[] getFillCharArr(char c, int nSize)
	{
		char[] cs = new char[nSize];
		Arrays.fill(cs, c);
		
		return cs;
	}
	
	public static Method getAccessibleMethod(Class<?> klass, String strMethodName, Class<?>... klassesParams) throws Exception
	{
		Method method = klass.getDeclaredMethod(strMethodName, klassesParams);
		method.setAccessible(true);
		
		return method;
	}
	
	public static String getStr(byte[] bs)
	{
		return new String(bs, Charset.forName("UTF-8"));
	}
	
	public static byte[] getStrBytes(String str)
	{
		return str.getBytes(Charset.forName("UTF-8"));
	}
}
