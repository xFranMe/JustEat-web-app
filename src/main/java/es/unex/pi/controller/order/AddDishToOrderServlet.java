package es.unex.pi.controller.order;

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
import java.util.Map;

import es.unex.pi.dao.DishDAO;
import es.unex.pi.dao.JDBCDishDAOImpl;
import es.unex.pi.dao.JDBCRestaurantDAOImpl;
import es.unex.pi.dao.RestaurantDAO;
import es.unex.pi.model.Dish;
import es.unex.pi.model.OrderDishes;
import es.unex.pi.model.Restaurant;
import es.unex.pi.util.Triplet;

/**
 * Servlet implementation class AddDishToOrderServlet
 */
@WebServlet(urlPatterns = {"/AddDishToOrderServlet.do"})
public class AddDishToOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDishToOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se obtiene de la request el ID del plato que se añade al pedido y la cantidad que se ha seleccionado
		Long idd = Long.parseLong(request.getParameter("id"));
		int amount = Integer.valueOf(request.getParameter("amount"));
		
		// Se recupera la conexión con la base de datos para recuperar la información del plato
		ServletContext servletContext = getServletContext();
		Connection conn = (Connection) servletContext.getAttribute("dbConn");
		DishDAO dishDAO = new JDBCDishDAOImpl();
		dishDAO.setConnection(conn);
		Dish dish = dishDAO.get(idd);
		
		// Se recupera también la información del restaurante al que pertenece el plato
		RestaurantDAO restaurantDAO = new JDBCRestaurantDAOImpl();
		restaurantDAO.setConnection(conn);
		Restaurant restaurant = restaurantDAO.get(dish.getIdr());
		
		// Se recupera de la sesión la lista de platos (es un mapa, para agilizar búsquedas) que ya se tienen en el pedido
		// Si no existe aún el mapa de platos, se crea
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		if(orderDishesMap == null) {
			// Se crea el mapa
			orderDishesMap = new HashMap<>();
		} 
			
		// Hay que buscar si ya tiene una entrada con el ID del plato que se quiere añadir
		Triplet<OrderDishes, Dish, Restaurant> triplet = orderDishesMap.get(dish.getId());
		if(triplet != null) {
			// Ya existía el plato en el pedido, por lo que simplemente se actualiza la cantidad
			int currentAmount = triplet.getFirst().getAmount();
			triplet.getFirst().setAmount(currentAmount + amount);
		} else {
			// No existe el plato en el pedido, por lo que se crea el nuevo triplet
			// Se necesita OrderDishes, Dish y Restaurant (ya tenemos dish y restaurant)
			OrderDishes orderDishes = new OrderDishes();
			orderDishes.setIddi(dish.getId());
			orderDishes.setAmount(amount);
			triplet = new Triplet<OrderDishes, Dish, Restaurant>(orderDishes, dish, restaurant);
		}
		
		// Se añade/actualiza la entrada
		orderDishesMap.put(dish.getId(), triplet);
		
		// Se añade a la sesión el mapa una vez ya está actualizado
		session.setAttribute("orderDishesList", orderDishesMap);
		
		// Se redirige a la página del restaurante del que se viene	
		response.sendRedirect("RestaurantProfileServlet.do?id="+dish.getIdr());
	}
}
