package unit;

import java.util.Map;

import com.moregeek.persistence.redis.RedisKey;
import com.moregeek.persistence.redis.RedisClient;

import attrs.AvatarAttrs;

public class Avatar extends AObject
{
	public Avatar(Map<String, Object> mapProperties
				 , RedisKey redisKey
				 , AvatarAttrs avatarAttrs
				 , RedisClient redisClient) throws Exception
	{
		super(mapProperties
			  , redisKey
			  , avatarAttrs
			  , redisClient);
	}
	
	
}
