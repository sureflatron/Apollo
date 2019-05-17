package myapps.user.filter;

import myapps.servicio_basico.commons.EnumParametros;
import myapps.servicio_basico.commons.IApplicationContext;

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
@WebServlet("/Certificado")
public class Certificado extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	private IApplicationContext beanApplicationContext;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Certificado() {
		super();		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		// Set response content type
		// response.setContentType("text/html");
		
		response.setHeader("Access-Control-Allow-Origin",
				beanApplicationContext.getFormParameters().get(EnumParametros
						.DOMINIO_ACCESS_ORIGIN.toString()));
		response.setHeader("X-XSS-Protection", "1; mode=block");
		response.setHeader("X-CONTENT-TYPE-OPTIONS", "nosniff");
		response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
		if (request.getScheme() != null && request.getScheme().toLowerCase().trim().equals("https")) {
		response.setHeader("Content-Security-Policy", "script-src " +
				"'unsafe-inline' " + beanApplicationContext.getFormParameters
				().get(EnumParametros.DOMINIO_ACCESS_ORIGIN.toString()));
		}
		
		// response.setHeader("Access-Control-Allow-Headers", "*");
		// response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method,
		// Access-Control-Request-Headers");

		response.setContentType("text/html;charset=UTF-8");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<html><head><style>.outer-container {position: absolute;display: table;width: 100%;height: 95%;background: none;}.inner-container {display: table-cell;vertical-align: middle;text-align: center;}");
		out.println(".centered-content {display: inline-block;background: none;padding: 10px;border: 0px solid none;color: red;}</style><head>");
		out.println("<body style='background-color: rgb(0, 46, 110);' onload='goBack()'><script>function goBack() { window.history.back();} </script><div class='outer-container'>");
		out.println("<div class='inner-container'><div class='centered-content'><a href='#' onclick='goBack();' style='color: white;'/>Go Back</a></div></div></div>");
		out.println("</body></html>");
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
