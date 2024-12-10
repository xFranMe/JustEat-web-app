<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Categoría: ${category.name}</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search/restaurantList.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restaurant/restaurantCollection.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/footer.css">
    </head>
    <body>
        <!--Header-->
        <header class="content"> 
            <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
            <a id="profile" href="UserProfileServlet.do">Perfil</a>
            <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">          
        </header>
        
        <!--Category results-->
        <section class="content">
            <h3>Restaurantes con la categoría ${category.name}</h3>
            <h4><c:choose>
    				<c:when test="${not empty restaurantCategoryList}">¡Los siguientes restaurantes han sido encontrados!</c:when>
  					<c:otherwise>
  						Lo sentimos, no se ha encontrado restaurantes con la categoría seleccionada.
  					</c:otherwise>
			  </c:choose></h4>
            <div class="restaurant-collection">
                <c:forEach items="${restaurantCategoryList}" var="restaurant">
                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurant.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       <div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurant.name} Logo">
                       </div>
                       <div>
                           <h3 class="restaurant-name">${restaurant.name}</h3>
                           <p class="description">${restaurant.description}</p>
                           <p>Dirección: <span>${restaurant.address}</span> <span>(${restaurant.city})</span></p>
                           <p>Valoración media: <span>${restaurant.gradesAverage}</span>⭐</p> 
                       </div>        
	               	</div>
               	</a>
                </c:forEach>
            </div>
        </section>
	        
        <!--Footer-->
        <%@ include file="../general/footer.html" %>
         
    </body>
</html>