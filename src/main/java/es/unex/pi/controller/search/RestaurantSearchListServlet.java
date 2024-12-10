package es.unex.pi.controller.search;

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
import java.util.List;

import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Restaurant;
import es.unex.pi.util.LevenshteinSearch;

/**
 * Servlet implementation class RestaurantListServlet
 */
@WebServlet(urlPatterns = {"/RestaurantSearchListServlet.do"})
public class RestaurantSearchListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantSearchListServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recupera el texto introducido como texto de búsqueda y la disponibilidad seleccionada
		String searchText = request.getParameter("search");
		String availability = request.getParameter("available");
		String gradeOrder = request.getParameter("grade");
		
		if(searchText != null && availability != null && gradeOrder != null) {
			
			if(!gradeOrder.matches("0|1")) {
				response.sendRedirect("SearchAndCategoriesServlet.do");
				return;
			}
			
			// Se recupera la conexión con la BD y se establece a través de RestaurantDAO
			ServletContext servletContext = getServletContext();
			Connection connection = (Connection) servletContext.getAttribute("dbConn");
			RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
			restaurantDAO.setConnection(connection);
			
			// Se recuperan los restaurantes de la BD en función de la disponibilidad seleccionada y el orden
			List<Restaurant> allRestaurants = new ArrayList<>();
			switch (availability) {
			case "0":
			case "1":
				allRestaurants = restaurantDAO.getAllByAvailabilityOrdered(Integer.parseInt(availability), Integer.parseInt(gradeOrder));
				break;
			case "-1":
				allRestaurants = restaurantDAO.getAllOrdered(Integer.parseInt(gradeOrder));
				break;
			default: // Si se ha modificado la URL y el valor de disponibilidad es cualquier otro, se redirecciona a SearchAndCategoriesServlet
				response.sendRedirect("SearchAndCategoriesServlet.do");
				return;
			}
			
			if(searchText.isEmpty() || searchText.isBlank()) { // Si no se introducido ningún valor en la barra de búsqueda, no se filtra la lista allRestaurants obtenida y se devuelve tal cual
				request.setAttribute("availability", availability);
				request.setAttribute("noSearchtextList", allRestaurants);
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/search/RestaurantSearchList.jsp");
				view.forward(request, response);
			} else {
				List<Restaurant> listCityAddress = new ArrayList<>();
				List<Restaurant> listName = new ArrayList<>();
				List<Restaurant> listDescription = new ArrayList<>();
				for (Restaurant restaurant : allRestaurants) {
					// Se recuperan los retaurantes coincidentes por ciudad o dirección: tolerancia a fallo de 3 caracteres o inclusión de la cadena
					if ((LevenshteinSearch.distance(restaurant.getCity().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getCity().toLowerCase().contains(searchText.toLowerCase()) || searchText.toLowerCase().contains(restaurant.getCity().toLowerCase()))
						|| (LevenshteinSearch.distance(restaurant.getAddress().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getAddress().toLowerCase().contains(searchText.toLowerCase()))) {
		                listCityAddress.add(restaurant);
		            }
					// Se recuperan los retaurantes coincidentes por nombre: tolerancia a fallo de 3 caracteres o inclusión de la cadena
					if (LevenshteinSearch.distance(restaurant.getName().toLowerCase(), searchText.toLowerCase()) <= 3 || restaurant.getName().toLowerCase().contains(searchText.toLowerCase()) || searchText.toLowerCase().contains(restaurant.getName().toLowerCase())) {
		                listName.add(restaurant);
		            }
					// Se recuperan los retaurantes coincidentes por descripción: tolerancia a fallo de 7 caracteres
					if (LevenshteinSearch.distance(restaurant.getDescription().toLowerCase(), searchText.toLowerCase()) <= 7) {
		                listDescription.add(restaurant);
		            }		
				}
				
				// Se añaden las listas a la request y se despacha a RestaurantList.jsp
				request.setAttribute("restaurantListByCityAddress", listCityAddress);
				request.setAttribute("restaurantListByName", listName);		
				request.setAttribute("restaurantListByDescription", listDescription);
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/search/RestaurantSearchList.jsp");
				view.forward(request, response);
			}			
		} else {
			response.sendRedirect("SearchAndCategoriesServlet.do");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}

}
