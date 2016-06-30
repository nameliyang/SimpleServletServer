package com.ly.httprequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

public class SimpleHttpRequest {
   static final 	CountDownLatch latch = new CountDownLatch(1);
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		final Socket socket = new Socket("www.qianqu.cc", 80);
		OutputStream outputStream = socket.getOutputStream();
		
		outputStream.write("GET /hot HTTP/1.1\r\n".getBytes());
		outputStream.write("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n".getBytes());
		outputStream.write("Accept-Encoding:deflate\r\n".getBytes());
		outputStream.write("Accept-Language:zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3\r\n".getBytes());
		outputStream.write("Cache-Control:max-age=0\r\n".getBytes());
		outputStream.write("Connection:keep-alive\r\n".getBytes());
		outputStream.write("Host:www.qianqu.cc\r\n".getBytes());
		outputStream.write("User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0\r\n".getBytes());
		outputStream.write("\r\n".getBytes());
		
		
		new Thread(new Runnable() {
			public void run() {
				try {
					doReadInputStream(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		System.out.println("awiat start ");
		latch.await();
		System.out.println("awiat end ");
		socket.close();
		
	}
	
	public static  void doReadInputStream(Socket socket) throws IOException{
		InputStream inputStream = socket.getInputStream();
		byte[] buffer  = new byte[1024];
		int len = 0;
		long count = 0;
		while((len=inputStream.read(buffer))!=-1){
			System.out.print(new String(buffer,0,len,"UTF-8"));
			count+=len;
		}
		System.out.println("read count : "+count);
		latch.countDown();
	}
}
