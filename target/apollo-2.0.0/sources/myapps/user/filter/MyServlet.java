package myapps.user.filter;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;
import org.primefaces.util.Base64;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
    private IApplicationContext beanApplicationContext;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());	
		// Set response content type
		// response.setContentType("text/html");
		//response.setHeader("Access-Control-Allow-Origin", Parametros.dominioAccessOrigin);
		response.setHeader("Access-Control-Allow-Origin",
                beanApplicationContext.getFormParameters().get(EnumParametros.DOMINIO_ACCESS_ORIGIN.toString()));

		response.setHeader("X-XSS-Protection", "1; mode=block");
		response.setHeader("X-CONTENT-TYPE-OPTIONS", "nosniff");
		response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
		if (request.getScheme() != null && request.getScheme().toLowerCase().trim().equals("https")) {
				response.setHeader("Content-Security-Policy", "script-src " +"'unsafe-inline' " + beanApplicationContext.getFormParameters
																								().get(EnumParametros.DOMINIO_ACCESS_ORIGIN.toString()));
		}
//		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
		String result=request.getRemoteAddr();
		 byte[] utf8 = result.getBytes("UTF8");

		result = Base64.encodeToString(utf8, false);
		//log.info("Ip desencriptada por cargar ip: " + ipClient);	

		out.println(result);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
