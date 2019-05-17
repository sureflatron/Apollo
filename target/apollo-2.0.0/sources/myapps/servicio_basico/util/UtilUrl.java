package myapps.servicio_basico.util;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilUrl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static String getIp(String dir) throws Exception {		
		String response = "";
		URL url = new URL(dir);
		InetAddress inetAddress = InetAddress.getByName(url.getHost());
		String ip = inetAddress.getHostAddress();
		response = dir.replace(url.getHost(), ip);		
		return response;
	}

	public static synchronized String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = (String) request.getSession().getAttribute("TEMP$IP_CLIENT");			
		}
		return ip;
	}

	public static boolean esTextoValido(String valor, String expresionRegular) {
		Pattern pattern = Pattern.compile(expresionRegular);
		Matcher matcher = pattern.matcher(valor);
		return matcher.find();
	}

}
