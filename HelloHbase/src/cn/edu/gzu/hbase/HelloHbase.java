package cn.edu.gzu.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HelloHbase {
	
	private static Configuration conf = null;
	private static HBaseAdmin admin = null;
	static{
		try {
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", "192.168.101.26");
			admin = new HBaseAdmin(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void helloHbase() throws TableNotFoundException, IOException{
		HTableDescriptor tableDescriptor = admin.getTableDescriptor(Bytes.toBytes("mygirls"));
		byte[] name = tableDescriptor.getName();
		System.out.println(new String(name));
		HColumnDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
		for (HColumnDescriptor d : columnFamilies) {
		System.out.println(d.getNameAsString());
		}
	}
	/**
	 * 创建hbase新表
	 * @param tableName
	 * @param cfs
	 * @throws IOException
	 */
	public static void createTable(String tableName,String[] cfs) throws IOException{
		if(admin.tableExists(tableName)){
			System.out.println("该表已经存在！");
		}
		else{
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for(int i = 0;i < cfs.length;i++){
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("建表成功");
		}
	}
	public static void delTable(String tableName) throws IOException{
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println("删除表成功");
	}
	public static void writeRow(String tableName,String[] cfs) throws IOException{
		HTable table = new HTable(conf,tableName);
		Put put = new Put(Bytes.toBytes("row1"));
		for(int j = 0;j < cfs.length;j++){
			put.add(Bytes.toBytes(cfs[j]),
                    Bytes.toBytes(String.valueOf(1)),
                    Bytes.toBytes("value_1"));
            table.put(put);
		}
	}
	public static void selectRow(String tablename, String rowKey)
	         throws IOException {
	     HTable table = new HTable(conf, tablename);
	     Get g = new Get(rowKey.getBytes());
	     Result rs = table.get(g);
	     for (KeyValue kv : rs.raw()) {
	         System.out.print(new String(kv.getRow()) + "  ");
	         System.out.print(new String(kv.getFamily()) + ":");
	         System.out.print(new String(kv.getQualifier()) + "  ");
	         System.out.print(kv.getTimestamp() + "  ");
	         System.out.println(new String(kv.getValue()));
	     }
	}
	public static void main(String[] args) throws IOException {
		String tableName = "hellohbase";
		String[] columns = {"id","name","age"};
		//helloHbase();
		
		//createTable("hellohbase",columns);
		//delTable("hellohbase");
		//writeRow(tableName,columns);
		selectRow(tableName,"row1");
	}
}
