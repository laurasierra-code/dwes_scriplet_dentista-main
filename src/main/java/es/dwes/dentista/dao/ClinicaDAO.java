package es.dwes.dentista.dao;

import es.dwes.dentista.model.Cita;
import es.dwes.dentista.model.Odontologo;
import es.dwes.dentista.model.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ClinicaDAO {
    private static final String URL = setting("DB_URL",
            "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
    private static final String USER = setting("DB_USER", "user");
    private static final String PASSWORD = setting("DB_PASSWORD", "user");

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el controlador JDBC de MySQL.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Paciente> listarPacientes() throws SQLException {
        String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                + "p.telefono, p.email, p.direccion, p.alergias, "
                + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS numero_citas "
                + "FROM paciente p ORDER BY p.apellidos, p.nombre";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rows.getInt("id_paciente"));
                paciente.setNombre(rows.getString("nombre"));
                paciente.setApellidos(rows.getString("apellidos"));
                paciente.setDni(rows.getString("dni"));
                paciente.setFechaNacimiento(rows.getString("fecha_nacimiento"));
                paciente.setTelefono(rows.getString("telefono"));
                paciente.setEmail(rows.getString("email"));
                paciente.setDireccion(rows.getString("direccion"));
                paciente.setAlergias(rows.getString("alergias"));
                paciente.setNumeroCitas(rows.getInt("numero_citas"));
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }

    public List<Cita> listarCitas(boolean todas) throws SQLException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, p.id_paciente, "
                + "p.nombre AS nombre_paciente, p.apellidos, o.nombre AS odontologo "
                + "FROM cita c JOIN paciente p ON p.id_paciente = c.id_paciente "
                + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                + (todas ? "" : "WHERE c.fecha >= CURDATE() ")
                + "ORDER BY c.fecha, c.hora";
        List<Cita> citas = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                Cita cita = new Cita();
                cita.setId(rows.getInt("id_cita"));
                cita.setFecha(rows.getString("fecha"));
                String hora = rows.getString("hora");
                cita.setHora(hora != null && hora.length() > 5 ? hora.substring(0, 5) : hora);
                cita.setMotivo(rows.getString("motivo"));
                cita.setIdPaciente(rows.getInt("id_paciente"));
                cita.setNombrePaciente(rows.getString("nombre_paciente"));
                cita.setApellidosPaciente(rows.getString("apellidos"));
                cita.setNombreOdontologo(rows.getString("odontologo"));
                citas.add(cita);
            }
        }
        return citas;
    }

    public List<Paciente> listarPacientesParaCita() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT id_paciente, nombre, apellidos FROM paciente ORDER BY apellidos, nombre";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rows.getInt("id_paciente"));
                paciente.setNombre(rows.getString("nombre"));
                paciente.setApellidos(rows.getString("apellidos"));
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }

    public List<Odontologo> listarOdontologos() throws SQLException {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT id_odontologo, nombre, especialidad FROM odontologos ORDER BY nombre";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                Odontologo odontologo = new Odontologo();
                odontologo.setId(rows.getInt("id_odontologo"));
                odontologo.setNombre(rows.getString("nombre"));
                odontologo.setEspecialidad(rows.getString("especialidad"));
                odontologos.add(odontologo);
            }
        }
        return odontologos;
    }

    public void insertarPaciente(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente "
                + "(nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, paciente.getNombre());
            statement.setString(2, paciente.getApellidos());
            statement.setString(3, paciente.getDni());
            if (paciente.getFechaNacimiento() == null || paciente.getFechaNacimiento().isBlank()) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
            }
            statement.setString(5, paciente.getTelefono());
            statement.setString(6, paciente.getEmail());
            statement.setString(7, paciente.getDireccion());
            statement.setString(8, paciente.getAlergias());
            statement.executeUpdate();
        }
    }

    public void insertarCita(Cita cita) throws SQLException {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(cita.getFecha()));
            statement.setTime(2, java.sql.Time.valueOf(cita.getHora()));
            statement.setInt(3, cita.getIdPaciente());
            statement.setInt(4, cita.getIdOdontologo());
            statement.setString(5, cita.getMotivo());
            statement.executeUpdate();
        }
    }

    public void borrarPaciente(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM paciente WHERE id_paciente = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void borrarCita(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM cita WHERE id_cita = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
