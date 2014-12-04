package com.nhn.pinpoint.testweb.service;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ArcusClient;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

/**
 * 
 * @author netspider
 * 
 */
@Service
public class CacheServiceImpl implements CacheService, DisposableBean {

	private ArcusClient arcus;
	private MemcachedClient memcached;

	public CacheServiceImpl() {
		try {
			arcus = ArcusClient.createArcusClient("ncloud.arcuscloud.nhncorp.com:17288", "ff31ddb85e9b431c8c0e5e50a4315c27", new ConnectionFactoryBuilder());
			memcached = new MemcachedClient(AddrUtil.getAddresses("10.99.200.15:11316,10.99.200.16:11316,10.99.200.17:11316"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void arcus() {
		int rand = new Random().nextInt(100);
		String key = "pinpoint:testkey-" + rand;

		// set
		Future<Boolean> setFuture = null;
		try {
			setFuture = arcus.set(key, 10, "Hello, pinpoint." + rand);
			setFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (setFuture != null)
				setFuture.cancel(true);
		}

		// get
		Future<Object> getFuture = null;
		try {
			getFuture = arcus.asyncGet(key);
			getFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (getFuture != null)
				getFuture.cancel(true);
		}

		// del
		Future<Boolean> delFuture = null;
		try {
			delFuture = arcus.delete(key);
			delFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (delFuture != null)
				delFuture.cancel(true);
		}
	}

	@Override
	public void memcached() {
		int rand = new Random().nextInt(100);
		String key = "pinpoint:testkey-" + rand;

		// set
		Future<Boolean> setFuture = null;
		try {
			setFuture = memcached.set(key, 10, "Hello, pinpoint." + rand);
			setFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (setFuture != null)
				setFuture.cancel(true);
		}

		// get
		Future<Object> getFuture = null;
		try {
			getFuture = memcached.asyncGet(key);
			getFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (getFuture != null)
				getFuture.cancel(true);
		}

		// del
		Future<Boolean> delFuture = null;
		try {
			delFuture = memcached.delete(key);
			delFuture.get(1000L, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			if (delFuture != null)
				delFuture.cancel(true);
		}
	}

	@Override
	public void destroy() throws Exception {
		arcus.shutdown();
		memcached.shutdown();
	}

}