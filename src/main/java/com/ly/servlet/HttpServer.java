package com.ly.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
// telnet 127.0.0.1 8080
public class HttpServer {
	/**
	 * WEB_ROOT is the directory where our HTML and other files reside. For this
	 * package, WEB_ROOT is the "webroot" directory under the working directory.
	 * The working directory is the location in the file system from where the
	 * java command was invoked.
	 */
	public static final String WEB_ROOT = System.getProperty("user.dir")
			+ File.separator + "webroot";
	
	// shutdown command
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	// the shutdown command received
	private boolean shutdown = false;

	public static void main(String[] args) throws IOException {
		
//		final ServerSocket serverSocket = new ServerSocket(8080,1);
//		while(true){
//			final Socket socket = serverSocket.accept();
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						String date = new Date().toLocaleString();
//						OutputStream outputStream = socket.getOutputStream();
//						for(int i = 0;i<date.length();i++){
//							Thread.sleep(1000);
//							outputStream.write(date.substring(i,i+1).getBytes());
//						}
//						outputStream.close();
//						socket.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//			
//		}
		HttpServer server = new HttpServer();
		server.await();
	}
	public void await() {
		ServerSocket serverSocket = null;
		int port = 8080;
		try {
			serverSocket = new ServerSocket(port, 1,
					InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Loop waiting for a request
		while (!shutdown) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// create Request object and parse
				Request request = new Request(input);
				request.parse();

				// create Response object
				Response response = new Response(output);
				response.setRequest(request);
				
				if(request.getUri().startsWith("/servlet/")){
					ServletProcessor processor = new ServletProcessor();
					processor.process(request, response);
				}else{
					StaticResourceProcessor processor = 
							new StaticResourceProcessor();
					processor.process(request, response);
				}
				
				
				//response.sendStaticResource();

				// Close the socket
				socket.close();

				// check if the previous URI is a shutdown command
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
