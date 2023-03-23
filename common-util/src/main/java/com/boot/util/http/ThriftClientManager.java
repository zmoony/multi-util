//缓存池
package com.boot.util.http;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClientManager<T> {
	private GenericObjectPool<T> s_clientPool;

	// 构造时需输入图片服务ip地址和端口以及最大thrift客户端个数
	public ThriftClientManager(String ip, int port, int maxClientCount) {
		GenericObjectPoolConfig objectPoolConfig = new GenericObjectPoolConfig();
		objectPoolConfig.setMaxTotal(maxClientCount);
		objectPoolConfig.setMaxIdle(maxClientCount);
		objectPoolConfig.setMinIdle(maxClientCount);
		objectPoolConfig.setTestOnReturn(true);
		objectPoolConfig.setMaxWaitMillis(-1);// 永远等待

		s_clientPool = new GenericObjectPool<T>(new ThriftClientFactory(ip, port),
				objectPoolConfig);
	}


}

class ThriftClientFactory<T> implements PooledObjectFactory<T> {
	String m_Ip;
	int m_port;

	public ThriftClientFactory(String ip, int port) {
		m_Ip = ip;
		m_port = port;
	}

	public ThriftClientFactory() {
		m_Ip = "127.0.0.1";
		m_port = 6552;
	}

	@Override
	public PooledObject<T> makeObject() throws Exception {
		TTransport transport = new TSocket(m_Ip, m_port);
		TProtocol protocol = new TBinaryProtocol(transport);
//		T client = new T(protocol);
		transport.open();
//		return new DefaultPooledObject<T>(client);
		return null;
	}

	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		T client = p.getObject();
		if (null != client) {
//			client.getInputProtocol().getTransport().close();
		}

	}

	@Override
	public boolean validateObject(PooledObject<T> p) {

//		TTransport tTransport = p.getObject().getInputProtocol().getTransport();

//		return tTransport.isOpen();
		return false;
	}

	@Override
	public void activateObject(PooledObject<T> p) throws Exception {

	}

	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {

	}
}
