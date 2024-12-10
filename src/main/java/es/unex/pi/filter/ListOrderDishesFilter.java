//package es.unex.pi.filter;
//
//import jakarta.servlet.DispatcherType;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//import java.sql.Connection;
//
//import es.unex.pi.dao.JDBCOrderDAOImpl;
//import es.unex.pi.dao.OrderDAO;
//import es.unex.pi.model.Order;
//import es.unex.pi.model.User;
//
///**
// * Servlet Filter implementation class ListOrderDishesFilter
// */
////@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }, urlPatterns = {"/ListOrderDishesServlet.do"})
//public class ListOrderDishesFilter extends HttpFilter implements Filter {
//       
//	private static final long serialVersionUID = 1L;
//
//	/**
//     * @see HttpFilter#HttpFilter()
//     */
//    public ListOrderDishesFilter() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see Filter#destroy()
//	 */
//	public void destroy() {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
//	 */
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//		
//		try {
//			// Se obtiene el ID del pedido que se quiere consultar
//			Long ido = Long.parseLong(request.getParameter("id"));    
//			
//			// Se recupera la conexión con la base de datos
//			ServletContext servletContext = request.getServletContext();
//			Connection conn = (Connection) servletContext.getAttribute("dbConn");
//			
//			// Se obtiene el pedido de la BD
//			OrderDAO orderDAO = new JDBCOrderDAOImpl();
//			orderDAO.setConnection(conn);
//			Order order = orderDAO.get(ido);
//			
//			if(order != null) {
//				// Se obtiene el usuario de la sesión
//				HttpSession session = req.getSession(true);
//				User user = (User) session.getAttribute("user");
//				// Si el ID de usuario del pedido y el ID del usuario logeado coinciden, se continúa con la request
//				if(order.getIdu() == user.getId()) {
//					// pass the request along the filter chain
//					chain.doFilter(request, response);
//				} else {
//					res.sendRedirect(req.getContextPath() + "/SearchAndCategoriesServlet.do");
//				}
//			} else {
//				// No existe pedido con el ID pasado
//				res.sendRedirect(req.getContextPath() + "/SearchAndCategoriesServlet.do");
//			}
//		}
//		catch (NumberFormatException e) {
//			// No se ha intoducido un ID numérico
//			res.sendRedirect(req.getContextPath() + "/SearchAndCategoriesServlet.do");
//		}
//	}
//
//	/**
//	 * @see Filter#init(FilterConfig)
//	 */
//	public void init(FilterConfig fConfig) throws ServletException {
//		// TODO Auto-generated method stub
//	}
//
//}
