package attrs;

import java.lang.reflect.Method;

import unit.Data;

import com.moregeek.persistence.redis.RedisField;

public class AttrClass
{
	private String strName;
//	private Types type;
	private String strDefaultValue;
//	private AttrClass attrClass;
	private RedisField redisField;
	
	private Data.Types dataType;
	
	public AttrClass(String strName
					 , String strDefaultValue
					 , Data.Types dataType
					 , RedisField redisField)
	{
		this.strName = strName;
		this.strDefaultValue = strDefaultValue;
		this.dataType = dataType;
		this.redisField = redisField;
	}
	
	public String getName()
	{
		return this.strName;
	}
	
	public String getDefaultValue()
	{
		return this.strDefaultValue;
	}
	
	public RedisField getRedisField()
	{
		return this.redisField;
	}
	
	public Data.Types getDataType()
	{
		return this.dataType;
	}
}
