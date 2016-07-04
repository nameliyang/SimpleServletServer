package com.ly.servlet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;

public class ServletProcessor {
	
	@SuppressWarnings("all")
	public void process(Request request,Response response){
		String uri = request.getUri();
		String servletName = uri.substring(uri.lastIndexOf("/")+1);
		URLClassLoader loader = null;
		
		URL[] urls = new URL[1];
		URLStreamHandler streamHandler = null;
		File classPath = new File(Constants.WEB_ROOT);
		
		try {
			String repository = 
					(new URL("file", null, classPath.getCanonicalPath()+File.separator)).toString();
			urls[0] = new URL(null,repository,streamHandler);
			System.out.println("url---->" + urls[0]);
			
			loader = new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Class myClass = null;
		try {
			myClass = loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Servlet servlet = null;
		
		try{
			servlet = (Servlet)myClass.newInstance();
			servlet.service(request, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
