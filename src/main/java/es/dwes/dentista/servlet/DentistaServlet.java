package es.dwes.dentista.servlet;

import es.dwes.dentista.dao.ClinicaDAO;
import es.dwes.dentista.model.Cita;
import es.dwes.dentista.model.Paciente;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dentista")
public class DentistaServlet extends HttpServlet {
    private final ClinicaDAO dao = new ClinicaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = parametro(request, "accion", "inicio");
        try {
            switch (accion) {
                case "inicio" -> mostrar(request, response, "index.jsp");
                case "pacientes" -> {
                    request.setAttribute("pacientes", dao.listarPacientes());
                    aplicarMensaje(request);
                    mostrar(request, response, "listadoPacientes.jsp");
                }
                case "citas" -> {
                    boolean todas = "1".equals(request.getParameter("todas"));
                    request.setAttribute("todas", todas);
                    request.setAttribute("citas", dao.listarCitas(todas));
                    aplicarMensaje(request);
                    mostrar(request, response, "listadoCitas.jsp");
                }
                case "formularioPaciente" -> mostrar(request, response, "formularioPaciente.jsp");
                case "formularioCita" -> {
                    request.setAttribute("pacientes", dao.listarPacientesParaCita());
                    request.setAttribute("odontologos", dao.listarOdontologos());
                    mostrar(request, response, "formularioCita.jsp");
                }
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            getServletContext().log("Error consultando la base de datos de la clínica", e);
            mostrarError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo consultar la base de datos. Comprueba la conexión y los datos de acceso.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = parametro(request, "accion", "");
        try {
            switch (accion) {
                case "guardarPaciente" -> {
                    Paciente paciente = nuevoPaciente(request);
                    dao.insertarPaciente(paciente);
                    response.sendRedirect(request.getContextPath()
                            + "/dentista?accion=pacientes&estado=pacienteCreado");
                }
                case "guardarCita" -> {
                    Cita cita = nuevaCita(request);
                    dao.insertarCita(cita);
                    response.sendRedirect(request.getContextPath()
                            + "/dentista?accion=citas&estado=citaCreada");
                }
                case "borrarPaciente" -> {
                    dao.borrarPaciente(entero(request, "id_paciente"));
                    response.sendRedirect(request.getContextPath()
                            + "/dentista?accion=pacientes&estado=pacienteBorrado");
                }
                case "borrarCita" -> {
                    dao.borrarCita(entero(request, "id_cita"));
                    response.sendRedirect(request.getContextPath()
                            + "/dentista?accion=citas&estado=citaBorrada");
                }
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", e.getMessage());
            if ("guardarPaciente".equals(accion)) {
                mostrar(request, response, "formularioPaciente.jsp");
            } else if ("guardarCita".equals(accion)) {
                cargarDatosFormularioCita(request, response);
            } else {
                mostrarError(request, response, HttpServletResponse.SC_BAD_REQUEST,
                        "La solicitud contiene datos no válidos.");
            }
        } catch (SQLException e) {
            getServletContext().log("Error modificando la base de datos de la clínica", e);
            mostrarError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo guardar el cambio. Comprueba los datos y la conexión a la base de datos.");
        }
    }

    private Paciente nuevoPaciente(HttpServletRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNombre(requerido(request, "nombre"));
        paciente.setApellidos(requerido(request, "apellidos"));
        paciente.setDni(requerido(request, "dni"));
        if (paciente.getDni().length() > 9) {
            throw new IllegalArgumentException("El DNI no puede superar 9 caracteres.");
        }
        String nacimiento = parametro(request, "fecha_nacimiento", "");
        if (!nacimiento.isBlank()) {
            paciente.setFechaNacimiento(fechaValida(nacimiento));
        }
        paciente.setTelefono(parametro(request, "telefono", ""));
        paciente.setEmail(parametro(request, "email", ""));
        paciente.setDireccion(parametro(request, "direccion", ""));
        paciente.setAlergias(parametro(request, "alergias", ""));
        return paciente;
    }

    private Cita nuevaCita(HttpServletRequest request) {
        Cita cita = new Cita();
        cita.setFecha(fechaValida(requerido(request, "fecha")));
        String hora;
        try {
            hora = LocalTime.parse(requerido(request, "hora")).toString();
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("La hora indicada no es válida.");
        }
        cita.setHora(hora.length() == 5 ? hora + ":00" : hora);
        cita.setIdPaciente(entero(request, "id_paciente"));
        cita.setIdOdontologo(entero(request, "id_odontologo"));
        cita.setMotivo(parametro(request, "motivo", ""));
        return cita;
    }

    private String fechaValida(String valor) {
        try {
            return LocalDate.parse(valor).toString();
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("La fecha indicada no es válida.");
        }
    }

    private int entero(HttpServletRequest request, String nombre) {
        try {
            int valor = Integer.parseInt(requerido(request, nombre));
            if (valor <= 0) {
                throw new IllegalArgumentException("El identificador indicado no es válido.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador indicado no es válido.");
        }
    }

    private String requerido(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Completa todos los campos obligatorios.");
        }
        return valor.trim();
    }

    private String parametro(HttpServletRequest request, String nombre, String defecto) {
        String valor = request.getParameter(nombre);
        return valor == null ? defecto : valor;
    }

    private void cargarDatosFormularioCita(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pacientes", dao.listarPacientesParaCita());
            request.setAttribute("odontologos", dao.listarOdontologos());
            mostrar(request, response, "formularioCita.jsp");
        } catch (SQLException e) {
            getServletContext().log("Error cargando los datos del formulario de cita", e);
            mostrarError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se pudo cargar el formulario. Comprueba la conexión a la base de datos.");
        }
    }

    private void aplicarMensaje(HttpServletRequest request) {
        String estado = request.getParameter("estado");
        if (estado == null) {
            return;
        }
        switch (estado) {
            case "pacienteCreado" -> request.setAttribute("mensaje", "Paciente dado de alta.");
            case "pacienteBorrado" -> request.setAttribute("mensaje",
                    "Paciente eliminado; sus citas asociadas también se han eliminado.");
            case "citaCreada" -> request.setAttribute("mensaje", "Cita registrada.");
            case "citaBorrada" -> request.setAttribute("mensaje", "Cita anulada.");
            default -> { }
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response, String vista)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vistas/" + vista).forward(request, response);
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, int estado, String mensaje)
            throws ServletException, IOException {
        response.setStatus(estado);
        request.setAttribute("error", mensaje);
        mostrar(request, response, "error.jsp");
    }
}
