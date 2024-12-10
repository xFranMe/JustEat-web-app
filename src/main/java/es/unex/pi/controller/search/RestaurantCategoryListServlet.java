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

import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCRestaurantCategoriesDAOImpl;
import es.unex.pi.dao.RestaurantCategoriesDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Restaurant;

/**
 * Servlet implementation class RestaurantCategoryList
 */
@WebServlet(urlPatterns = {"/RestaurantCategoryListServlet.do"})
public class RestaurantCategoryListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantCategoryListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Se recupera la conexión con la base de datos
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		
		try {
			// Se obtiene el ID de la categoría de la request
			Long categoryId = Long.parseLong(request.getParameter("id"));
			
			// Se obtiene la categoría de la BD
			CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
			categoryDAO.setConnection(conn);
			Category category = categoryDAO.get(categoryId);
			
			if(category != null) {
				// Se añade la categoría a la request
				request.setAttribute("category", category);
				
				// Se obtienen todos los restaurantes con la categoría
				List<Restaurant> restaurantCategoryList = new ArrayList<>();
				RestaurantCategoriesDAO restaurantCategoriesDAO = new JDBCRestaurantCategoriesDAOImpl();
				restaurantCategoriesDAO.setConnection(conn);
				restaurantCategoryList = restaurantCategoriesDAO.getRestaurantsByCategory(category.getId());
				
				// Se añade la lista a la request
				request.setAttribute("restaurantCategoryList", restaurantCategoryList);
				
				// Se despacha la request con toda la información de los restaurantes
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/search/RestaurantCategoryList.jsp");
				view.forward(request, response);
			} else {
				// No existe categoría con el ID pasado
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
		doGet(request, response);
	}

}
