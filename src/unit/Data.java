package unit;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import util.CommonUtil;
import util.DataAccessUtil;

public class Data
{
	public enum Types
	{
		Byte(null, null, null, null),
		Bytes(null, null, null, null),
		Bool(null, null, null, null),
		Short(null, null, null, null),
		Shorts(null, null, null, null),
		Int(null, null, null, null),
		Ints(null, null, null, null),
		ColInts(null, null, null, null),
		Long(null, null, null, null),
		Longs(null, null, null, null),
		Float(null, null, null, null),
//		Floats(null, null, null, null),
		Double(null, null, null, null),
//		Doubles(null, null, null, null),
		String(null, null, null, null),
		Strings(null, null, null, null),
		Stringss(null, null, null, null),
		ColStrs(null, null, null, null),
		MapBytes(null, null, null, null);
		
		private Class clazzData;
		private Method methodGetValue;
		private Method methodSerialize;
		private Method methodDeserialize;
		
		private Types(Class clazzData, Method methodGetValue, Method methodSerialize, Method methodDeserialize)
		{
			this.clazzData = clazzData;
			this.methodGetValue = methodGetValue;
			this.methodSerialize = methodSerialize;
			this.methodDeserialize = methodDeserialize;
		}
		
		public void setDataClass(Class clazzData)
		{
			this.clazzData = clazzData;
		}
		
		public Class getDataClass()
		{
			return this.clazzData;
		}
		
		public void setGetValueMethod(Method methodGetValue)
		{
			this.methodGetValue = methodGetValue;
		}
		
		public Method getGetValueMethod()
		{
			return this.methodGetValue;
		}
		
		public void setSerializeMethod(Method methodSerialize)
		{
			this.methodSerialize = methodSerialize;
		}
		
		public Method getSerializeMethod()
		{
			return this.methodSerialize;
		}
		
		public void setDeserializeMethod(Method methodDeserialize)
		{
			this.methodDeserialize = methodDeserialize;
		}
		
		public Method getDeserializeMethod()
		{
			return this.methodDeserialize;
		}
		
		static
		{
			try
			{
				Types.Byte.setDataClass(byte.class);
				Types.Byte.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeByte", byte.class));
				Types.Byte.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readByte"));
				Types.Byte.setGetValueMethod(CommonUtil.getAccessibleMethod(Byte.class, "parseByte", String.class));
				
				Types.Bytes.setDataClass(byte[].class);
				Types.Bytes.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeBytes", byte[].class));
				Types.Bytes.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readBytes"));
				
				Types.Bool.setDataClass(boolean.class);
				Types.Bool.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeBoolean", boolean.class));
				Types.Bool.setGetValueMethod(CommonUtil.getAccessibleMethod(Boolean.class, "parseBoolean", String.class));
				
				Types.Short.setDataClass(short.class);
				Types.Short.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeShort", short.class));
				Types.Short.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readShort"));
				Types.Short.setGetValueMethod(CommonUtil.getAccessibleMethod(Short.class, "parseShort", String.class));
				
				Types.Shorts.setDataClass(short[].class);
				Types.Shorts.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeShorts", short[].class));
				Types.Shorts.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readShorts"));
				
				Types.Int.setDataClass(int.class);
				Types.Int.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeInt", int.class));
				Types.Int.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readInt"));
				Types.Int.setGetValueMethod(CommonUtil.getAccessibleMethod(Integer.class, "parseInt", String.class));
				
				Types.Ints.setDataClass(int[].class);
				Types.Ints.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeInts", int[].class));
				//Types.Ints.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readInts"));
				
				Types.ColInts.setDataClass(Collection.class);
				Types.ColInts.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeColInts", Collection.class));
				//Types.ColInts.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readColInts"));
				
				Types.Long.setDataClass(long.class);
				Types.Long.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeLong", long.class));
				Types.Long.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readLong"));
				Types.Long.setGetValueMethod(CommonUtil.getAccessibleMethod(Long.class, "parseLong", String.class));
				
				Types.Longs.setDataClass(long[].class);
				Types.Longs.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeLongs", long[].class));
				//Types.Longs.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readLongs"));
				
				Types.Float.setDataClass(float.class);
				Types.Float.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeFloat", float.class));
				Types.Float.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readFloat"));
				Types.Float.setGetValueMethod(CommonUtil.getAccessibleMethod(Float.class, "parseFloat", String.class));
				
	//				Floats(CommonUtil.getAccessibleMethod(DataOutput.class, "writeFloats", float[].class)
	//						, CommonUtil.getAccessibleMethod(DataOutput.class, "readFloats")),
				
				Types.Double.setDataClass(double.class);
				Types.Double.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeDouble", double.class));
				Types.Double.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readDouble"));
				Types.Double.setGetValueMethod(CommonUtil.getAccessibleMethod(Double.class, "parseDouble", String.class));
				
	//				Doubles(CommonUtil.getAccessibleMethod(DataOutput.class, "writeDoubles", double[].class)
	//						, CommonUtil.getAccessibleMethod(DataOutput.class, "readDoubles")),
				
				Types.String.setDataClass(String.class);
				Types.String.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeString", String.class));
				Types.String.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readString"));
				
				Types.Strings.setDataClass(String[].class);
				Types.Strings.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeStrings", String[].class));
				Types.Strings.setDeserializeMethod(CommonUtil.getAccessibleMethod(DataInput.class, "readStrings"));
				
				Types.Stringss.setDataClass(String[][].class);
				Types.Stringss.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeStringss", String[][].class));
				
				Types.ColStrs.setDataClass(Collection.class);
				Types.ColStrs.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeColStrs", Collection.class));
				
				Types.MapBytes.setDataClass(Map.class);
				Types.MapBytes.setSerializeMethod(CommonUtil.getAccessibleMethod(DataOutput.class, "writeMapBytes", Map.class));
				Types.MapBytes.setGetValueMethod(CommonUtil.getAccessibleMethod(DataAccessUtil.class, "doNothing", Object.class));
			}
			catch(Exception exc)
			{
				System.out.println("Data types init error! " + exc.getMessage());
			}
		}
	}
	
	public static Object getValueByType(Data.Types dataType, Object objValue) throws Exception
	{
		if (objValue.getClass() == dataType.getDataClass()) return objValue;
		
		return dataType.getGetValueMethod().invoke(null, objValue);
	}
}
