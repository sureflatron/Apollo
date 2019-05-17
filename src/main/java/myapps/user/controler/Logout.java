package myapps.user.controler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*
 import micrium.sienc.bl.groupUser.LogWebBL;
 import micrium.sienc.filter.ControlTimeOut;
 import micrium.sienc.util.GuiceInjectorSingleton;
 import micrium.sienc.vo.groupUser.LogWeb;//*/


public class Logout extends HttpServlet {


	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			// String strIdUs = (String) request.getSession().getAttribute("TEMP$USER_NAME");
			// String address = request.getRemoteAddr();
			// setBitacora(strIdUs, address);
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet NewServlet</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
			out.println("</body>");
			out.println("</html>");

			HttpSession sesion = request.getSession();
			sesion.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
			sesion.setAttribute("TEMP$USER_NAME", "");
			sesion.setAttribute("TEMP$GROUP", "");
			sesion.setAttribute("TEMP$IP_CLIENT", "");
			sesion.invalidate();
			response.sendRedirect(request.getContextPath());
		} finally {
			out.close();
		}
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
