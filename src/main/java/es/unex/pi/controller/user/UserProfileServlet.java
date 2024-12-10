package es.unex.pi.controller.user;

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
import java.util.ArrayList;
import java.util.List;

import es.unex.pi.dao.JDBCOrderDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.OrderDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Order;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet(urlPatterns = {"/UserProfileServlet.do"})
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando el usuario es redireccionado para ver su perfil.
	 * Despacha la request a UserProfile.jsp para mostrar la información del perfil y sus restaurantes.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recupera y establece la conexión con la BD
		ServletContext servletContext = getServletContext();
		Connection connection = (Connection) servletContext.getAttribute("dbConn");
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(connection);
		
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Se recuperan los restaurantes del usuario de la sesión de la BD
		List<Restaurant> userRestaurants = new ArrayList<Restaurant>();
		userRestaurants = restaurantDAO.getAllByUser(user.getId());
		
		// Se almacenan los restaurantes en la request
		request.setAttribute("restaurantList", userRestaurants);
		
		// Se recuperan los pedidos realizados por el usuario
		OrderDAO orderDAO = new JDBCOrderDAOImpl();
		orderDAO.setConnection(connection);
		List<Order> orderList = new ArrayList<>();
		orderList = orderDAO.getAllFromUser(user.getId());
		
		// Se almacenan los pedidos en la request
		request.setAttribute("orderList", orderList);
		
		// Se despacha UserProfile.jsp
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/UserProfile.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Este método no requiere de funcionalidad más allá de redireccionar al método doGet().
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Simplemente se invoca el método doGet() del mismo servlet, ya que no se necesita una funcionalidad concreta para el doPost()
		doGet(request, response);
	}
}
