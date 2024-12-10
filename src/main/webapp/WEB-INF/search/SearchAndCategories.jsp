<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Búsqueda y Categorías</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search/searchAndCategories.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/footer.css">
    </head>
    <body>
        <!--Header-->
        <header class="content box-shadow">
            <img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo.png" alt="Just Eat Logo">
            <a class="text-header" href="CreateOrderServlet.do">Mi pedido</a>
            <a class="text-header" href="UserProfileServlet.do">Perfil</a>
            <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
        </header>

        <div>
            <img id="background-img" src="${pageContext.request.contextPath}/img/bg_hero-mid-veganuary.jpg" alt="Imagen de fondo">
        </div>

        <!--Search-->
        <div id="search-container">
            <div id="search-box" class="box-shadow">
                <h1>Pide lo que te pida el cuerpo</h1>
                <p>Comida a domicilio online cerca de ti</p>
                <form action="RestaurantSearchListServlet.do" method="GET">
                	<div>
	                    <input type="text" name="search" placeholder="ej. Calle Pizarro, Cáceres">
	                    <input type="submit" value="Buscar restaurantes">
                    </div>
                    <p>Disponibilidad</p>
                    <input type="radio" name="available" id="available-all" value=-1 checked required>
	                <label for="available-all">Todos</label>
                    <input type="radio" name="available" id="available-yes" value=1>
	                <label for="available-yes">Acepta pedidos</label>
	                <input type="radio" name="available" id="available-no" value=0>
	                <label for="available-no">No acepta pedidos</label>
	                <p>Orden por valoración</p>
	                <input type="radio" name="grade" id="grade-higher-first" value=0 checked required>
	                <label for="grade-higher-first">Mayor a menor</label>
                    <input type="radio" name="grade" id="grade-lower-first" value=1>
	                <label for="grade-lower-first">Menor a mayor</label>
                </form>
            </div>
        </div>
        
        <!--Categories-->
        <section class="content">
            <h2>Tipos de cocina más populares</h2>
            <p>Busca los restaurantes más populares de tu zona y haz tu pedido de comida a domicilio online.</p>
            <div id="card-collection">
            <c:forEach items="${categoryList}" var="category">
                <a href="<c:url value='RestaurantCategoryListServlet.do?id=${category.id }'/>">
                    <div class="card box-shadow">
                        <div>
                            <img src="${pageContext.request.contextPath}/img/category-default-logo.png" alt="${category.name} Logo">
                        </div>
                        <p>${category.name}</p>
                    </div>
                </a>
            </c:forEach>
            </div>
        </section>
        
		<!--Footer-->
        <%@ include file="../general/footer.html" %>
        
    </body>
</html>