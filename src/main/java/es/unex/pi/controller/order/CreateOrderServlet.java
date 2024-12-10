package es.unex.pi.controller.order;

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
import java.util.Map;

import es.unex.pi.dao.JDBCOrderDAOImpl;
import es.unex.pi.dao.JDBCOrderDishesDAOImpl;
import es.unex.pi.dao.OrderDAO;
import es.unex.pi.dao.OrderDishesDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Order;
import es.unex.pi.model.OrderDishes;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;
import es.unex.pi.util.Triplet;

/**
 * Servlet implementation class ListOrderDishesServlet
 */
@WebServlet(urlPatterns = {"/CreateOrderServlet.do"})
public class CreateOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateOrderServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Antes de despachar a ListOrderDishes.jsp se debe calcular el precio total del pedido para mostrárselo al usuario
		float totalBill = 0;
		// Se recupera de la sesión el mapa con los platos añadidos al pedido
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		if(orderDishesMap != null) {
			// Se recorre el mapa y se va actualizando el precio total del pedido
			for (var entry : orderDishesMap.entrySet()) {
			    totalBill = totalBill + (entry.getValue().getFirst().getAmount() * entry.getValue().getSecond().getPrice());
			}
		}
		
		// Se añade el precio total a la request
		request.setAttribute("totalBill", totalBill);
		
		// Se despacha la request a ListOrderDishes.jsp
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/order/OrderResume.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se obtiene de la request el precio total del pedido 
		float totalBill = Float.parseFloat(request.getParameter("bill"));
		
		// Se recupera de la sesión el mapa con todos los platos del pedido
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		// Se recupera de la sesión el usuario que realiza el pedido (el que está logeado)
		User user = (User) session.getAttribute("user");
		
		if(orderDishesMap != null) {
			// Se crea el objeto pedido pertinente
			Order order = new Order(); // El ID del pedido lo generará la BD
			order.setIdu(user.getId());
			order.setTotalPrice(totalBill);
			
			// Se recupera la conexión con la BD
			ServletContext servletContext = getServletContext();
			Connection conn = (Connection) servletContext.getAttribute("dbConn");
			
			// Se añade el pedido a la BD
			OrderDAO orderDAO = new JDBCOrderDAOImpl();
			orderDAO.setConnection(conn);
			orderDAO.add(order);
			
			// Se recupera de la BD el último registro de la tabla Order con el ID del usuario logeado
			// Esto se hace para recuperar el ID de pedido que ha generado la BD. Se necesita para los objetos OrderDishes
			order = orderDAO.getLastOrderFromUser(user.getId());
			
			// Se recorre la lista de platos del mapa y se van añadiendo a la BD
			OrderDishesDAO orderDishesDAO = new JDBCOrderDishesDAOImpl();
			orderDishesDAO.setConnection(conn);
			OrderDishes orderDishes;
			for (var entry : orderDishesMap.entrySet()) {
			    orderDishes = entry.getValue().getFirst();
			    orderDishes.setIdo(order.getId()); // Aquí se añade el ID del pedido
			    orderDishesDAO.add(orderDishes);
			}
			
			// Se borra el mapa de platos del pedido de la sesión
			session.removeAttribute("orderDishesList");
		}
		
		// Se redirige a la página de perfil del usuario
		response.sendRedirect("UserProfileServlet.do");
	}
}
