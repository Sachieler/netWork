package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	//和本线程相关的Socket
	Socket socket = null;
	public ServerThread(Socket socket){
		this.socket = socket;
	}

	//线程运行，响应cilent的请求
	public void run(){
		InputStream input = null;
		OutputStream output = null;
		try{
			input = socket.getInputStream();
			output = socket.getOutputStream();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		Request request = new Request(input);
		request.parse();
		
		Response reponse = new Response(output);
		reponse.setRequest(request);
		try {
			reponse.sendStaticResource();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("连接已关闭! ");
	}
}
