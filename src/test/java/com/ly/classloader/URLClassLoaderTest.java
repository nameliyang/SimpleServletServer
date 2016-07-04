package com.ly.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;



public class URLClassLoaderTest {
	
	public static void main(String[] args) throws Exception {
		
		
		String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(File.pathSeparator);
		
		for(String path:classpathEntries){
			System.out.println("classpath:"+path);
		}
		
		//Person.class.getClassLoader().getResource("");
		
		
		URL url = new URL("file", null, "E:/dream/code/SimpleServletServer/webroot");
		
		System.out.println(url);
		URLClassLoader loader = new URLClassLoader(new URL[]{url});
		
		Class<?> loadClass = loader.loadClass("com.ly.classloader.Person");
		
		IPerson person = (IPerson) loadClass.newInstance();
		
		
		System.out.println(IPerson.class.getClassLoader().getClass().getName());
		System.out.println(loadClass.getClassLoader().getClass().getName());
		
		
		person.doSomething();
	}
}

interface IPerson{
	
	public void doSomething();
	
}