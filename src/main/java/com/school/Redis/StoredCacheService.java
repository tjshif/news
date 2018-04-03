package com.school.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;

@Service
public class StoredCacheService {
	@Autowired
	private ShardedJedisSentinelPool shardedJedisSentinelPool;

	public Boolean exists(String key)
	{
		ShardedJedis resource = null;
		boolean broken = false;
		Boolean bExit = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			bExit = resource.exists(key);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return bExit;
	}

	public Long del(String key)
	{
		ShardedJedis resource = null;
		Long ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.del(key);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public void set(String key, String value) {
		ShardedJedis resource = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			resource.set(key, value);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
	}

	public String get(String key) {
		ShardedJedis resource = null;
		String string = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			string = resource.get(key);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return string;
	}

	public Long zadd(String key, double score, String member)
	{
		ShardedJedis resource = null;
		Long ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zadd(key, score, member);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Long zadd(String key, Map<String, Double> scoreMembers)
	{
		ShardedJedis resource = null;
		Long ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zadd(key,scoreMembers);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Set<Tuple> zrangeWithScores(String key, long start, long end)
	{
		ShardedJedis resource = null;
		Set<Tuple> ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zrangeWithScores(key,start, end);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count)
	{
		ShardedJedis resource = null;
		Set<String> ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zrevrangeByScore(key,max, min, offset, count);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Set<String> zrange(String key, long start, long end)
	{
		ShardedJedis resource = null;
		Set<String> ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zrange(key, start, end);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Long zremrangeByRank(String key, long start, long end)
	{
		ShardedJedis resource = null;
		Long ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Long zcard(String key)
	{
		ShardedJedis resource = null;
		Long ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zcard(key);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}

	public Set<String> zrevrange(String key, long start, long end)
	{
		ShardedJedis resource = null;
		Set<String> ret = null;
		boolean broken = false;
		try {
			resource = shardedJedisSentinelPool.getResource();
			ret = resource.zrevrange(key, start, end);
		} catch (Exception e) {
			broken = true;
			e.printStackTrace();
		} finally {
			close(resource,broken);
		}
		return ret;
	}
	/**
	 * 注意，此方法需要try-catch，因为当master发生变更后，监控线程会重新初始化连接池中的连接，造成抛错
	 */
	public void close(ShardedJedis resource, boolean broken) {
		if (null == resource) {
			return;
		}
		try {
			if(broken){
				shardedJedisSentinelPool.returnBrokenResource(resource);
			}else{
				shardedJedisSentinelPool.returnResource(resource);
			}
		} catch (Exception e) {
			resource.disconnect();
			e.printStackTrace();
		}
	}
}
