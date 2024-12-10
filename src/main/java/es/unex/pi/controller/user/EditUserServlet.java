package es.unex.pi.controller.user;

import jakarta.servlet.RequestDispatcher;
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

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet(urlPatterns = {"/EditUserServlet.do" })
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método despacha la request al jsp EditUser.jsp (compartido por EditUserServlet y SignUpServlet).
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recupera el usuario loggeado de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		request.setAttribute("user", user);
		
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando un usuario edita su información, realizando las comprobaciones pertinentes.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se recuperan los datos introducidos por el usuario para crear un nuevo objeto
		User editedUser = new User();
		editedUser.setName(request.getParameter("name"));
		editedUser.setSurname(request.getParameter("surname"));
		editedUser.setEmail(request.getParameter("email"));
		editedUser.setPassword(request.getParameter("password"));
		
		// Se comprueba la validez de los datos introducidos
		Map<String, String> messages = new HashMap<String, String>();
		if (editedUser.validate(messages)) {
			// Se establece la conexión con la BD
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			UserDAO userDAO = new JDBCUserDAOImpl();
			userDAO.setConnection(conn);
			
			// Se recupera el usuario loggeado de la sesión
			HttpSession session = request.getSession();
			User sessionUser = (User) session.getAttribute("user");
			
			// Se comprueba si un usuario ya está registrado con el mismo email (que no sea el suyo propio)
			if(!editedUser.getEmail().equals(sessionUser.getEmail()) && userDAO.getByEmail(editedUser.getEmail()) != null) { // Si el email ha cambiado, se debe comprobar que no haya otro user con el mismo email
																													// Si el mail no ha cambiado, no hace falta comprobar que haya otro usuario con el mismo
				// Un usuario con el mismo email existe. Se añade un mensaje de error y se despacha
				messages.put("user_already_exists", "El nuevo email introducido no está disponible.");
				// Se almacenan los errores en la request
				request.setAttribute("messages", messages);
				// Se mantiene la información introducida para no hacer que el usuario vuelva a escribirlo todo
				request.setAttribute("user", editedUser);
				// Se despacha a EditUser.jsp
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
				view.forward(request,response);
			} else { // Los datos son válidos y no existe un usuario con el mismo email (en caso de que haya sido editado el email)
				// Se recupera el ID del usuario de la sesión
				editedUser.setId(sessionUser.getId());
				// Se actualiza el usuario en la BD
				userDAO.update(editedUser);
				// Se almacena el usuario en la sesión
				session.setAttribute("user", editedUser);
				// Se redirecciona al servlet UserProfileServlet.do
				response.sendRedirect("UserProfileServlet.do");
			}
		} else { // Datos no válidos
			// Se almacenan los errores en la request
			request.setAttribute("messages", messages);
			// Se mantiene la información introducida para no hacer que el usuario vuelva a escribirlo todo
			request.setAttribute("user", editedUser);
			// Se despacha a EditUser.jsp
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/EditUser.jsp");
			view.forward(request,response);
		}
	}
}
