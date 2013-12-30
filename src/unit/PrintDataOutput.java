package unit;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import util.CommonUtil;

public class PrintDataOutput implements DataOutput
{
	private Method methodWriteByte;
	private Method methodWriteByteArray;
	private Method methodWriteMap;
	private Method methodWriteShort;
	private Method methodWriteShorts;
	private Method methodWriteInt;
	private Method methodWriteLong;
	private Method methodWriteLongs;
	private Method methodWriteFloat;
	private Method methodWriteDouble;
	private Method methodWriteBytes;
	private Method methodWriteString;
	
	public PrintDataOutput() throws Exception
	{
		this.methodWriteByte = CommonUtil.getAccessibleMethod(DataOutput.class, "writeByte", byte.class);
		this.methodWriteByteArray = CommonUtil.getAccessibleMethod(DataOutput.class, "writeBytes", byte[].class);
		
		this.methodWriteLong = CommonUtil.getAccessibleMethod(DataOutput.class, "writeLong", long.class);
		this.methodWriteShort = CommonUtil.getAccessibleMethod(DataOutput.class, "writeShort", short.class);
		this.methodWriteString = CommonUtil.getAccessibleMethod(DataOutput.class, "writeString", String.class);
		
	}
	
	public Method getWriteByteMethod()
	{
		return this.methodWriteByte;
	}
	
	public Method getWriteByteArrayMethod()
	{
		return this.methodWriteByteArray;
	}
	
	public Method getWriteShortMethod()
	{
		return this.methodWriteShort;
	}
	
	public Method getWriteStringMethod()
	{
		return this.methodWriteString;
	}
	
	public Method getWriteLongMethod()
	{
		return this.methodWriteLong;
	}

	@Override
	public void writeByte(byte value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeBytes(byte[] value) {
		// TODO Auto-generated method stub
		for(byte b : value)
		{
			System.out.println(b);
		}
	}

	@Override
	public void writeMapBytes(Map<byte[], byte[]> value) {
		// TODO Auto-generated method stub
		for(byte[] bs : value.keySet())
		{
			System.out.println("map key : " + CommonUtil.getStr(bs) + ", value : " + CommonUtil.getStr(value.get(bs)));
		}
	}

	@Override
	public void writeShort(short value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeShorts(short[] value) {
		// TODO Auto-generated method stub
		for(short s : value)
		{
			System.out.println(s);
		}
	}

	@Override
	public void writeShorts(Short[] value) {
		// TODO Auto-generated method stub
		for(Short s : value)
		{
			System.out.println(s);
		}
	}

	@Override
	public void writeInt(int value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeLong(long value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeLongs(long[] value) {
		// TODO Auto-generated method stub
		for(long l : value)
		{
			System.out.println(l);
		}
	}

	@Override
	public void writeFloat(float value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeDouble(double value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeString(String value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void writeStrings(String[] value) {
		// TODO Auto-generated method stub
		for(String str : value)
		{
			System.out.println(str);
		}
	}

	@Override
	public void writeStringss(String[][] value) {
		// TODO Auto-generated method stub
		for(String[] strs : value)
		{
			for(String str : strs)
			{
				System.out.println(str);
			}
		}
	}

	@Override
	public void writeColStrs(Collection value) {
		// TODO Auto-generated method stub
		Iterator it = value.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
	}

	@Override
	public void writeInts(int[] value) {
		// TODO Auto-generated method stub
		for(int n : value)
		{
			System.out.println(n);
		}
	}

	@Override
	public void writeColInts(Collection<Integer> value) {
		// TODO Auto-generated method stub
		Iterator<Integer> it = value.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
	}

	@Override
	public void writeBoolean(boolean value) {
		// TODO Auto-generated method stub
		System.out.println(value);
	}

	@Override
	public void flush(int opType) {
		// TODO Auto-generated method stub
		
	}

}
