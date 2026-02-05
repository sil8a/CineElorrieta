package modelo;

public class Pelicula {
    private int idPelicula;
    private String titulo;
    private int duracion; // en minutos
    private String genero;
    private double precioBase;

    public Pelicula() {
    }

    public Pelicula(int idPelicula, String titulo, int duracion, String genero, double precioBase) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.precioBase = precioBase;
    }

    public int getIdPelicula() { return idPelicula; }
    public void setIdPelicula(int idPelicula) { this.idPelicula = idPelicula; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    @Override
    public String toString() {
        return titulo + " (" + duracion + " min)";
    }
}