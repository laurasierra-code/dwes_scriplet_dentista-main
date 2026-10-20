<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Nuevo paciente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h2>Introduzca los datos del nuevo paciente:</h2>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/dentista">
      <input type="hidden" name="accion" value="guardarPaciente">
      Nombre <input type="text" name="nombre" value="<c:out value='${param.nombre}'/>" required><br>
      Apellidos <input type="text" name="apellidos" value="<c:out value='${param.apellidos}'/>" required><br>
      DNI <input type="text" name="dni" maxlength="9" value="<c:out value='${param.dni}'/>" required><br>
      Fecha de nacimiento <input type="date" name="fecha_nacimiento" value="<c:out value='${param.fecha_nacimiento}'/>"><br>
      Teléfono <input type="text" name="telefono" value="<c:out value='${param.telefono}'/>"><br>
      Correo electrónico <input type="email" name="email" value="<c:out value='${param.email}'/>"><br>
      Dirección <input type="text" name="direccion" value="<c:out value='${param.direccion}'/>"><br>
      Alergias o notas <input type="text" name="alergias" value="<c:out value='${param.alergias}'/>"><br>
      <input type="submit" value="Aceptar">
    </form>
    <p><a href="${pageContext.request.contextPath}/dentista?accion=inicio">Volver al inicio</a></p>
  </body>
</html>
