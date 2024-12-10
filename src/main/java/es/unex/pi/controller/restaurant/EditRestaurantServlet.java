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
import es.unex.pi.model.RestaurantCategories;
import es.unex.pi.model.User;

/**
 * Servlet implementation class EditRestaurantServlet
 */
@WebServlet(urlPatterns = {"/EditRestaurantServlet.do"})
public class EditRestaurantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRestaurantServlet() {
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
				request.setAttribute("restaurant", restaurant);
				
				// Se obtiene todas las categorías existentes
				CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
				categoryDAO.setConnection(conn);
				List<Category> categoryList = new ArrayList<>();
				categoryList = categoryDAO.getAll();
				request.setAttribute("categoryList", categoryList);
				
				// Se obtiene las categorías del restaurante
				RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
				restaurantCategoriesDAO.setConnection(conn);
				List<RestaurantCategories> restaurantCategoriesList = new ArrayList<>();
				restaurantCategoriesList = restaurantCategoriesDAO.getAllByRestaurant(idr);
				Map<Long, Long> restaurantCategoriesMap = new HashMap<Long, Long>();
				if(restaurantCategoriesList != null) {
					for (RestaurantCategories restaurantCategory : restaurantCategoriesList) {
						restaurantCategoriesMap.put(restaurantCategory.getIdct(), restaurantCategory.getIdct());
					}
				}
				request.setAttribute("restaurantCategoriesMap", restaurantCategoriesMap);
				
				// Se despacha la request con toda la información del restaurante, sus categorías aplicadas y el resto de categorías
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/EditRestaurant.jsp");
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
		// Se recuperan los datos introducidos en el formulario
		Restaurant restaurant = new Restaurant();
		restaurant.setId(Long.parseLong(request.getParameter("id")));
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
		
		// Se obtiene el ID del usuario que crea el restaurante y se añade al objeto
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
		
		// Bandera empleada para despachar la request con los errores pertinentes
		boolean dispatch_error = false;
		
		// Se comprueba la validez de los datos introducidos
		Map<String, String> messages = new HashMap<String, String>();
		if (!restaurant.validate(messages, restaurantCategoriesMap)) {
			// Hay errores en los datos introducidos. Se despacha junto a los errores.
			dispatch_error = true;
		} else { 
			// Datos válidos
			// Se comprueba si ya existe un restaurante con el mismo nombre o email y que no sea él mismo
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(conn);
			Restaurant restaurantSameName = new Restaurant();
			restaurantSameName = restaurantDAO.get(restaurant.getName());
			
			Restaurant restaurantSameEmail = new Restaurant();
			restaurantSameEmail = restaurantDAO.get(restaurant.getContactEmail());
						
			if(restaurantSameName != null && !(restaurantSameName.getId() == restaurant.getId())) {
				// Ya hay un restaurante con el mismo nombre. Se añade un mensaje de error y se despacha
				messages.put("name_already_exists", "Ya existe un restaurante con el mismo nombre.");	
				dispatch_error = true;
			} else if(restaurantSameEmail != null && !(restaurantSameEmail.getId() == restaurant.getId())) { // Se comprueba si el email se repite
					// Ya hay un restaurante con el mismo email. Se añade un mensaje de error y se despacha
					messages.put("email_already_exists", "Ya existe un restaurante con el mismo email.");
					dispatch_error = true;
			} else { // Los datos son válidos y no existe un restaurante con el mismo nombre
					// Se actualiza el restaurante en la base de datos
					restaurantDAO.update(restaurant);
					// Se actualizan las categorías del restaurante en la base de datos: se eliminan los registros existentes para insertar los nuevos
					RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
					restaurantCategoriesDAO.setConnection(conn);
					restaurantCategoriesDAO.deleteAll(restaurant.getId());
					restaurantCategoriesDAO.addAll(restaurantCategoriesMap, restaurant.getId());
					// Se redirecciona al servlet SearchAndCategories.do
					response.sendRedirect("RestaurantProfileServlet.do?id="+restaurant.getId());
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
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/EditRestaurant.jsp");
			view.forward(request,response);
		}
	}

}
