package pw.mcbus.www.CQP_Socket;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import sun.misc.*;

public class MainClass extends JavaPlugin  {
	Map<String,UUID> map;
	Map<String,CommandSender> pmap;
	DatagramSocket ds;
	Map<String,String> config;
	String GroupID;
	@SuppressWarnings("unchecked")
	@Override
	//插件启动
	public void onEnable(){
		getCommand("CQP").setExecutor(new Commander());
		try
		{
			//建立配置文件
			File mapfile=new File(".\\plugins\\CQP\\Map.dat");
			if(mapfile.exists())
			{
				Object temp=null;
				FileInputStream in;
				try{
					in=new FileInputStream(mapfile);
					ObjectInputStream objIn =new ObjectInputStream(in);
					temp=objIn.readObject();
					objIn.close();
					map=(Map<String,UUID>)temp;
					System.out.println("[CQP]read map success!");
				}catch(IOException e){
					System.out.println("[ERROR][CQP]read map failed");
					e.printStackTrace();
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}
			else
			{
				map=new HashMap<String,UUID>();
			}
			pmap=new HashMap<String,CommandSender>();
			
			File confile=new File(".\\plugins\\CQP\\config.dat");
			if(confile.exists())
			{
				Object temp=null;
				FileInputStream in;
				try{
					in=new FileInputStream(mapfile);
					ObjectInputStream objIn =new ObjectInputStream(in);
					temp=objIn.readObject();
					objIn.close();
					config=(Map<String,String>)temp;
				}catch(IOException e){
					System.out.println("[ERROR][CQP]read config failed");
					e.printStackTrace();
				}catch(ClassNotFoundException e){
					e.printStackTrace();
				}
			}
			else
			{
				config=new HashMap<String,String>();
				config.put("GroupID", "304279325");
			}
			GroupID=config.get("GroupID");
			
			client=new DatagramSocket();//建立与酷Q的通讯

			RCV rcver=new RCV();
			rcver.start();//开始监听30236端口
			new Timer().schedule(new MyTimerTask(), 10000);//定时向服务器发送信息
		}catch (IOException e){}
	}
	@Override
	//插件关闭
	public void onDisable(){
		{//保存map对象到Map.dat文件
		File mapfile=new File(".\\plugins\\CQP\\Map.dat");
		FileOutputStream out;
		try{
			out=new FileOutputStream(mapfile);
			ObjectOutputStream objOut=new ObjectOutputStream(out);
			objOut.writeObject(map);
			objOut.flush();
			objOut.close();
			System.out.println("[CQP]write map success!");
		}catch (IOException e)
		{
			System.out.println("[ERROR][CQP]write map failed");
		}
		ds.close();}{
			File mapfile=new File(".\\plugins\\CQP\\config.dat");
			FileOutputStream out;
			try{
				out=new FileOutputStream(mapfile);
				ObjectOutputStream objOut=new ObjectOutputStream(out);
				objOut.writeObject(config);
				objOut.flush();
				objOut.close();
				System.out.println("[CQP]write config success!");
			}catch (IOException e)
			{
				System.out.println("[ERROR][CQP]write config failed");
			}
			ds.close();
		}
	}
	
		class MyTimerTask extends TimerTask{
		public void run()
		{
			try{
			Send("ClientHello 30236");
		}catch (IOException e){}
			new Timer().schedule(new MyTimerTask(), 240000);
		}
	}
	
	DatagramSocket client;
	private void Send(String msg) throws IOException
	{
		byte[] sendBuf;
		sendBuf=msg.getBytes();
		InetAddress addr=InetAddress.getByName("localhost");
		int port=11235;
		DatagramPacket sendPacket
			=new DatagramPacket(sendBuf,sendBuf.length,addr,port);
		client.send(sendPacket);
	}
	class Commander implements CommandExecutor{
		public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
			if(arg3.length==0)
			{
				arg0.sendMessage("该插件由回形针工作室出品\n/CQP <bind/list/delete/OP>");
				return false;
			}
			switch(arg3[0]){
			case "bind"://绑定QQ
			{
				//随机验证码
			int ic=(int)(100000+Math.random()*(999999-100000+1));
			while(pmap.containsKey(ic+"")){
				ic=(int)(100000+Math.random()*(999999-100000+1));
			}
			pmap.put(ic+"", arg0);
			arg0.sendMessage("[CQP]Now send \""+ic+"\" to QQgroup.");
			break;
			}
			case "list"://列出绑定的QQ
				arg0.sendMessage("The list of the QQ which are bound you:");
				arg0.sendMessage("--START--");
				for(String key:map.keySet()){
					if(map.get(key)==((Player)arg0).getUniqueId());
						arg0.sendMessage(key+";\n");
				}
				arg0.sendMessage("---END---");
				break;
			//case "M_bind"://手动绑定（手动输入QQ号）
			//{
			//	map.put(arg3[1], ((Player)arg0).getUniqueId());
			//	arg0.sendMessage("success!");
			//	break;
			//}
			case "delete"://删除
				if(arg3.length<2)
				{
					arg0.sendMessage("该插件由回形针工作室出品\n/CQP delete <QQ>");
					return false;
				}
				//检查是否存在
				if(!(map.containsKey(arg3[1]))&&(map.get(arg3[1]).equals(((Player)arg0).getUniqueId())))
				{
					arg0.sendMessage("Can not find this QQ on you");
					break;
				}
				map.remove(arg3[1]);
				arg0.sendMessage("success!");
				break;
			case "OP":
			{
				if(arg3.length<2)
				{
					arg0.sendMessage("该插件由回形针工作室出品\n/CQP OP <setGroupID/OP_M_delete>");
					return false;
				}
				if(!arg0.isOp())
				{
					arg0.sendMessage("You aren't OP!");
					break;
				}
				switch(arg3[1])
				{
				case "setGroupID"://设置群号
					if(arg3.length<3)
					{
						arg0.sendMessage("该插件由回形针工作室出品\n/CQP OP setGroupID <GroupID>");
						return false;
					}
					GroupID=arg3[2];
					config.remove("GroupID");
					config.put("GroupID", arg3[2]);
					arg0.sendMessage("Set GroupID:"+GroupID+" success!");
					break;
				case "OP_M_delete"://OP强行解绑
					if(arg3.length<3)
					{
						arg0.sendMessage("该插件由回形针工作室出品\n/CQP OP OP_M_delete <QQ>");
						return false;
					}
					map.remove(arg3[2]);
					arg0.sendMessage("sucess!");
					break;
				}
			}
			}

			return true;
		}
	}
	class RCV extends Thread{
		public void run(){
			try{
			ds = new DatagramSocket(30236);//定义服务，监视端口上面的发送端口，注意不是send本身端口
			while (true)
			{
				byte[] buf = new byte[1024];//接受内容的大小，注意不要溢出
				DatagramPacket dp = new DatagramPacket(buf,0,buf.length);//定义一个接收的包
				ds.receive(dp);//将接受内容封装到包中
				String data = new String(dp.getData(), 0, dp.getLength());//利用getData()方法取出内容
				String[] datas=data.split(" ");
				System.out.printf(data);
				if(datas[0].equalsIgnoreCase("GroupMessage")&&datas[1].equalsIgnoreCase(GroupID))
				{
					if(pmap.containsKey(decodeBase64(datas[3])))
					{
						map.put(datas[2], ((Player)(pmap.get(decodeBase64(datas[3])))).getUniqueId());
						pmap.get(decodeBase64(datas[3])).sendMessage("[CQP]success bind \""+pmap.get(decodeBase64(datas[3])).getName()+"\" to "+datas[2]);
						continue;
					}
					if(map.containsKey(datas[2])){
						Bukkit.broadcastMessage("<"+Bukkit.getPlayer(map.get(datas[2])).getName()+"> "+decodeBase64(datas[3]));
					}else{
						//Bukkit.broadcastMessage(datas[2]+":"+decodeBase64(datas[3]));
					}
				}
			}
			}catch (IOException e){}
		}
		public String decodeBase64(String s){
			byte[]b=null;
			String result=null;
			if(s !=null){
				BASE64Decoder decoder=new BASE64Decoder();
				try{
					b=decoder.decodeBuffer(s);
					result=new String(b,"GBK");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return result;
		}  
	}
}
