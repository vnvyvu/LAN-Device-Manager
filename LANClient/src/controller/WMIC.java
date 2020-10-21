/*
 * Created by VyVu
 * 
 *  @author vutrivi99@gmail.com
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

// TODO: Auto-generated Javadoc
/**
 * WMIC wrapped tool.
 */
public class WMIC {
	
	/**
	 * Get 1 query result.
	 *
	 * @param alias the alias
	 * @param column the column
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String get(String alias, String column) throws IOException {
		String res=null;
		BufferedReader read=new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("wmic.exe "+alias+" get "+column).getInputStream()));
		read.readLine();
		read.readLine();
		res=read.readLine();
		read.close();
		return res.trim();
	}
	
	/**
	 * Get multiple query result.
	 *
	 * @param alias the alias
	 * @param column the column
	 * @return the lines
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Stream<String> getLines(String alias, String column) throws IOException {
		return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("wmic.exe "+alias+" get "+column).getInputStream())).lines();
	}
	
	/**
	 * Get multiple query result with condition.
	 *
	 * @param alias the alias
	 * @param condition the condition
	 * @param column the column
	 * @return the lines
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Stream<String> getLines(String alias, String condition, String column) throws IOException{
		return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("wmic.exe "+alias+" where \""+condition+"\" get "+column).getInputStream())).lines();
	}
}
