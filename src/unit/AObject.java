package unit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import util.DataAccessUtil;

import com.google.gson.JsonObject;
import com.moregeek.persistence.redis.RedisClient;
import com.moregeek.persistence.redis.RedisData;
import com.moregeek.persistence.redis.RedisField;
import com.moregeek.persistence.redis.RedisKey;
import com.moregeek.persistence.redis.RedisData.RedisTypes;
import util.JsonUtil;

import attrs.AttrClass;
import attrs.Attrs;

public abstract class AObject implements SerializableObj
{
	protected Map<String, Object> mapProperties;

	protected RedisKey redisKey;
	protected Attrs attrs;
	protected RedisClient redisClient;
	
	public AObject(Map<String, Object> mapRedisValues
				   , RedisKey redisKey
			  	   , Attrs attrs
			  	   , RedisClient redisClient) throws Exception
	{
		Object objValue = null;
		this.mapProperties = new HashMap<String, Object>();
		this.redisClient = redisClient;
		
		for(AttrClass attrClass : attrs.getEnabledAttrs())
		{
			if (attrClass.getRedisField() == null) continue;
			
			if (attrClass.getRedisField().getValueRef() != null)
			{
				objValue = mapRedisValues.get(attrClass.getRedisField().getValueRef().getName());
				if (objValue != null) objValue = getValueBySolveSteps(attrClass.getRedisField(), objValue);
			}
			else
			{
				objValue = mapRedisValues.get(attrClass.getRedisField().getName());
			}
			
			System.out.println("put " + attrClass.getRedisField().getName() + " with value : " + objValue);
			this.mapProperties.put(attrClass.getRedisField().getName(), objValue);
		}
		
//		for(RedisField redisRefField : redisKey.getRedisRefFieldsList())
//		{
//			objValue = mapRedisValues.get(redisRefField.getValueRef().getName());
//			if (objValue == null) continue;
//
//			for(DataAccessUtil.SolveSteps solveStep : redisRefField.getSolveSteps())
//			{
//				objValue = solveStep.getMethod().invoke(this
//											 			, redisRefField
//											 			, objValue);
//			}
//			mapRedisValues.put(redisRefField.getName(), objValue);
//		}
//		
//		this.mapProperties = mapRedisValues;
		this.attrs = attrs;
	}
	
	public void setProperty(AttrClass attrClass, Object objValue)
	{
		this.mapProperties.put(attrClass.getName(), objValue);
	}
	
	public Object getProperty(AttrClass attrClass)
	{
		return this.mapProperties.get(attrClass.getName());
	}
	
	private Object getValueBySolveSteps(RedisField redisField, Object objValue) throws Exception
	{
		for(DataAccessUtil.SolveSteps solveStep : redisField.getSolveSteps())
		{
			objValue = solveStep.getMethod().invoke(this
										 			, redisField
										 			, objValue);
		}
		
		return objValue;
	}
	
	private JsonObject getJsonObjFromStr(RedisField redisField, Object objValue) throws Exception
	{
		return JsonUtil.getJsonObject((String)objValue);
	}

	private String getJsonObjAsStr(RedisField redisField, Object objValue) throws Exception
	{
		JsonObject jsonObj = (JsonObject)objValue;
		if (redisField.getJsonLevels() != null)
		{
			for(String strJsonLevel : redisField.getJsonLevels())
			{
				jsonObj = jsonObj.getAsJsonObject(strJsonLevel);
			}
		}

		System.out.println(jsonObj.toString());
		return jsonObj.get(redisField.getName()).getAsString();
	}
	
	@Override
	public void deserializeFrom(DataInput input) throws Exception
	{
		AttrClass attrClass;
		for(int i = 0; i < this.attrs.getDeserializeAttrs().length; i++)
		{
			attrClass = this.attrs.getDeserializeAttrs()[i];
			this.mapProperties.put(attrClass.getName(), attrClass.getDataType().getDeserializeMethod().invoke(input));
		}
	}

	@Override
	public void serializeTo(DataOutput output) throws Exception
	{
		AttrClass attrClass;
		Object objValue;
		for(int i = 0; i < this.attrs.getSerializeAttrs().length; i++)
		{
			attrClass = this.attrs.getSerializeAttrs()[i];
			objValue = Data.getValueByType(attrClass.getDataType(), this.mapProperties.get(attrClass.getName()));
			attrClass.getDataType().getSerializeMethod().invoke(output, objValue);
		}
	}
}
