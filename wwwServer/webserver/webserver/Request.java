package webserver;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
public class Request {	
	private InputStream input;				//输入流
	public String url;						//url
	
	public Request(){}						
	public Request(InputStream input){
		this.input = input;
	}
	public void parse()	//取得请求信息
	{		
		int i;											//	记录读入的字符数
		StringBuffer request = new StringBuffer(2048);	//	字符串缓冲区
		byte[] buffer = new byte[2048];

		try{	
			i = input.read(buffer);						//	将数据流里数据读入到位数组buffer里
		}
		catch(Exception ex){
			ex.printStackTrace();						
			i = -1;
		}
		for(int j = 0;j < i;j++){
			request.append((char)buffer[j]);			//将位数组buffer里的字符添加到字符串数组
		}
		System.out.println("请求报文："+"\n"+request.toString());			//将请求报文打印出来
		url = parseUrl(request.toString());				//从请求报文中获取url
		System.out.println("用户请求："+this.getUrl());	//将所需资源文件名打印出来

	}
	private String parseUrl(String requestString)		//分析请求报文，并返回
	{
		int index1,index2;
		index1 = requestString.indexOf(" ");
		if(index1 != -1){
			index2 = requestString.indexOf(" ",index1 + 1);
			if(index2 > index1)
			{
				return requestString.substring((int) (index1 +1),index2);	//截取所需资源路径名
			}
		}
		return null;
	}
	public String getUrl()								//获取资源；
	{								
        if(url.compareTo("/")==0){
        	url="/index.html";         
        }         
        if(url.indexOf(".")==-1){             			//自动补全文件名
        	url+=".html";         	
        }         
 		return url; 
	}	
}
