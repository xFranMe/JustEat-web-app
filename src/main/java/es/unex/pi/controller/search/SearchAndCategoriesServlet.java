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
import es.unex.pi.model.Category;

/**
 * Servlet implementation class SearchAndCategoriesServlet
 */
@WebServlet(urlPatterns = {"/SearchAndCategoriesServlet.do"})
public class SearchAndCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchAndCategoriesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando se redirecciona a la pantalla principal de la aplicación.
	 * Antes de despachar la request se obtienen las categorías existentes para mostrárselas al usuario.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se establece conexión con la BD
		ServletContext servletContext = getServletContext();
		Connection connection = (Connection) servletContext.getAttribute("dbConn");
		CategoryDAO categoryDAO = new JDBCCategoryDAOImpl();
		categoryDAO.setConnection(connection);
		
		// Se obtienen las categorías principales
		List<Category> categoryList = new ArrayList<Category>();
		categoryList = categoryDAO.getAll();
		
		// Se almacena en la request la lista de categorías obtenida
		request.setAttribute("categoryList", categoryList);
		
		// Se despacha la request a SearchAndCategories.jsp
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/search/SearchAndCategories.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Redirecciona al método doGet() del mismo servlet.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
