<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1 class="error">No se pudo completar la operación</h1>
    <p class="error"><c:out value="${error}"/></p>
    <p><a href="${pageContext.request.contextPath}/dentista?accion=inicio">Volver al inicio</a></p>
  </body>
</html>
