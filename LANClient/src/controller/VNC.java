package controller;

import java.io.File;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * VNC wrapped tool.
 */
public class VNC {
	
	/**
	 * Install.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void install() throws IOException {
		Runtime.getRuntime().exec("msiexec /i tightvnc.msi /quiet /norestart ADDLOCAL=Server SERVER_REGISTER_AS_SERVICE=1 SERVER_ADD_FIREWALL_EXCEPTION=1 SET_ALLOWLOOPBACK=1 VALUE_OF_ALLOWLOOPBACK=1 SET_USEVNCAUTHENTICATION=1 VALUE_OF_USEVNCAUTHENTICATION=1 SET_PASSWORD=1 VALUE_OF_PASSWORD=\"LDMpsw@1501\" SET_USECONTROLAUTHENTICATION=1 VALUE_OF_USECONTROLAUTHENTICATION=1 SET_CONTROLPASSWORD=1 VALUE_OF_CONTROLPASSWORD=\"LDMpsw@1501\"");
	}
	
	/**
	 * Checks if VNC is installed.
	 *
	 * @return true, if is installed
	 */
	public static boolean isInstalled() {
		File f=new File("C:\\Program Files (x86)\\TightVNC\\tvnserver.exe");
		if(!f.exists()) {
			f=new File("C:\\Program Files\\TightVNC\\tvnserver.exe");
			if(!f.exists()) return false;
			else return true;
		} else return true;
	}
}
