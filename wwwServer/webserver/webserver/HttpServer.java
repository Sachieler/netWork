package webserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.Enumeration; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.PrintStream; 
public class HttpServer {
	private int iPort = 8888;	//默认端口
	public static String Basic_Root = System.getProperty("user.dir");	//获取java项目路径
	public static String Web_Root = System.getProperty("user.dir")		
			+ File.separator + "webroot";								//准备创建的网页文件路径		
	public static int count = 0;
	
	public HttpServer(){
		System.out.println("欢迎使用Web服务器，本服务器只支持静态网页。");
		System.out.println("检查配置文件及网页文件夹...");
		getConfig();
		start();
	}

	public static void main(String[] args){
		new HttpServer();
	}

	private void getConfig(){
		File fileCon = new File(Basic_Root,"config.ini");			//在项目路径下预创建 "config.ini"配置文件
		File fileDir = new File(Web_Root);							//在项目路径下预创建"webroot"文件夹
		File fileWeb = new File(Web_Root,"index.html");				//"webroot"下预创建"index.html"文件
		if(!fileCon.exists()){
			System.out.println("配置文件Config.ini损坏,重建中...");
			reBuildConfig(fileCon);
		}
		if(!fileDir.exists()){
			System.out.println("网页存放文件夹不存在，重建中...");
			fileDir.mkdir();
			System.out.println("完成！请在");
			System.out.println(Web_Root+"中放置网页文件...");
			System.out.println("Web服务器将重新初始化...");
			getConfig();
		}
		if(!fileWeb.exists()){
			reBuildWeb(fileWeb);
		}
		Properties pps = new Properties();					//创建Properties类对象，用于读取配置文件
		try{
			pps.load(new FileInputStream("config.ini"));	//读取"config.ini"文件
			Enumeration enumer = pps.propertyNames();		//得到配置文件的属性名集合
			
			String strKey = (String)enumer.nextElement();	//获取属性名：Web_Root
			String strValue = pps.getProperty(strKey);		//获取属性名对应的值
			if(strValue.equals("") != true){
				Web_Root = strValue;						//重新载入配置文件中的Web_Root
			}
			System.out.println("网页存放的路径为："+Web_Root);
				
			strKey = (String)enumer.nextElement();			//获取属性名：iPort
			strValue = pps.getProperty(strKey);				//获取属性名对应的端口号
			if(strValue.equals("") != true){
				iPort = Integer.parseInt(strValue);			//重新载入配置文件中的端口号
			}
			System.out.println("Web服务器访问端口为："+iPort);
			
			System.out.println("您可以修改Config.ini文件重新设定以上配置");

			System.out.println("启动检查完成，服务器初始化中...");									
		}
		catch(IOException ex){
			ex.printStackTrace();	//打印异常
		}
	}
	public void start(){
		System.out.println("Web服务器启动中...");
		ServerSocket serverSocket = null;
		runSocket(serverSocket);
	}
	private void runSocket(ServerSocket serverSocket)		//运行socket
	{
			try{
			serverSocket = new ServerSocket(iPort);	//绑定端口号
			System.out.println("Web启动完成！");
			System.out.println("您现在可以在客户端中访问http://localhost:"+iPort+"/，以测试服务器是否运行"); 
			while(true){                 
				Socket socket = null;                 
              
				System.out.println("等待连接...");                 
				socket = serverSocket.accept();
				
				System.out.println(socket.getInetAddress().toString()+"请求连接");  	//获取客户端IP地址 
				
                //创建一个新的线程
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
                
				System.out.println("服务器开始处理第"+(++count)+"次连接");
				System.out.println("当前客户端的数量："+count);
				/*
				input=socket.getInputStream();                 
				output=socket.getOutputStream();
				
				
				
				System.out.println("服务器开始处理第"+(++count)+"次连接");    
				
				//开始处理并分析请求信息                 
				Request request=new Request(input);                 
				request.parse();                
				
				//开始发送所请求的资源                 
				Response response=new Response(output);                 
				response.setRequest(request);                 
				response.sendStaticResource();                 
				//关闭连接                 
				socket.close();                 
				System.out.println("连接已关闭！"); 

				*/            
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("3");
		}
	}
	private void reBuildConfig(File file){         
	  	try{             
	  		file.createNewFile();             								//创建"config.ini"文件
	  		FileOutputStream fos=new FileOutputStream(file);             	
	  		PrintStream sp=new PrintStream(fos);             
	  		sp.println("Web_Root=");             
	  		sp.println("PORT=");             
	  		sp.close();             
	  		System.out.println("配置文件Config.ini创建成功，您可以修改WEB_ROOT的值改变网页文件的存放路径，修改PORT的值改变访问端口！");         
	  		}         
	  		catch (IOException ex){             
	  			ex.printStackTrace();             
	  			System.out.println("重建配置文件Config.ini失败");             
	  			System.out.println("服务器将使用默认配置...");         
	  		}     
	}

	private void reBuildWeb(File file)			//创建默认网页文件
	{         
		try{             
			file.createNewFile();             
			FileOutputStream fos=new FileOutputStream(file);             
			PrintStream sp=new PrintStream(fos);             
			sp.println("<html>");             
			sp.println("<head>");             
			sp.println("<title>Web Server</title>");             
			sp.println("</head>");             
			sp.println("<body>");             
			sp.println("<div align="+"center"+">Running successfully</div>");             
			sp.println("</body>");             
			sp.println("</html>");             
			sp.close();         
		}
		 catch(Exception ex){
		 	ex.printStackTrace();
		}
	}
}

