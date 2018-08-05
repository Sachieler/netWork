package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.PrintStream;
import java.io.PrintWriter; 
public class Response {

 	private static final int BUFFER_SIZE = 1024;          //设置默认缓冲区大小 1024

 	Request request;                                      // 请求
 	OutputStream output;                                  // 输出流

 	public Response(OutputStream output){         
 		this.output = output;     
 	}     
 	public void setRequest(Request request){         
 		this.request = request;     
 	}     
 	
 	public void sendStaticResource() throws IOException              //发送请求资源    
 	{
 		//byte[] bytes = new byte[BUFFER_SIZE];                
 		FileInputStream finputstream = null;                                    //文件输入流
 		try{
 			File file = new File(HttpServer.Web_Root,request.getUrl());  //打开一个在Web_Root路径下的文件
 			if(file.exists())
 			{		
                System.out.println("开始发送用户请求资源..."); 
                
 				String headMessage = "HTTP/1.1 200 OK\r\n" +                                       
						"Content-Type:text/html\r\n" + "\r\n";
   
                //输出文件中的数据
                
                finputstream = new FileInputStream(file);                   //打开文件输入流
               
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(headMessage);
                byte[] buffer = new byte[2048]; 
                int i = 0;
				try {
					i = finputstream.read(buffer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					i = -1;
				} 							
				//将文件读入buffer
                for(int j = 0;j<i;j++){
                	stringBuffer.append((char)buffer[j]);
                }
                PrintWriter printWriter = new PrintWriter(output,true); 
                printWriter.println(stringBuffer);
                //System.out.println(stringBuffer);
                /*
                while (ch != -1){                     
                	output.write(bytes, 0, ch);                      
                	ch = fis.read(bytes, 0, BUFFER_SIZE);                 
                }
                */                 
                System.out.println("发送完毕！"); 				
 			}
 			
 			else
  			{                 
        		System.out.println("用户请求的资源不存在");                 
        		String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +                                       
        										"Content-Type:text/html\r\n" +                                       
        										"\r\n" +                                       
        										"<hl>File Not Found</hl>";
        		System.out.println("响应报文："+"\n"+errorMessage);
        		//output.write(errorMessage.getBytes());             
  			} 
  			
 		}
 		catch(Exception ex){
 			ex.printStackTrace();
 			System.out.println("获取请求资源错误，请检查本地资源设置！");
 			System.exit(1);
 		}
 		if(finputstream != null){
 			finputstream.close();
 		}
 	} 
}
