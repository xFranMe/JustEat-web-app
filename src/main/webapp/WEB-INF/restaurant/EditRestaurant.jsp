<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><c:choose>
    				<c:when test="${restaurant.id == null || restaurant.id == -1}">Crear restaurante</c:when>
  					<c:otherwise>
  						Editar ${restaurant.name}
  					</c:otherwise>
			  </c:choose></title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/form.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restaurant/editRestaurant.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
            <div>
                <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
                <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
            </div>
        </header>

        <!--Restaurant Edition Container-->
        <div class="content">
    			
            <!--Restaurant Info Form-->
            <form action="?" method="POST">
            
                <fieldset class="box-shadow">
                
                	<c:forEach items="${messages}" var="error">
	        		<p class="error-text">${error.value}</p>
	    			</c:forEach>
                
                    <legend><c:choose>
			    				<c:when test="${restaurant.id == null || restaurant.id == -1}">Nuevo restaurante</c:when>
			  					<c:otherwise>
			  						Editar ${restaurant.name}
			  					</c:otherwise>
						  	</c:choose></legend>
						  	
					<input type="hidden" name="id" value="${restaurant.id }">
                    
                    <label for="name">Nombre del restaurante</label>
                    <input type="text" id="name" name="name" placeholder="Nombre del restaurante" value="${restaurant.name}" required>
                    
                    <label for="description">Descripción del restaurante</label>
                    <textarea name="description" id="description" cols="10" rows="4" required>${restaurant.description}</textarea>
                    
                    <label for="address">Dirección</label>
                    <textarea name="address" id="address" cols="10" rows="2" required>${restaurant.address}</textarea>
                    
                    <label for="city">Localidad</label>
                    <input type="text" id="city" name="city" placeholder="Ej. Cáceres" value="${restaurant.city}" required>
                    
                    <label for="telephone">Teléfono</label>
                    <input type="tel" name="telephone" id="telephone" placeholder="Ej. 923 942 532" value="${restaurant.telephone}" required>

                    <label for="contactEmail">Email</label>
                    <input type="email" name="contactEmail" id="contactEmail" placeholder="example@domain.com" value="${restaurant.contactEmail}" required>
                    
                    <label for="minPrice">Rango de precios - Precio mínimo</label>
                    <input type="number" min="0" max="1000000" step="0.01" name="minPrice" id="minPrice" value="${restaurant.minPrice}" required>

                    <label for="maxPrice">Rango de precios - Precio máximo</label>
                    <input type="number" min="0" max="1000000" step="0.01" name="maxPrice" id="maxPrice" value="${restaurant.maxPrice}" required>
                    
                    <div id="categories-container">
                        <p>Categorías</p>
                        
                    	<c:forEach items="${requestScope.categoryList}" var="category">
       					<input type="checkbox" name="categories" value="${category.id}" id="${category.id}" <c:if test="${not empty requestScope.restaurantCategoriesMap[category.id]}">checked</c:if>>
                       	<label for="${category.id}">${category.name}</label>
   						</c:forEach> 
                    </div>
                    
                    <div id="bike-friendly-container">
                        <p>Bike Friendly</p>
                        <input type="radio" name="bikeFriendly" id="bike-friendly-yes" value=1 required <c:if test="${restaurant.bikeFriendly == 1}">checked</c:if>>
                        <label for="bike-friendly-yes">Sí</label>
                        <input type="radio" name="bikeFriendly" id="bike-friendly-no" value=0 <c:if test="${restaurant.bikeFriendly == 0}">checked</c:if>>
                        <label for="bike-friendly-no">No</label>
                    </div>
                    
                    <div id="available-container">
                        <p>Disponibilidad</p>
                        <input type="radio" name="available" id="available-yes" value=1 required <c:if test="${restaurant.available == 1}">checked</c:if>>
                        <label for="available-yes">Acepta pedidos</label>
                        <input type="radio" name="available" id="available-no" value=0 <c:if test="${restaurant.available == 0}">checked</c:if>>
                        <label for="available-no">No acepta pedidos</label>
                    </div>
                    
                    <input type="submit" value=<c:choose>
								    				<c:when test="${restaurant.id == null || restaurant.id == -1}">"Añadir restaurante"</c:when>
								  					<c:otherwise>
								  						"Confirmar cambios"
								  					</c:otherwise>
											  </c:choose>>
                </fieldset>
            </form>
        </div>
    </body>
</html>