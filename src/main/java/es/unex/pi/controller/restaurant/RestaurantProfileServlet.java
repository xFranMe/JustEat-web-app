package es.unex.pi.controller.restaurant;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantCategoriesDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.JDBCReviewsDAOImpl;
import es.unex.pi.dao.RestaurantCategoriesDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.dao.ReviewsDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.RestaurantCategories;
import es.unex.pi.model.Review;

/**
 * Servlet implementation class RestaurantProfileServlet
 */
@WebServlet(urlPatterns = {"/RestaurantProfileServlet.do"})
public class RestaurantProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantProfileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recupera la conexión con la base de datos
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		
		try {
			// Se obtiene el ID del restaurante de la request
			Long idr = Long.parseLong(request.getParameter("id"));
			
			// Se recupera la información del restaurante de la BD y se añade a la request si no es null
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(conn);
			Restaurant restaurant = restaurantDAO.get(idr);
			
			if(restaurant != null) {
				request.setAttribute("restaurant", restaurant);
				
				// Se recupera las categorías del restaurante y se añaden a la request
				List<Category> categoryList = new ArrayList<>();
				RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
				restaurantCategoriesDAO.setConnection(conn);
				categoryList = restaurantCategoriesDAO.getRestaurantCategories(idr);
				request.setAttribute("categoryList", categoryList);
				
				// Se recuperan los platos del restaurante y se añaden a la request
				DishDAO dishDAO = new JDBCDishDAOImpl();
				dishDAO.setConnection(conn);
				List<Dish> dishList = new ArrayList<>();
				dishList = dishDAO.getByRestaurantId(idr);
				request.setAttribute("dishList", dishList);
				
				// Se recuperan las reseñas (aka valoraciones) del restaurante y se añaden a la request
				ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
				reviewsDAO.setConnection(conn);
				List<Review> reviewsList = reviewsDAO.getAllByRestaurant(idr);
				request.setAttribute("reviewsList", reviewsList);
				
				// Se recuperan los nombres de los usuarios que han realizado las reseñas del restaurante y se añade a la request
				Map<Long, String> usersMap = new HashMap<>();
				usersMap = reviewsDAO.getUsernamesByRestaurant(idr);
				request.setAttribute("usersMap", usersMap);
				
				//---------------------Ampliación 4--------------------------
				
				// Se recuperan los restaurantes relacionados por categorías y se añaden a la request
				// Se usa un mapa para no duplicar restaurantes
				Map<Long, Restaurant> similarCategoriesMap = new HashMap<>();
				for (RestaurantCategories restaurantCategories : restaurantCategoriesDAO.getAllByRestaurant(idr)) {
					for (RestaurantCategories restaurantCategories2 : restaurantCategoriesDAO.getAllByCategory(restaurantCategories.getIdct())) {
						similarCategoriesMap.put(restaurantCategories2.getIdr(), restaurantDAO.get(restaurantCategories2.getIdr()));
					}
				}
				similarCategoriesMap.remove(idr); // Se elimina del mapa el propio restaurante
				
				List<Restaurant> listAux = new ArrayList<>(similarCategoriesMap.values());
				request.setAttribute("similarCategoriesList", listAux);
						
				// Se recuperan los restaurantes relacionados por localidad y se añaden a la request
				listAux = restaurantDAO.getCityRelated(restaurant);
				request.setAttribute("similarCityList", listAux);
				
				// Se recuperan los restaurantes relacionados por precio y se añaden a la request
				listAux = restaurantDAO.getPriceRelated(restaurant);
				request.setAttribute("similarPriceList", listAux);
				
				// Se recuperan los restaurantes relacionados por valoración y se añaden a la request
				listAux = restaurantDAO.getGradeRelated(restaurant);
				request.setAttribute("similarGradeList", listAux);
				
				// Se despacha la request con toda la información del restaurante, sus categorías y sus platos
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/restaurant/RestaurantProfile.jsp");
				view.forward(request, response);
			} else {
				// No existe restaurante con el ID pasado
				response.sendRedirect("SearchAndCategoriesServlet.do");
			}
		}
		catch (NumberFormatException e) {
			// No se ha intoducido un ID numérico
			response.sendRedirect("SearchAndCategoriesServlet.do");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Simplemente se invoca el método doGet() del mismo servlet, ya que no se necesita una funcionalidad concreta para el doPost()
		doGet(request, response);
	}
}
