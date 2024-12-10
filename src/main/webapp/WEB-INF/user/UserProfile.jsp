<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Perfil de ${sessionScope.user.name}</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/userProfile.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restaurant/restaurantCollection.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
            <div>
                <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
                <a class="text-header" href="CreateOrderServlet.do">Mi pedido</a>
                <a class="text-header" href="LogoutServlet.do">Cerrar sesión</a>
                <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
            </div>
        </header>

        <!--Profile info-->
        <section id="main" class="content">
            <div id="profile-info" class="box-shadow">
                <img src="${pageContext.request.contextPath}/img/user-logo.jpg" alt="Logo Perfil Usuario">
                <h1>Perfil de ${sessionScope.user.name}</h1>
                <div>Nombre:</div>
                <p id="name">${sessionScope.user.name}</p>
                <div>Apellidos:</div>
                <p id="surname">${sessionScope.user.surname}</p>
                <div>Dirección de correo electrónico:</div>
                <p id="email">${sessionScope.user.email}</p>
                <div><a href="EditUserServlet.do">Editar perfil</a></div>
                <div><a href="DeleteUserServlet.do">Eliminar cuenta</a></div>
            </div>
        </section>
        
        <!--Restaurants-->
        <section class="content">
        	<h2>Mis restaurantes</h2>
        	<div id="new-restaurant">
        		<a href="CreateRestaurantServlet.do">Crear nuevo restaurante</a>
        	</div>
            
            <div class="restaurant-collection">
            
            <c:forEach var="restaurant" items="${requestScope.restaurantList}">
            	<a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurant.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       <div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurant.name} Logo">
                       </div>
                       <div>
                           <h3>${restaurant.name}</h3>
                           <p class="description">${restaurant.description}</p>
                           <p>Dirección: <span>${restaurant.address}</span> <span>(${restaurant.city})</span></p>
                           <p>Valoración media: <span>${restaurant.gradesAverage}</span>⭐</p> 
                       </div>        
	               	</div>
               	</a>
           	</c:forEach>
           	
            </div>
            
        </section>
        
        <!--Orders-->
        <section class="content">
        	<h2>Mis pedidos anteriores</h2>
        	
        	<c:choose>
				<c:when test="${empty requestScope.orderList}">
					<h4>Todavía no se ha realizado ningún pedido.</h4>
				</c:when>
				<c:otherwise>
        			<ul>
            			<c:forEach var="order" items="${requestScope.orderList}">
            				<li>
            					<p><span class="order-info">Código de pedido #${order.id}</span> </p>
            					<p><span class="order-info">Total pagado:</span> ${order.totalPrice} €</p>
            					<a href="<c:url value='ListOrderDishesServlet.do?id=${order.id}'/>">Ver detalles</a>
            				</li>
           				</c:forEach>
            		</ul>
		   		</c:otherwise>
			</c:choose>
        
        </section>
    </body>
</html>