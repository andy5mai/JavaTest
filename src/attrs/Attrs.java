package attrs;

import java.util.HashSet;
import java.util.Set;

import unit.Data;

import com.moregeek.persistence.redis.RedisField;

public abstract class Attrs
{
	protected Set<AttrClass> attrsEnabled = new HashSet<AttrClass>();
	protected AttrClass[] attrsSerialize;
	protected AttrClass[] attrsDeserialize;

//	public Attrs(String[] strsNames
//				 , String[] strsDefaultValues
//				 , AttrClass[] serializeArrs
//				 , AttrClass[] deserializeArrs) throws Exception
//	{
//		for(int i = 0; i < strsNames.length; i++)
//		{
//			this.getClass().getField(strsNames[i]).set(this, new AttrClass(strsNames[i], strsDefaultValues[i]));
//		}
//		
//		this.serializeAttrs = serializeArrs;
//		this.deserializeAttrs = deserializeArrs;
//	}
	
	public void setAttrClass(String strName
									, String strDefaultValue
									, Data.Types dataType
									, RedisField redisField) throws Exception
	{
		AttrClass attrClass = new AttrClass(strName
											 , strDefaultValue
											 , dataType
											 , redisField);
		this.attrsEnabled.add(attrClass);
		this.getClass().getField(strName).set(null, attrClass);
	}
	
//	public static void setSerializeAttrs(Class<? extends Attrs> ClassAttrs, AttrClass[] attrsSerialize) throws Exception
//	{
////		Field field = ClassAttrs.getField("attrsSerialize");
////		field.setAccessible(true);
////		field.set(null, attrsSerialize);
//		Attrs.attrsSerialize = attrsSerialize;
//	}
//	
//	public static void setDeserializeAttrs(Class<? extends Attrs> ClassAttrs, AttrClass[] attrsDeserialize) throws Exception
//	{
////		Field field = ClassAttrs.getField("attrsDeserialize");
////		field.setAccessible(true);
////		field.set(null, attrsDeserialize);
//		Attrs.attrsDeserialize = attrsDeserialize;
//	}
//	
	public Set<AttrClass> getEnabledAttrs()
	{
		return this.attrsEnabled;
	}
	
	public AttrClass[] getSerializeAttrs()
	{
		return this.attrsSerialize;
	}
	
	public void setSerializeAttrs(AttrClass[] attrsSerialize)
	{
		this.attrsSerialize = attrsSerialize;
	}
	
	public AttrClass[] getDeserializeAttrs()
	{
		return this.attrsDeserialize;
	}
	
	public void setDeserializeAttrs(AttrClass[] attrsDeserialize)
	{
		this.attrsDeserialize = attrsDeserialize;
	}
}
