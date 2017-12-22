package proxy;

import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CheckProxy extends Window {
	private static final long serialVersionUID = 1L;

	public static void isChange() throws IOException {
		
		updateSSID();
		isOn();
		
		if(proxyOn) {
			trayIcon.setImage(getImage(ICONPATH_ON));
		}else {
			trayIcon.setImage(getImage(ICONPATH_OFF));
		}

		if(ssid_input.equals(ssid) && !proxyOn) {
			proxyOn();
		}else if(!ssid_input.equals(ssid) && proxyOn){
			proxyOff();
		}

	}
	
	private static void updateSSID() throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show interfaces");    
		builder.redirectErrorStream(true);
		Process p = builder.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = r.readLine()) != null) {
		    if (line.contains("    SSID                   : ")){
		    	ssid=line.replace("    SSID                   : ","");
		    	return;
		    }
		}
		ssid="";
	}
	
	public static void isOn() throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "REG QUERY \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable");    
		builder.redirectErrorStream(true);
		Process p = builder.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = r.readLine()) != null) {
		    if (line.contains("0x1")){
		    	proxyOn=true;
		    	return;
		    }
		}
    	proxyOn=false;
	}
	
	private static void proxyOn() throws IOException {
		Runtime.getRuntime().exec("cmd /c reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f >nul");
		Runtime.getRuntime().exec("cmd /c start ms-settings:network-proxy && taskkill /IM SystemSettings.exe /f >nul");
		System.out.println("ON");
		trayIcon.setImage(getImage(ICONPATH_ON));
		trayIcon.displayMessage(APPNAME, "Proxy state changed to ON.", TrayIcon.MessageType.NONE);
	}

	private static void proxyOff() throws IOException {
		Runtime.getRuntime().exec("cmd /c reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 0 /f >nul");
		Runtime.getRuntime().exec("cmd /c start ms-settings:network-proxy && taskkill /IM SystemSettings.exe /f >nul");
		System.out.println("OFF");
		trayIcon.setImage(getImage(ICONPATH_OFF));
		trayIcon.displayMessage(APPNAME, "Proxy state changed to OFF.", TrayIcon.MessageType.NONE);
	}

}
