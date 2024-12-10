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
//import es.unex.pi.dao.JDBCRestaurantDAOImpl;
//import es.unex.pi.dao.RestaurantDAO;
//import es.unex.pi.model.Restaurant;
//import es.unex.pi.model.User;
//
///**
// * Servlet Filter implementation class RestaurantFilter
// * DeleteRestaurantServlet y EditRestaurantServlet
// */
//@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }, urlPatterns = {"/EditRestaurantServlet.do", "/DeleteRestaurantServlet.do", "/CreateDishServlet.do"})
//public class RestaurantFilter extends HttpFilter implements Filter {
//       
//	private static final long serialVersionUID = 1L;
//
//	/**
//     * @see HttpFilter#HttpFilter()
//     */
//    public RestaurantFilter() {
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
//			// Se obtiene el ID del restaurante que se quiere Editar/Eliminar o en el que se quiere crear un plato
//			Long idr = Long.parseLong(request.getParameter("id"));    
//			
//			// Se recupera la conexión con la base de datos
//			ServletContext servletContext = request.getServletContext();
//			Connection conn = (Connection) servletContext.getAttribute("dbConn");
//			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
//			restaurantDAO.setConnection(conn);
//			
//			// Se obtiene el restaurante de la BD
//			Restaurant restaurant = restaurantDAO.get(idr);
//			
//			if(restaurant != null) {
//				// Se obtiene el usuario de la sesión
//				HttpSession session = req.getSession(true);
//				User user = (User) session.getAttribute("user");
//				// Si el ID de usuario del restaurante y el ID del usuario logeado coinciden, se continúa con la request
//				if(restaurant.getIdu() == user.getId()) {
//					// pass the request along the filter chain
//					chain.doFilter(request, response);
//				} else {
//					res.sendRedirect(req.getContextPath() + "/UserProfileServlet.do");
//				}
//			} else {
//				// No existe pedido con el ID pasado
//				res.sendRedirect(req.getContextPath() + "/UserProfileServlet.do");
//			}
//		}
//		catch (NumberFormatException e) {
//			// No se ha intoducido un ID numérico
//			res.sendRedirect(req.getContextPath() + "/UserProfileServlet.do");
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
