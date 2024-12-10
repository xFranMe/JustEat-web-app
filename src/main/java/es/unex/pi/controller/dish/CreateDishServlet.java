package es.unex.pi.controller.dish;

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
import java.util.HashMap;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;

/**
 * Servlet implementation class CreateDishServlet
 */
@WebServlet(urlPatterns = {"/CreateDishServlet.do" })
public class CreateDishServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateDishServlet() {
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
				// Se añade el ID del restaurante a la request para tenerlo después en el formulario y recogerlo en el doPost
				request.setAttribute("idr", idr);
				
				// Se despacha a EditDish.jsp
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/dish/EditDish.jsp");		
				view.forward(request, response);
			} else {
				// No existe restaurante o no pertenece al usuario
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
		// Se obtiene el ID del restaurante de la request
		Long idr = Long.valueOf(request.getParameter("idr"));
		
		// Se recuperan los datos introducidos por el usuario para crear un nuevo plato
		Dish dish = new Dish();
		dish.setName(request.getParameter("name"));
		dish.setDescription(request.getParameter("description"));
		dish.setPrice(Float.parseFloat(request.getParameter("price")));
		dish.setIdr(idr);
		
		// Se establece la conexión con la BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
			
		if (dishDAO.getByNameAndRestaurantId(dish.getName(), dish.getIdr()) == null) {
			// Se almacena el nuevo plato en la BD
			dishDAO.add(dish);
			// Se redirecciona hacia el detalle del restaurante
			response.sendRedirect("RestaurantProfileServlet.do?id="+idr);
		} else {
			Map<String, String> messages = new HashMap<String, String>();
			// Se añaden los errores a la request
			messages.put("error", "El nombre para el plato no está disponible.");
			request.setAttribute("messages", messages);
			// Se añade el plato a la request para que el usuario no tenga que volver a introducir toda la información
			request.setAttribute("dish", dish);
			// Se añade de nuevo el ID del restaurante a la request ya que aún no se ha creado el plato
			request.setAttribute("idr", idr);
			// Se despacha la request a EditDish.jsp
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/dish/EditDish.jsp");
			view.forward(request,response);
		}
	}
}
