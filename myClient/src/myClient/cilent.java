package myClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
 
public class cilent
{
	/*
	public static void myclient(String[] args) throws UnknownHostException, IOException
	{
		runSocket();
		
	}
	*/
	static void runSocket() throws IOException	//创建socket，建立连接，从url栏获取并发送url，从服务器获取资源，并将其写入到资源栏
	{
		String []res = new String[3];	//res[0]:ip地址 res[1]:端口号 res[2]:所需资源路径
		String str = frame.str;			//url
		String reSrc = null;			//获取的资源
        String message = strParse(str,res);		//得到请求报文信息
		final String HOST = res[0];

		final int PORT = Integer.parseInt(res[1]);//端口号

		Socket socket = new Socket(HOST, PORT);//创建一个客户端连接
		
		OutputStream out = socket.getOutputStream();//获取服务端的输出流，为了向服务端输出数据
		PrintWriter bufw=new PrintWriter(out,true);	//标准写入器	
		
		bufw.println(message);//把窗体输入的内容送入输出流里面
		
		InputStream in=socket.getInputStream();//获取服务端的输入流，为了获取服务端输入的数据
		BufferedReader bufr=new BufferedReader(new InputStreamReader(in));	// 读出器
		
		//读取服务端传来的数据

		/*
		char[] tempBuffer = new char[2048];
		StringBuffer response = new StringBuffer();
		String src;
		int index;
		bufr.read(tempBuffer);
		response.append(tempBuffer);
		System.out.println("响应报文："+"\n"+response);
		index = response.indexOf("<");
		src = response.substring(index);
		frame.srcArea.setText(src);
		*/
		
		
		int i,index;
		StringBuffer response = new StringBuffer(); 
		byte[] tempBuffer = new byte[50000];
		try{
			i = in.read(tempBuffer);
		}catch(Exception ex){
			ex.printStackTrace();
			i = -1;
		}
		for(int j = 0;j<i;j++){
			response.append((char)tempBuffer[j]);
		}
		System.out.println("响应报文："+"\n"+response);
		index = response.indexOf("<");
		reSrc = response.substring(index);
		
		frame.srcArea.setText(reSrc);
		
		bufw.close();
		bufr.close();
		socket.close();
	}

	private static String strParse(String str,String []res)			//分析url，并将其转换成请求报文
	{
		int index1,index2,index3;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ); //保存当前日期
		String date = sdf.format(new Date());
		
		index1 = str.indexOf("//");
		index2 = str.indexOf(":",index1+1);
		if(index2!=-1){
			index3 = str.indexOf("/",index2+1);
			res[0] = str.substring((int)index1+2,index2);
			if(index3!=-1){
				res[1] = str.substring((int)index2+1,index3);
				res[2] = str.substring(index3);
			}
			else{
				res[1] = str.substring((int)index2+1);
				res[2] = "/";
			}
		}
		else{
			index3 = str.indexOf("/",index1+2);
			res[1] = "8888";
			if(index3!=-1){
				res[0] = str.substring((int)index1+2,index3);
				res[2] = str.substring(index3);
			}
			else{
				res[0] = str.substring((int)index1+2);
				res[2] = "/";
			}
		}
		if(index3==-1)
			frame.urlText.setText(str+"/");
		return "GET "+res[2]+" "+"HTTP/1.1"+"\n"+
				"Host: "+res[0]+":"+res[1]+"\n"+
				"Connection: close"+"\n"+
				"Accept: text/html"+"\n"+
				"Accept-Charset: UTF-8";
				/*+"\n"+
				"Date: "+date+"\n";
				*/								
	}
}
