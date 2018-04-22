package com.mmall.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import ch.qos.logback.classic.Logger;

/**
 * 缓存
* @ClassName: TokenCache  
* @Description: TODO  
* @author Administrator  
* @date 2018年4月14日  
*
 */
public class TokenCache {
   private static Logger logger=(Logger) LoggerFactory.getLogger(TokenCache.class);
   public static final String TOKEN_PREFIX="token_";
   //LRU算法
   private static LoadingCache<String, String> loaclCache=CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String,String>(){
    //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载。
	@Override
	public String load(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return "null";
	}
	   
	   
   });
   public static void setKey(String key,String value){
	   loaclCache.put(key, value);
   }
   public static String getKey(String Key){
	   String value=null;
	   try {
		   value=loaclCache.get(Key);
		   if("null".equals(value)){
			   return null;
		   }
		return value;
	} catch (Exception e) {
		logger.error("localCache get error",e);
	}
	   return null;
	   
   }
}
