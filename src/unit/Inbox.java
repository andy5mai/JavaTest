package unit;

import java.util.Map;

import com.moregeek.persistence.redis.RedisClient;
import com.moregeek.persistence.redis.RedisKey;

import attrs.AttrClass;
import attrs.InboxAttrs;
import attrs.InboxCountAttrs;

public class Inbox extends InboxListView
{
	public Inbox(Map<String, Object> mapProperties
				 , RedisKey redisKey
				 , InboxAttrs inboxAttrs
				 , RedisClient redisClient) throws Exception
	{
		super(mapProperties, redisKey, inboxAttrs, redisClient);
	}
	
	private long getNewInboxId() throws Exception
	{
		return this.redisClient.addInboxCount(InboxCountAttrs.COUNT.getRedisField(), (long)9999999);
	}
}
