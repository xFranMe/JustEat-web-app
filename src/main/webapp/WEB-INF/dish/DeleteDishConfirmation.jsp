<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Eliminar ${dish.name}</title>
		<link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/deleteConfirmation.css">
	</head>
	<body>
		<div class="delete-background content">
			<div class="delete-form">
				<img src="${pageContext.request.contextPath}/img/dish-icon.png" alt="Logo plato">
				<h1>Eliminar Plato</h1>
				<p>¿Eliminar permanentemente el perfil del plato ${dish.name}?</p>
				<p>Esta acción no podrá ser revertida y supondrá el borrado de todos los platos y valoraciones del mismo.</p>
				<form method="POST" action="DeleteDishServlet.do">
					<input type="hidden" name="id" value="${dish.id}"> 
					<input id="button" type="submit" value="Eliminar plato">
				</form>
			</div>
		</div>
	</body>
</html>