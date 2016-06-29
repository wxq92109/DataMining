package cn.edu.gzu.hadoop.rpc;

import java.io.IOException;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

public class RPCServer implements Bizable{

	public String sayHi(String name)
	{
		return "HI ~ "+name;
	}
	public static void main(String[] args) throws HadoopIllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		Server server = new RPC.Builder(new Configuration())
		.setInstance(new RPCServer())
		.setBindAddress("192.168.100.207")
		.setPort(9527)
		.setProtocol(Bizable.class)
		.build();
		server.start();
	}

}
