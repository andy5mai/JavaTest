package unit;

import java.util.Collection;
import java.util.Map;

public interface DataOutput {
	
	void writeByte(byte value);
	
	void writeBytes(byte[] value);
	
	void writeMapBytes(Map<byte[], byte[]> value);
	
	void writeShort(short value);
	
	void writeShorts(short[] value);
	
	void writeShorts(Short[] value);
	
	void writeInt(int value);
	
	void writeLong(long value);
	
	void writeLongs(long[] value);
	
	void writeFloat(float value);
	
	void writeDouble(double value);
	
	void writeString(String value);
	
	void writeStrings(String[] value);
	
	void writeStringss(String[][] value);
	
	void writeColStrs(Collection value);
	
	void writeInts(int[] value);
	
	void writeColInts(Collection<Integer> value);
	
	void writeBoolean(boolean value);

	void flush(int opType);

}
