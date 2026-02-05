package modelo;

import java.sql.Date;
import java.sql.Time;

public class Sesion {
    private int idSesion;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private int idSala;      // FK hacia Sala
    private int idPelicula;  // FK hacia Pelicula
    private int espectadores;
    private double precioSesion;

    public Sesion() {
    }

    public Sesion(int idSesion, Date fecha, Time horaInicio, Time horaFin, int idSala, int idPelicula, int espectadores, double precioSesion) {
        this.idSesion = idSesion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idSala = idSala;
        this.idPelicula = idPelicula;
        this.espectadores = espectadores;
        this.precioSesion = precioSesion;
    }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }

    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }

    public int getIdSala() { return idSala; }
    public void setIdSala(int idSala) { this.idSala = idSala; }

    public int getIdPelicula() { return idPelicula; }
    public void setIdPelicula(int idPelicula) { this.idPelicula = idPelicula; }

    public int getEspectadores() { return espectadores; }
    public void setEspectadores(int espectadores) { this.espectadores = espectadores; }

    public double getPrecioSesion() { return precioSesion; }
    public void setPrecioSesion(double precioSesion) { this.precioSesion = precioSesion; }

    @Override
    public String toString() {
        return "Sesion [fecha=" + fecha + ", hora=" + horaInicio + ", precio=" + precioSesion + "]";
    }
}