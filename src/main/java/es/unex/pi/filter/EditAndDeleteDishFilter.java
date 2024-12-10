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
//import es.unex.pi.dao.DishDAO;
//import es.unex.pi.dao.JDBCDishDAOImpl;
//import es.unex.pi.dao.JDBCRestaurantDAOImpl;
//import es.unex.pi.dao.RestaurantDAO;
//import es.unex.pi.model.Dish;
//import es.unex.pi.model.Restaurant;
//import es.unex.pi.model.User;
//
///**
// * Servlet Filter implementation class DishFilter
// * CreateDishServlet, DeleteDishServlet y EditDishServlet
// */
////@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }, urlPatterns = {"/EditDishServlet.do", "/DeleteDishServlet.do"})
//public class EditAndDeleteDishFilter extends HttpFilter implements Filter {
//       
//	private static final long serialVersionUID = 1L;
//
//	/**
//     * @see HttpFilter#HttpFilter()
//     */
//    public EditAndDeleteDishFilter() {
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
//			// Se obtiene el ID del plato que se quiere Editar/Eliminar
//			Long idd = Long.parseLong(request.getParameter("id"));    
//			
//			// Se recupera la conexión con la base de datos
//			ServletContext servletContext = request.getServletContext();
//			Connection conn = (Connection) servletContext.getAttribute("dbConn");
//			DishDAO dishDAO = new JDBCDishDAOImpl();
//			dishDAO.setConnection(conn);
//			
//			// Se obtiene el plato de la BD
//			Dish dish = dishDAO.get(idd);
//			
//			if(dish != null) {
//				// Se obtiene de la BD el restaurante al que pertenece el plato
//				RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
//				restaurantDAO.setConnection(conn);
//				Restaurant restaurant = restaurantDAO.get(dish.getIdr());
//				// Se obtiene el usuario de la sesión
//				HttpSession session = req.getSession(true);
//				User user = (User) session.getAttribute("user");
//				// Si el ID de usuario del restaurante y el ID del usuario logeado coinciden, se continúa con la request
//				if(restaurant != null && restaurant.getIdu() == user.getId()) {
//					// pass the request along the filter chain
//					chain.doFilter(request, response);
//				} else {
//					res.sendRedirect(req.getContextPath() + "/SearchAndCategoriesServlet.do");
//				}
//				
//			} else {
//				// No existe el plato con el ID pasado
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
