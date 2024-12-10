package es.unex.pi.controller.review;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.JDBCReviewsDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.dao.ReviewsDAO;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.Review;
import es.unex.pi.model.User;

/**
 * Servlet implementation class AddReviewServlet
 */
@WebServlet(urlPatterns = {"/CreateReviewServlet.do"})
public class CreateReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateReviewServlet() {
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
			
			if(restaurant != null) {
				// Existe el restaurante
				// Se comprueba, de nuevo (por si el usuario accede a este servlet directamente desde su url), que el usuario no haya publicado ya una reseña para el restaurante
				ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
				reviewsDAO.setConnection(conn);
				User user = (User) request.getSession().getAttribute("user");
				if(reviewsDAO.get(idr, user.getId()) != null) {
					// Ya existe la reseña, se redirecciona a la página de perfil del restaurante
					response.sendRedirect("RestaurantProfileServlet.do?id="+idr);
				} else {
					// No existe reseña, por lo que se despacha al formulario de creación de una nueva reseña
					// Se añade a la request la info del restaurante para personalizar el formulario y tenerlo en el doPost
					request.setAttribute("restaurant", restaurant);					
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/review/CreateReview.jsp");
					view.forward(request, response);
				}
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
		// Se recuperan los datos introducidos en el formulario
		Review review = new Review();
		User user = (User) request.getSession().getAttribute("user");
		long idr = Long.parseLong(request.getParameter("id"));
		review.setIdr(idr);
		review.setIdu(user.getId());
		review.setGrade(Integer.parseInt(request.getParameter("grade")));
		review.setReview(request.getParameter("comment"));
		
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		ReviewsDAO reviewsDAO = new JDBCReviewsDAOImpl();
		reviewsDAO.setConnection(conn);
		// Se añade la reseña a la BD
		reviewsDAO.add(review);
		// Se actualiza la valoración media del restaurante tras añadir la nueva reseña
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		restaurantDAO.updateGradesAverage(idr);
		// Se redirecciona al perfil del restaurante
		response.sendRedirect("RestaurantProfileServlet.do?id="+idr);
	}
}
