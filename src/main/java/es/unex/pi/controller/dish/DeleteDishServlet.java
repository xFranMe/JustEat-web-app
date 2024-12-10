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

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;

/**
 * Servlet implementation class DeleteDishServlet
 */
@WebServlet(urlPatterns = {"/DeleteDishServlet.do" })
public class DeleteDishServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDishServlet() {
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
					
					// Se despacha a DeleteDish.jsp
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/dish/DeleteDishConfirmation.jsp");		
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
		// Se recupera la conexión con la BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		
		// Se establece la conexión a través de DishDAO
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		
		// Se recupera el plato de la request
		Dish dish = dishDAO.get(Long.valueOf(request.getParameter("id")));
		
		// Se elimina el plato de la BD
		dishDAO.delete(dish.getId());
		
		// Se redirecciona al perfil del restaurante del plato
		response.sendRedirect("RestaurantProfileServlet.do?id="+dish.getIdr());
	}
}
