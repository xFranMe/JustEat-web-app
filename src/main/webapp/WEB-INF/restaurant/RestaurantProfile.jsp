<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${restaurant.name}</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restaurant/restaurantCollection.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/restaurant/restaurantProfile.css">
    	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/footer.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
            <div>
                <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
                <a id="text-header" href="UserProfileServlet.do">Perfil</a>
                <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
            </div>
        </header>

        <!--Restaurant section-->
        <section id="main" class="content">
        	<!--Restaurant info-->
            <div id="restaurant-info" class="box-shadow">
                <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="Logo Restaurante">
                <h1>${restaurant.name}</h1>
                <p>${restaurant.description}</p>                
                <div>Categorías:</div>
                <ul id="categories">
                <c:forEach items="${categoryList}" var="category">
					<li>${category.name}</li>
				</c:forEach>
                </ul>
                
                <div>Valoración media:</div>
                <p id="gradesAverage">${restaurant.gradesAverage}⭐</p>
                <div>Dirección:</div>
                <p id="address">${restaurant.address}</p>
                <div>Localidad:</div>
                <p id="city">${restaurant.city}</p>
                <div>Teléfono de contacto:</div>
                <p id="telephone">${restaurant.telephone}</p>
                <div>Correo electrónico:</div>
                <p id="email">${restaurant.contactEmail}</p>
                <div>Rango de precios:</div>
                <span id="minPrice">${restaurant.minPrice}</span>
                <span> - </span>
                <span id="maxPrice">${restaurant.maxPrice}</span>
                <span>€</span>
                <div>Bike Friendly:</div>
                <p id="bike-friendly"><c:choose>
						    				<c:when test="${restaurant.bikeFriendly eq 1}">Sí</c:when>
						  					<c:otherwise>
						  						No
						  					</c:otherwise>
									  </c:choose></p>
                <div>Disponibilidad:</div>
                <p id="available"><c:choose>
					    				<c:when test="${restaurant.available eq 1}">Acepta pedidos</c:when>
					  					<c:otherwise>
					  						No acepta pedidos
					  					</c:otherwise>
								  </c:choose></p>
                
           		<c:if test="${sessionScope.user.id == restaurant.idu}">
                <div><a href="<c:url value='EditRestaurantServlet.do?id=${restaurant.id}'/>">Editar este restaurante</a></div>
            	<div><a href="<c:url value='DeleteRestaurantServlet.do?id=${restaurant.id}'/>">Eliminar este restaurante</a></div>
           		</c:if>
                
            </div>
            
            <!--Reviews-->
            <div id="reviews" class="box-shadow">
                <h2>Reseñas</h2>
           		
           		<c:choose>
    				<c:when test="${not empty usersMap[sessionScope.user.id]}">
    				<h4>Parece que ya has añadido una reseña a este restaurante.</h4>
    				</c:when>
  					<c:otherwise>
  					<div><a href="<c:url value='CreateReviewServlet.do?id=${restaurant.id}'/>">Añadir reseña</a></div>
  					</c:otherwise>
			  	</c:choose>
           		
                <div id="reviews-collection">
                <c:forEach items="${requestScope.reviewsList}" var="review">
	                <div class="review-card">
                        <h3 class="review-user">${usersMap[review.idu]}</h3><span>( ${review.grade}⭐ )</span>
                        <p class="review-comment"><c:choose>
    												<c:when test="${not empty review.review}">
    													"${review.review}"
    												</c:when>
  													<c:otherwise>
  														Este usuario no ha añadido ningún comentario.
  													</c:otherwise>
			  									</c:choose></p>
	                </div>
                </c:forEach>
            	</div>
           	</div>
        </section>

        <!--Menu dishes-->
        <section id="menu" class="content">
            <h2>Menú</h2>
            <div id="menu-edit-link">
            <c:if test="${sessionScope.user.id == restaurant.idu}">
                <a href="<c:url value='CreateDishServlet.do?id=${restaurant.id}'/>">Añadir nuevo plato</a>
            </c:if>
            </div>
            <div id="dishes-collection">
                <c:forEach items="${requestScope.dishList}" var="dish">
                <div class="card">
                    <div>
                        <h3>${dish.name}</h3>
                        <p>${dish.description}</p>
                        <p>${dish.price} €</p>
                        <c:choose>
							<c:when test="${restaurant.available == 1}">
								<form method="POST" action="AddDishToOrderServlet.do?id=${dish.id}">
									<label for="amount${dish.id}">Cantidad: </label>
									<input type="number" id="amount${dish.id}" name="amount" value="1" min="1" max="100">
									
									<input type="submit" value="Añadir a pedido">
								</form>
							</c:when>
							<c:otherwise>
								<h4>Este plato no puede añadirse al pedido ya que el restaurante actualmente no acepta pedidos.</h4>
							</c:otherwise>
						</c:choose>
                        <c:if test="${sessionScope.user.id == restaurant.idu}">
                			<a href="<c:url value='EditDishServlet.do?id=${dish.id}'/>">Editar plato</a>
                			<a href="<c:url value='DeleteDishServlet.do?id=${dish.id}'/>">Eliminar plato</a>
           				</c:if>
                    
                    </div>
                    <div class="dish-picture">
                        <img src="${pageContext.request.contextPath}/img/dish-icon.png" alt="${dish.name}">
                    </div>
                </div>
                </c:forEach>
                
            </div>
        </section>
        
        
        <!--Related restaurants-->
        
        <h2>Restaurantes relacionados</h2>
        
        <!--Related restaurants: category-->
        <section class="content">
            <h3>Restaurantes con categorías similares...</h3>
            <h4><c:choose>
    				<c:when test="${not empty requestScope.similarCategoriesList}">Si buscas el mismo tipo de comida, ¡no te pierdas las siguientes opciones!</c:when>
  					<c:otherwise>
  						Parece que no hay más alternativas que se ajusten al tipo de comida ofrecida por este restaurante :(
  					</c:otherwise>
			  </c:choose></h4>
            <div class="restaurant-collection">
                <c:forEach items="${requestScope.similarCategoriesList}" var="restaurantByCategory">
                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByCategory.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       <div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByCategory.name} Logo">
                       </div>
                       <div>
                           <h3>${restaurantByCategory.name}</h3>
                           <p class="description">${restaurantByCategory.description}</p>
                           <p>Dirección: <span>${restaurantByCategory.address}</span> <span>(${restaurantByCategory.city})</span></p>
                           <p>Valoración media: <span>${restaurantByCategory.gradesAverage}</span>⭐</p> 
                       </div>        
	               	</div>
               	</a>
                </c:forEach>
            </div>
        </section>
        
        <!--Related restaurants: city-->
        <section class="content">
            <h3>Restaurantes en la misma ciudad...</h3>
            <h4><c:choose>
    				<c:when test="${not empty requestScope.similarCityList}">¿No quieres que la comida llegue fría? ¡Aquí te dejamos restaurantes localizados en la misma ciudad!</c:when>
  					<c:otherwise>
  						Lamentablemente no hay restaurantes localizados en esta misma localidad.
  					</c:otherwise>
			  </c:choose></h4>
            <div class="restaurant-collection">
                <c:forEach items="${requestScope.similarCityList}" var="restaurantByCity">
                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByCity.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       	<div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByCity.name} Logo">
                       	</div>
                       	<div>
                           	<h3>${restaurantByCity.name}</h3>
                           	<p class="description">${restaurantByCity.description}</p>
                           	<p>Dirección: <span>${restaurantByCity.address}</span> <span>(${restaurantByCity.city})</span></p>
                           	<p>Valoración media: <span>${restaurantByCity.gradesAverage}</span>⭐</p> 
                    	</div>        
	               	</div>
               	</a>
                </c:forEach>
            </div>
        </section>
        
        <!--Related restaurants: price-->
        <section class="content">
            <h3>Restaurantes con precios similares...</h3>
            <h4><c:choose>
    				<c:when test="${not empty requestScope.similarPriceList}">¿Presupuesto ajustado? No te pierdas las siguientes opciones por precios similares...</c:when>
  					<c:otherwise>
  						Actualmente no existen restaurantes que ofrezcan productos con un rango de precio similar.
  					</c:otherwise>
			  </c:choose></h4>
            <div class="restaurant-collection">
                <c:forEach items="${requestScope.similarPriceList}" var="restaurantByPrice">
                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByPrice.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       <div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByPrice.name} Logo">
                       </div>
                       <div>
                           <h3>${restaurantByPrice.name}</h3>
                           <p class="description">${restaurantByPrice.description}</p>
                           <p>Dirección: <span>${restaurantByPrice.address}</span> <span>(${restaurantByPrice.city})</span></p>
                           <p>Valoración media: <span>${restaurantByPrice.gradesAverage}</span>⭐</p> 
                       </div>        
	               	</div>
               	</a>
                </c:forEach>
            </div>
        </section>
        
        <!--Related restaurants: average grade-->
        <section class="content">
            <h3>Restaurantes con una valoración similar...</h3>
            <h4><c:choose>
    				<c:when test="${not empty requestScope.similarGradeList}">¡A continuación te dejamos restaurantes con una valoración muy parecida!</c:when>
  					<c:otherwise>
  						Sorprendentemente no existen más alternativas con una valoración similar. ¡Prueba con otras opciones para descubrir restaurantes de calidad!
  					</c:otherwise>
			  </c:choose></h4>
            <div class="restaurant-collection">
                <c:forEach items="${requestScope.similarGradeList}" var="restaurantByGrade">
                <a class="restaurant-link" href="<c:url value='RestaurantProfileServlet.do?id=${restaurantByGrade.id }'/>">
	            	<div class="restaurant-card box-shadow">  
                       <div class="restaurant-picture-box">
                           <img src="${pageContext.request.contextPath}/img/restaurant-logo.png" alt="${restaurantByGrade.name} Logo">
                       </div>
                       <div>
                           <h3>${restaurantByGrade.name}</h3>
                           <p class="description">${restaurantByGrade.description}</p>
                           <p>Dirección: <span>${restaurantByGrade.address}</span> <span>(${restaurantByGrade.city})</span></p>
                           <p>Valoración media: <span>${restaurantByGrade.gradesAverage}</span>⭐</p> 
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