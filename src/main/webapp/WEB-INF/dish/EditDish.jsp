<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title><c:choose>
	  					<c:when test="${dish.id == null || dish.id == -1}">Añadir plato</c:when>
						<c:otherwise>
							Editar plato
						</c:otherwise>
		  		</c:choose></title>
		<link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/form.css">
		<link rel="stylesheet"	href="${pageContext.request.contextPath}/css/dish/editDish.css">
	</head>
	<body>
		<header class="content">
			<div>
				<a id="logo-link" href="SearchAndCategoriesServlet.do"> <img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a> 
				<img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
			</div>
		</header>
	
		<div class="content">
			<form action="?" method="POST">
				<fieldset class="box-shadow">
					<legend><c:choose>
		     					<c:when test="${dish.id == null || dish.id == -1}">Nuevo plato</c:when>
		   						<c:otherwise>
		   							Editar "${dish.name}"
		   						</c:otherwise>
						  	</c:choose></legend>
	
					<c:forEach items="${messages}" var="error">
	        			<p class="error-text">${error.value}</p>
	    			</c:forEach>
	    			
	    			<input type="hidden" name="idr" value=<c:choose>
																<c:when test="${dish.id == null || dish.id == -1}">"${requestScope.idr}"</c:when>
												    			<c:otherwise>		
																	"${dish.idr}"
																</c:otherwise>
														  </c:choose>>
														  
					<input type="hidden" name="idd" value="${dish.id}">
	    			
					<label for="name">Nombre</label> 
					<input type="text" id="name" name="name" placeholder="Nombre del plato"	value="${dish.name}" required> 
					
					<label for="description">Descripción</label>
					<textarea name="description" id="description" cols="10" rows="3" required>${dish.description}</textarea>
	
					<label for="price">Precio</label> 
					<input type="number" min="0" max="100" step="0.01" name="price" id="price" value="${dish.price}" required>

					<input type="submit" value=<c:choose>
													<c:when test="${dish.id == null || dish.id == -1}">"Añadir plato"</c:when>
												    <c:otherwise>
												    	"Confirmar cambios"
													</c:otherwise>
												</c:choose>>
				</fieldset>
			</form>
		</div>
	
	</body>
</html>