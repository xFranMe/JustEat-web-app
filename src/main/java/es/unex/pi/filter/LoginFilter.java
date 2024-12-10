package es.unex.pi.filter;

import java.io.IOException;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }, urlPatterns = {"*.do"})
public class LoginFilter implements Filter {
	    
	/**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
					
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession(true);
		
		// Se obtiene el path del servlet al que se dirige la petición: queremos que únicamente LoginServlet y SignUpServlet no sean capturados por el filtro
		String path = ((HttpServletRequest) req).getServletPath();
		
		// Solo se redireccionará a LoginServlet si no existe sesión (si es null) y si el servlet al que se accede no es LoginServlet ni SignUpServlet
		if(!(path.contains("LoginServlet") || path.contains("SignUpServlet")) && session.getAttribute("user") == null) {
			res.sendRedirect(req.getContextPath() + "/LoginServlet.do");
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}




