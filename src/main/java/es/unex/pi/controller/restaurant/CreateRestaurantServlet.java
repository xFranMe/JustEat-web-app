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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCRestaurantCategoriesDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantCategoriesDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;

/**
 * Servlet implementation class CreateRestaurantServlet
 */
@WebServlet(urlPatterns = {"/CreateRestaurantServlet.do"})
public class CreateRestaurantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateRestaurantServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se debe pasar con la request las categorías que pueden ser asignadas al restaurante
		ServletContext servletContext = getServletContext();
		Connection connection = (Connection) servletContext.getAttribute("dbConn");
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(connection);
		
		// Se obtienen las categorías principales
		List<Category> categoryList = new ArrayList<>();
		categoryList = categoryDAO.getAll();
		
		// Se almacena en la request la lista de categorías obtenida
		request.setAttribute("categoryList", categoryList);
		
		// Se despacha la respuesta a EditRestaurant.jsp
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/EditRestaurant.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recuperan los datos introducidos en el formulario
		Restaurant restaurant = new Restaurant();
		restaurant.setName(request.getParameter("name"));
		restaurant.setAddress(request.getParameter("address"));
		restaurant.setTelephone(request.getParameter("telephone"));
		restaurant.setCity(request.getParameter("city"));
		restaurant.setContactEmail(request.getParameter("contactEmail"));
		restaurant.setDescription(request.getParameter("description"));
		// Hay que convertir de String a Float ya que getParameter lo devuelvo todo como String
		restaurant.setMinPrice(Float.parseFloat(request.getParameter("minPrice")));
		restaurant.setMaxPrice(Float.parseFloat(request.getParameter("maxPrice")));
		// Lo mismo pero de String a int
		restaurant.setBikeFriendly(Integer.valueOf(request.getParameter("bikeFriendly")));
		restaurant.setAvailable(Integer.valueOf(request.getParameter("available")));
		
		// Se obtiene el ID del usuario que crea el restaurante (el de la sesión) y se añade al objeto
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		restaurant.setIdu(user.getId());
		
		// Se obtiene la lista de categorías seleccionadas por el usuario -> Si no se ha seleccionado ninguna categoría se debe devolver un error
		Map<Long, Long> restaurantCategoriesMap = new HashMap<Long, Long>();
		String[] categoriesChecked = request.getParameterValues("categories");
		if(categoriesChecked != null) {
			for (String categoryId : categoriesChecked) {
				restaurantCategoriesMap.put(Long.parseLong(categoryId), Long.parseLong(categoryId));
			}
		}
		
		// Se recupera la conexión con la BD para futuras comprobaciones
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		
		// Bandera empleada para determinar si hay que despachar la request con los errores almacenados en messages
		boolean dispatch_error = false;
		Map<String, String> messages = new HashMap<String, String>();
		
		// Se comprueba la validez de los datos introducidos
		if (!restaurant.validate(messages, restaurantCategoriesMap)) {
			// Hay errores en los datos introducidos. Se despacha junto a los errores.
			dispatch_error = true;
		} else { 
			// Datos válidos
			// Se comprueba si ya existe un restaurante con el mismo nombre
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(conn);						
			if(restaurantDAO.get(restaurant.getName()) != null) {
				// Ya hay un restaurante con el mismo nombre. Se añade un mensaje de error y se despacha
				messages.put("name_already_exists", "Ya existe un restaurante con el mismo nombre.");	
				dispatch_error = true;
			} else if(restaurantDAO.getByEmail(restaurant.getContactEmail()) != null) { // Se comprueba si el email se repite
					// Ya hay un restaurante con el mismo email. Se añade un mensaje de error y se despacha
					messages.put("email_already_exists", "Ya existe un restaurante con el mismo email.");
					dispatch_error = true;
			} else { // Los datos son válidos y no existe un restaurante con el mismo nombre o email
					// Se almacena el nuevo restaurante en la base de datos
					long newId = restaurantDAO.add(restaurant);
					// Se almacenan las categorías del restaurante en la base de datos
					RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
					restaurantCategoriesDAO.setConnection(conn);
					restaurantCategoriesDAO.addAll(restaurantCategoriesMap, newId); // Se obtiene el ID (para el segundo parámetro) del restaurante que se acaba de insertar
					// Se redirecciona al servlet SearchAndCategories.do
					response.sendRedirect("RestaurantProfileServlet.do?id="+newId);
			}
		}
		
		if(dispatch_error) {
			// Se almacenan los errores en la request
			request.setAttribute("messages", messages);
			// Se mantiene la información introducida para no hacer que el usuario vuelva a escribirlo todo
			request.setAttribute("restaurant", restaurant);
			// Se pasan las categorías existentes
			CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
			categoryDAO.setConnection(conn);
			List<Category> categoryList = new ArrayList<>();
			categoryList = categoryDAO.getAll();
			request.setAttribute("categoryList", categoryList);
			// Se pasan las categorías que había seleccionado el usuario
			request.setAttribute("restaurantCategoriesMap", restaurantCategoriesMap);
			// Se despacha a EditRestaurant.jsp
			System.out.println(restaurant.getId());
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/EditRestaurant.jsp");
			view.forward(request,response);
		}
	}
}
