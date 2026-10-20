<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Listado de citas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
  </head>
  <body>
    <h1><c:choose><c:when test="${todas}">Todas las citas</c:when><c:otherwise>Próximas citas</c:otherwise></c:choose></h1>
    <c:if test="${not empty mensaje}"><p class="nota"><c:out value="${mensaje}"/></p></c:if>
    <table>
      <tr><th>Id cita</th><th>Fecha</th><th>Hora</th><th>Paciente</th><th>Motivo</th><th>Odontólogo</th><th>Acciones</th></tr>
      <c:forEach var="cita" items="${citas}">
        <tr>
          <td><c:out value="${cita.id}"/></td>
          <td><c:out value="${cita.fecha}"/></td>
          <td><c:out value="${cita.hora}"/></td>
          <td><c:out value="${cita.apellidosPaciente}, ${cita.nombrePaciente}"/></td>
          <td><c:out value="${cita.motivo}"/></td>
          <td><c:out value="${cita.nombreOdontologo}"/></td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/dentista">
              <input type="hidden" name="accion" value="borrarCita">
              <input type="hidden" name="id_cita" value="${cita.id}">
              <input type="submit" value="Anular">
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
    <p>
      <c:choose>
        <c:when test="${todas}"><a href="${pageContext.request.contextPath}/dentista?accion=citas">Ver sólo las próximas citas</a></c:when>
        <c:otherwise><a href="${pageContext.request.contextPath}/dentista?accion=citas&amp;todas=1">Ver el histórico completo</a></c:otherwise>
      </c:choose>
      | <a href="${pageContext.request.contextPath}/dentista?accion=formularioCita">Nueva cita</a>
      | <a href="${pageContext.request.contextPath}/dentista?accion=inicio">Volver al inicio</a>
    </p>
  </body>
</html>
