<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Resultados de la búsqueda</title>
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
        
        <c:choose>
  			<c:when test="${not empty noSearchtextList}">
  			<!--Search results: only availability results-->
	        <section class="content">
	            <h3>Restaurantes encontrados</h3>
	            <h4><c:choose>
				        <c:when test="${availability == 1}">Todos los restaurantes mostrados a continuación aceptan pedidos en este momento.</c:when>
				        <c:when test="${availability == 0}">Los siguientes restaurantes no están aceptando pedidos por el momento.</c:when>
				        <c:otherwise>A continuación se muestran todos los restaurantes, tanto los que aceptan pedidos como los que no.</c:otherwise>
				  </c:choose></h4>
	            <div class="restaurant-collection">
	                <c:forEach items="${noSearchtextList}" var="restaurant">
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
  			</c:when>
			<c:otherwise>
	        <!--Search results: restaurants by city or address-->
	        <section class="content">
	            <h3>Coincidencias por localidad o dirección</h3>
	            <h4><c:choose>
	    				<c:when test="${not empty restaurantListByCityAddress}">¡Las siguientes coincidencias han sido encontradas!</c:when>
	  					<c:otherwise>
	  						Lo sentimos, no se ha encontrado coincidencias por localidad o dirección.
	  					</c:otherwise>
				  </c:choose></h4>
	            <div class="restaurant-collection">
	                <c:forEach items="${restaurantListByCityAddress}" var="restaurantByCityAddress">
	                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByCityAddress.id }'/>">
		            	<div class="restaurant-card box-shadow">  
	                       <div class="restaurant-picture-box">
	                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByCityAddress.name} Logo">
	                       </div>
	                       <div>
	                           <h3 class="restaurant-name">${restaurantByCityAddress.name}</h3>
	                           <p class="description">${restaurantByCityAddress.description}</p>
	                           <p>Dirección: <span>${restaurantByCityAddress.address}</span> <span>(${restaurantByCityAddress.city})</span></p>
	                           <p>Valoración media: <span>${restaurantByCityAddress.gradesAverage}</span>⭐</p> 
	                       </div>        
		               	</div>
	               	</a>
	                </c:forEach>
	            </div>
	        </section>
	        
	        <!--Search results: restaurants by name-->
	        <section class="content">
	            <h3>Coincidencias por nombre de restaurante</h3>
	            <h4><c:choose>
	    				<c:when test="${not empty restaurantListByName}">¡Genial! Los siguientes restaurantes han sido encontrados.</c:when>
	  					<c:otherwise>
	  						Parece que no existe ningún restaurante cuyo nombre coincida con el criterio de búsqueda...
	  					</c:otherwise>
				  </c:choose></h4>
	            <div class="restaurant-collection">
	                <c:forEach items="${restaurantListByName}" var="restaurantByName">
	                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByName.id }'/>">
		            	<div class="restaurant-card box-shadow">  
	                       <div class="restaurant-picture-box">
	                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByName.name} Logo">
	                       </div>
	                       <div>
	                           <h3 class="restaurant-name">${restaurantByName.name}</h3>
	                           <p class="description">${restaurantByName.description}</p>
	                           <p>Dirección: <span>${restaurantByName.address}</span> <span>(${restaurantByName.city})</span></p>
	                           <p>Valoración media: <span>${restaurantByName.gradesAverage}</span>⭐</p> 
	                       </div>        
		               	</div>
	               	</a>
	                </c:forEach>
	            </div>
	        </section>
	        
	        <!--Search results: restaurants by description-->
	        <section class="content">
	            <h3>Coincidencias por descripción de restaurante</h3>
	            <h4><c:choose>
	    				<c:when test="${not empty restaurantListByDescription}">Quizá las siguientes descripciones se ajusten al criterio de búsqueda...</c:when>
	  					<c:otherwise>
	  						Vaya, aquí hay mucho vacío. Prueba a buscar por otra descripción la próxima vez.
	  					</c:otherwise>
				  </c:choose></h4>
	            <div class="restaurant-collection">
	                <c:forEach items="${restaurantListByDescription}" var="restaurantByDescription">
	                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByDescription.id }'/>">
		            	<div class="restaurant-card box-shadow">  
	                       <div class="restaurant-picture-box">
	                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByDescription.name} Logo">
	                       </div>
	                       <div>
	                           <h3 class="restaurant-name">${restaurantByDescription.name}</h3>
	                           <p class="description">${restaurantByDescription.description}</p>
	                           <p>Dirección: <span>${restaurantByDescription.address}</span> <span>(${restaurantByDescription.city})</span></p>
	                           <p>Valoración media: <span>${restaurantByDescription.gradesAverage}</span>⭐</p> 
	                       </div>        
		               	</div>
	               	</a>
	                </c:forEach>
	            </div>
	        </section>
	        
	        </c:otherwise>
	  	</c:choose>

        <!--Footer-->
        <%@ include file="../general/footer.html" %>
        
    </body>
</html>