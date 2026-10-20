<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Clínica Dental</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1>Bienvenido a la aplicación de gestión de citas de la clínica dental</h1>
    <p>Seleccione una opción:</p>
    <ul>
      <li><a href="${pageContext.request.contextPath}/dentista?accion=pacientes">Ver listado de pacientes</a></li>
      <li><a href="${pageContext.request.contextPath}/dentista?accion=citas">Ver próximas citas</a></li>
      <li><a href="${pageContext.request.contextPath}/dentista?accion=formularioCita">Nueva cita</a></li>
      <li><a href="${pageContext.request.contextPath}/dentista?accion=formularioPaciente">Nuevo paciente</a></li>
    </ul>
  </body>
</html>
