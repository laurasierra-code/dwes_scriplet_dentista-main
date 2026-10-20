package es.dwes.dentista.model;

public class Cita {
    private int id;
    private String fecha;
    private String hora;
    private String motivo;
    private int idPaciente;
    private int idOdontologo;
    private String nombrePaciente;
    private String apellidosPaciente;
    private String nombreOdontologo;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    public int getIdOdontologo() { return idOdontologo; }
    public void setIdOdontologo(int idOdontologo) { this.idOdontologo = idOdontologo; }
    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }
    public String getApellidosPaciente() { return apellidosPaciente; }
    public void setApellidosPaciente(String apellidosPaciente) { this.apellidosPaciente = apellidosPaciente; }
    public String getNombreOdontologo() { return nombreOdontologo; }
    public void setNombreOdontologo(String nombreOdontologo) { this.nombreOdontologo = nombreOdontologo; }
}
