<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eliminar plato</title>
    <link rel="icon" href="${pageContext.request.contextPath}/img/icon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/content.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/general/deleteConfirmation.css">
</head>
    <body>
        <div>
            <h1>Eliminar plato "${dish.name}" del pedido</h1>
            <p>¿Desea eliminar el plato seleccionado del pedido actual?</p>
            <p>Esta acción supondrá el borrado de todas sus unidades del pedido.</p>
            <form method="POST" action="DeleteDishFromOrderServlet.do">
            	<input type="hidden" name="id" value="${dish.id}">
                <input id="button" type="submit" value="Eliminar plato">
            </form>
        </div>
    </body>
</html>