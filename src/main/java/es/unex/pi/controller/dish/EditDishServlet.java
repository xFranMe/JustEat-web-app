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
 * Servlet implementation class EditDishServlet
 */
@WebServlet(urlPatterns = {"/EditDishServlet.do"})
public class EditDishServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditDishServlet() {
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
			// Se obtiene el ID del plato de la request
			Long idd = Long.valueOf(request.getParameter("id"));
			
			// Se obtiene el plato de la BD
			DishDAO dishDAO = new JDBCDishDAOImpl();
			dishDAO.setConnection(conn);
			Dish dish = dishDAO.get(idd);
			
			if(dish != null) {
				// Se obtiene de la BD el restaurante al que pertenece el plato
				RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
				restaurantDAO.setConnection(conn);
				Restaurant restaurant = restaurantDAO.get(dish.getIdr());
				
				// Se obtiene el usuario de la sesión
				HttpSession session = request.getSession(true);
				User user = (User) session.getAttribute("user");
				
				if(restaurant != null && restaurant.getIdu() == user.getId()) {
					// Se añade el plato a la request para precargar su información
					request.setAttribute("dish", dish);			
					
					// Se despacha a EditDish.jsp
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/dish/EditDish.jsp");		
					view.forward(request, response);
				} else {
					// No existe restaurante o no pertenece al usuario
					response.sendRedirect("UserProfileServlet.do");				
				}
			} else {
				// No existe plato con el ID pasado
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
		// Se recuperan los datos introducidos en el formulario
		Dish editedDish = new Dish();
		editedDish.setName(request.getParameter("name"));
		editedDish.setDescription(request.getParameter("description"));
		editedDish.setPrice(Float.parseFloat(request.getParameter("price")));
		
		// Se establece la conexión con la BD a través de DishDAO
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		
		// Se recupera la información del plato original que ha sido editado
		Long idd = Long.parseLong(request.getParameter("idd"));
		Dish originalDish = dishDAO.get(idd);
		// Se añaden al plato editado el ID del plato original y de su restaurante
		editedDish.setId(originalDish.getId());
		editedDish.setIdr(originalDish.getIdr());
		
		if (dishDAO.getByNameAndRestaurantId(editedDish.getName(), editedDish.getIdr()) == null || editedDish.getName().equals(originalDish.getName())) {
			// No existe un plato con el mismo nombre, por lo que se puede actualizar en la BD
			dishDAO.update(editedDish);
			// Se redirige hacia el detalle del restaurante
			response.sendRedirect("RestaurantProfileServlet.do?id="+editedDish.getIdr());
		} else {
			// Existe ya un plato con el mismo nombre, por lo que se informa del error
			Map<String, String> messages = new HashMap<String, String>();
			messages.put("error", "El nuevo nombre para el plato no se encuentra disponible.");
			request.setAttribute("messages", messages);
			// Se añade a la request la información del plato editado para que el usuario no tenga que introducir toda la información de nuevo
			request.setAttribute("dish", editedDish);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/dish/EditDish.jsp");
			view.forward(request,response);
		}
	}
}
