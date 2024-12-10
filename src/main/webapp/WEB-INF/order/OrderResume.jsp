<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mi pedido</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/orderResume.css">
    	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/footer.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
                <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
                <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
        </header>

        <!--Order dishes-->
        <c:choose>
			<c:when test="${empty sessionScope.orderDishesList}">
					<h2>Parece que aún no has añadido ningún plato a tu pedido.</h2>
			</c:when>
			<c:otherwise>
        		<section class="content">
		            <h2>Resumen de tu pedido</h2>       				
		            <div id="dishes-collection">
		                <c:forEach items="${sessionScope.orderDishesList}" var="dish">
		                <div class="card">
		                    <div>
		                        <h3>${dish.value.second.name} (${dish.value.third.name})</h3>
		                        <p>${dish.value.second.description}</p>
		                        <p>${dish.value.second.price} €</p>
		                        <p>${dish.value.first.amount} unidad/es</p>
		                		<a href="<c:url value='DeleteDishFromOrderServlet.do?id=${dish.value.second.id}'/>">Eliminar del pedido</a>
		                    </div>
		                    <div class="dish-picture">
		                        <img src="${pageContext.request.contextPath}/img/dish-icon.png" alt="${dish.value.second.name}">
		                    </div>
		                </div>
		                </c:forEach>
		            </div>
		            <p id="bill">Precio total a pagar: <span>${requestScope.totalBill} €</span></p>
		            <form method="POST" action="CreateOrderServlet.do">
		            	<input type="hidden" name="bill" value="${requestScope.totalBill}">
		            	<input id="register-form-submit" type="submit" value="Realizar pedido">
		            </form>
				</section>
		   	</c:otherwise>
		</c:choose>
        
    </body>
</html>