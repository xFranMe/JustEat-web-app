<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Eliminar cuenta</title>
		<link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/deleteConfirmation.css">
	</head>
	<body>
		<div class="delete-background content">
			<div class="delete-form">
				<img src="${pageContext.request.contextPath}/img/user-logo.jpg" alt="Logo usuario">
				<h1>Eliminar cuenta</h1>
				<p>¿Eliminar permanentemente la cuenta asociada a ${sessionScope.user.email}?</p>
				<p>Esta acción no podrá ser revertida y supondrá el borrado de todos los restaurantes creados por el usuario.</p>
				<form method="POST" action="DeleteUserServlet.do">
					<input id="button" type="submit" value="Eliminar cuenta">
				</form>
			</div>
		</div>	
	</body>
</html>