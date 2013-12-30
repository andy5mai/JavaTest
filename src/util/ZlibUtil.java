package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jzlib.ZInputStream;

public class ZlibUtil
{
	private static Logger log = LoggerFactory.getLogger(ZlibUtil.class);
	
	//private static int nMaxSize = 2000000;
	private static int nBufferSize = 1024;
	
	public static byte[] uncompress(byte[] bsCompressed)
	{
		byte[] dataResult = null;
		
		ByteArrayInputStream byteArrInStream = null;
		ZInputStream zInStream = null;
		
		try
		{
			byteArrInStream = new ByteArrayInputStream(bsCompressed);
			zInStream = new ZInputStream(byteArrInStream);
			
	        int nTotalLen = 0;
	        int nCount = 0;
	        
	        byte[] bBuffer = new byte[nBufferSize];
	        
	        ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
		    int length = 0;
		    while( (length = zInStream.read(bBuffer)) != -1)
		    {
		    	byteArrOutStream.write(bBuffer, 0, length);
		    }
		    
		    dataResult = byteArrOutStream.toByteArray();
	        
		    byteArrInStream.close();
		    zInStream.close();
		}
		catch(Exception exc)
		{
			log.error("failed! ", exc);
		}
		
		return dataResult;
	}
	
//	public static void uncompress(RefObj refObj)
//	{
//		byte[] bs = (byte[])refObj.get();
//		refObj.set(uncompress(bs));
//	}
}
