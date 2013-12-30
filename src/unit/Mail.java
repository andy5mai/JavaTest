package unit;

import java.util.Map;

import com.moregeek.persistence.redis.RedisClient;
import com.moregeek.persistence.redis.RedisKey;

import attrs.AvatarAttrs;
import attrs.MailAttrs;

public class Mail extends Inbox
{
	public Mail(Map<String, Object> mapProperties
				, RedisKey redisKey
				, MailAttrs mailAttrs
				, RedisClient redisClient) throws Exception
	{
		super(mapProperties
			  , redisKey
			  , mailAttrs
			  , redisClient);
		
		System.out.println("mapProperties : " + this.mapProperties.size());
		String strAttachmentId = (String)this.mapProperties.get(MailAttrs.ATTACHMENT_ID.getName());
		
		if (strAttachmentId != null
			&& !strAttachmentId.isEmpty())
		{
			this.mapProperties.put(MailAttrs.Attachment.getName()
								   ,redisClient.getAllValues(RedisKey.Types.INBOX_AID_WEB.getRedisKey(), strAttachmentId));
		}
	}
	
	public void setPortraitObjId(Avatar avatar)
	{
		this.mapProperties.put(MailAttrs.portraitObjId.getName(), avatar.getProperty(AvatarAttrs.portraitObjId));
	}
	
	public void setAttachment(Map<byte[], byte[]> mapAttachment)
	{
		this.mapProperties.put(MailAttrs.Attachment.getName(), mapAttachment);
	}
}
