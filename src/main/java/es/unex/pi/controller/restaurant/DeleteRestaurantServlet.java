package es.unex.pi.controller.restaurant;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;

/**
 * Servlet implementation class DeleteRestaurantServlet
 */
@WebServlet(urlPatterns = {"/DeleteRestaurantServlet.do"})
public class DeleteRestaurantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteRestaurantServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Se recupera la conexión con la base de datos
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		
		try {
			// Se obtiene el ID del restaurante de la request
			Long idr = Long.valueOf(request.getParameter("id"));
			
			// Se obtiene el restaurante de la BD
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(conn);
			Restaurant restaurant = restaurantDAO.get(idr);
			
			// Se obtiene el usuario de la sesión
			HttpSession session = request.getSession(true);
			User user = (User) session.getAttribute("user");
			
			if(restaurant != null && restaurant.getIdu() == user.getId()) {
				request.setAttribute("restaurant", restaurantDAO.get(idr));
				
				// Se despacha la request
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/DeleteRestaurantConfirmation.jsp");
				view.forward(request, response);
			} else {
				// No existe restaurante con el ID pasado o no pertenece al usuario
				response.sendRedirect("UserProfileServlet.do");
			}
		}
		catch (NumberFormatException e) {
			// No se ha intoducido un ID numérico
			response.sendRedirect("UserProfileServlet.do");
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recupera la conexión con la base de datos
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		
		// Se establece la conexión a través de RestaurantDAO
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		
		// Se elimina el restaurante recuperando el ID de la request
		restaurantDAO.delete(Long.parseLong(request.getParameter("id")));
		
		// Se redirecciona al perfil de usuario
		response.sendRedirect("UserProfileServlet.do");
	}

}
