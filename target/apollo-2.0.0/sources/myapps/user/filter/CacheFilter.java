package myapps.user.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class TestFilter
 */
@WebFilter(filterName = "CacheFilter", urlPatterns = { "/*" })
public class CacheFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public CacheFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// httpResponse.setHeader("x-ua-compatible", "IE=8");
		httpResponse.setHeader("X-UA-Compatible", "IE=edge");
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains");
		// httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; connect-src 'self'; img-src 'self'; style-src 'self';");
		if (request.getScheme() != null && request.getScheme().toLowerCase().trim().equals("https")) {
//			httpResponse.setHeader("Content-Security-Policy", "default-src https:; connect-src https:; font-src https: data:; frame-src https:; frame-ancestors https:; img-src https: data:; media-src https:; object-src https:; script-src 'unsafe-inline' 'unsafe-eval' https:; style-src 'unsafe-inline' https:;");
			httpResponse.setHeader("Content-Security-Policy", "default-src https:; connect-src https:; font-src https: data:; frame-src https:; frame-ancestors https:; img-src https: data:; media-src https:; object-src https:; script-src 'unsafe-inline' 'unsafe-eval' https:; style-src 'unsafe-inline' https:;");
		}
		httpResponse.setHeader("X-Content-Type-Options", "nosniff");
//		httpResponse.setDateHeader("X-XSS-Protection", 0);
		httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
		httpResponse.setDateHeader("Expires", 0);
		httpResponse.setHeader("X-Frame-Options", "DENY");

		chain.doFilter(request, response);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
