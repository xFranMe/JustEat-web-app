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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCOrderDAOImpl;
import es.unex.pi.dao.JDBCOrderDishesDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.OrderDAO;
import es.unex.pi.dao.OrderDishesDAO;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.Order;
import es.unex.pi.model.OrderDishes;
import es.unex.pi.model.Restaurant;
import es.unex.pi.model.User;
import es.unex.pi.util.Triplet;

/**
 * Servlet implementation class ListOrderDishesServlet
 */
@WebServlet(urlPatterns = {"/ListOrderDishesServlet.do"})
public class ListOrderDishesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListOrderDishesServlet() {
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
			// Se obtiene el ID del pedido de la request
			Long ido = Long.valueOf(request.getParameter("id"));
			
			// Se obtiene el pedido de la BD
			OrderDAO orderDAO = new JDBCOrderDAOImpl();
			orderDAO.setConnection(conn);
			Order order = orderDAO.get(ido);
			
			// Se obtiene el usuario de la sesión
			HttpSession session = request.getSession(true);
			User user = (User) session.getAttribute("user");
			
			if(order != null && order.getIdu() == user.getId()) {
				// Se elabora un mapa con toda la información de los platos del pedido
				Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = new HashMap<>();
				
				// Se recuperan todos los objetos OrderDishes del pedido
				OrderDishesDAO orderDishesDAO = new JDBCOrderDishesDAOImpl();
				orderDishesDAO.setConnection(conn);
				List<OrderDishes> orderDishesList = orderDishesDAO.getAllByOrder(ido);
				
				// A partir de los anteriores objetos, se recupera la información de los platos y sus restaurantes
				// En caso de que restaurantes y/o platos hayan sido borrados, se mostrará en el pedido como eliminados
				DishDAO dishDAO = new JDBCDishDAOImpl();
				dishDAO.setConnection(conn);
				RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
				restaurantDAO.setConnection(conn);
				Dish dish;
				Restaurant restaurant;
				Triplet<OrderDishes, Dish, Restaurant> triplet;
				for (OrderDishes orderDishes : orderDishesList) {
					dish = dishDAO.get(orderDishes.getIddi());
					if(dish == null) { // Si el plato ha sido borrado, no se puede acceder al restaurante
						dish = new Dish();
						dish.setName("Plato eliminado");
						restaurant = new Restaurant();
						restaurant.setName("La información del plato no está disponible");
					} else {
						restaurant = restaurantDAO.get(dish.getIdr());
					}
					triplet = new Triplet<OrderDishes, Dish, Restaurant>(orderDishes, dish, restaurant);
					orderDishesMap.put(dish.getId(), triplet);
				}
				
				// Se añade a la request el mapa con toda la información de los platos y sus restaurantes
				request.setAttribute("totalBill", order.getTotalPrice());
				// Se añade también a la request el precio total que fue pagado
				request.setAttribute("orderDishesList", orderDishesMap);
				// Se despacha a ListOrderDishes.jsp
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/order/ListOrderDishes.jsp");		
				view.forward(request, response);
			} else {
				// No existe pedido con el ID pasado o no pertenece al usuario
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
		doGet(request, response);
	}

}
