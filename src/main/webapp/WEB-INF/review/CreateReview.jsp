<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>¡Añade tu reseña!</title>
        <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/form.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review/createReview.css">
    </head>
    <body>
        <!--Header-->
        <header class="content">
            <div>
                <a id="logo-link" href="SearchAndCategoriesServlet.do"><img id="logo" src="${pageContext.request.contextPath}/img/just-eat-logo-white.png" alt="Just Eat Logo"></a>
                <img id="country" src="${pageContext.request.contextPath}/img/spanish-flag.png" alt="España" title="Región: España">
            </div>
        </header>

        <!--Add Review Container-->
        <div class="content">
    			
            <!--Review Form-->
            <form action="CreateReviewServlet.do" method="POST">
                <fieldset class="box-shadow">
                
                <c:forEach items="${messages}" var="error">
	        		<p class="error-text">${error.value}</p>
	    		</c:forEach>
                
                    <legend>Completa tu reseña de ${restaurant.name}</legend>
						  	
					<input type="hidden" name="id" value="${restaurant.id }">
                    
                    <label for="grade">Valoración</label>
                    <input type="number" min="1" max="5" step="1" name="grade" id="grade" required>
                    
                    <label for="comment">Comentario (opcional)</label>
                    <textarea name="comment" id="comment" cols="10" rows="4"></textarea>
                    
                    <input type="submit" value="Publicar reseña">
                    
                </fieldset>
            </form>
        </div>
    </body>
</html>