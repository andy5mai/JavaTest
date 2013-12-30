package unit;

public interface DataInput {

	public byte readByte();
	
	public byte[] readBytes();
	
	public short readShort();
	
	public Short[] readShorts();
	
	public int readInt();
	
	public long readLong();
	
	public float readFloat();
	
	public double readDouble();
	
	public String readString();
	
	public String[] readStrings();
	
	public int readableBytes();
}
