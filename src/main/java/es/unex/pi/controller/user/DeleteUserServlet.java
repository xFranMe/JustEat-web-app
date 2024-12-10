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

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

/**
 * Servlet implementation class DeleteUserServlet
 */
@WebServlet(urlPatterns = {"/DeleteUserServlet.do" })
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Este método despacha la request al JSP que pide la confirmación del usuario para la eliminación de la cuenta.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se despacha la request a DeleteUserConfirmation.jsp
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/user/DeleteUserConfirmation.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Este método se ejecuta cuando el usuario confirma que desea eliminar la cuenta en DeleteUserConfirmation.jsp.
	 * Elimina al usuario de la DB y de la sesión, redireccionando al usuario a la página de inicio de sesión.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se procede a eliminar al usuario del sistema
		// Se obtiene la conexión a la BD
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);
		
		// Se obtiene el usuario de la sesión
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user != null) {
			// Se elimina de la base de datos
			userDAO.delete(user.getId());
			// Se invalida la sesión
			session.invalidate();
		}
		
		// Se redirecciona al login una vez eliminado el usuario
		response.sendRedirect("LoginServlet.do");
	}
}
