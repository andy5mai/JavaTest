package unit;

import java.util.Map;

import com.moregeek.persistence.redis.RedisClient;
import com.moregeek.persistence.redis.RedisKey;

import attrs.InboxListViewAttrs;

public class InboxListView extends AObject
{
	public InboxListView(Map<String, Object> mapProperties
						, RedisKey redisKey
						, InboxListViewAttrs inboxListViewAttrs
						, RedisClient redisClient) throws Exception
	{
		super(mapProperties, redisKey, inboxListViewAttrs, redisClient);
	}
}
