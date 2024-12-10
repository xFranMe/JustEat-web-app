package es.unex.pi.controller.order;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

import es.unex.pi.model.Dish;
import es.unex.pi.model.OrderDishes;
import es.unex.pi.model.Restaurant;
import es.unex.pi.util.Triplet;

/**
 * Servlet implementation class DeleteDishFromOrderServlet
 */
@WebServlet(urlPatterns = {"/DeleteDishFromOrderServlet.do"})
public class DeleteDishFromOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDishFromOrderServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// Se obtiene el ID del plato a borrar
			Long idd = Long.valueOf(request.getParameter("id"));
			
			// Se recupera el mapa con todos los platos del pedido actual
			HttpSession session = request.getSession();
			Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
			
			if(orderDishesMap == null) {
				// El pedido está vacío, por lo que se redirige al perfil de usuario
				response.sendRedirect("UserProfileServlet.do");
				return;
			}
			
			Triplet<OrderDishes, Dish, Restaurant> triplet = orderDishesMap.get(idd);
			if(triplet == null) {
				// No existe el plato en el pedido, se redirige al perfil de usuario
				response.sendRedirect("UserProfileServlet.do");
				return;
			}
			
			Dish dish = triplet.getSecond();
			request.setAttribute("dish", dish);
			// Se despacha la request a DeleteDishFromOrder.jsp
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/order/DeleteDishFromOrder.jsp");
			view.forward(request, response);
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
		// Se recupera el ID del plato a borrar de la request
		Long idd = Long.parseLong(request.getParameter("id"));
		
		// Se recupera el mapa con los platos del pedido
		HttpSession session = request.getSession();
		Map<Long, Triplet<OrderDishes, Dish, Restaurant>> orderDishesMap = (Map<Long, Triplet<OrderDishes, Dish, Restaurant>>) session.getAttribute("orderDishesList");
		
		// Se elimina el plato del mapa
		if(orderDishesMap != null) {
			orderDishesMap.remove(idd);
		}
		
		// Se redirecciona al pedido
		response.sendRedirect("CreateOrderServlet.do");
	}

}
